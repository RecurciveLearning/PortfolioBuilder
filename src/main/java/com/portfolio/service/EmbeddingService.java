package com.portfolio.service;

import ai.onnxruntime.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Map;

@Service
public class EmbeddingService {

    private OrtEnvironment env;
    private OrtSession session;

    @PostConstruct
    public void init() {
        try {
            env = OrtEnvironment.getEnvironment();

            // Load ONNX model from resources to a temporary file
            InputStream modelStream = getClass().getResourceAsStream("/models/all-MiniLM-L6-v2.onnx");
            if (modelStream == null) {
                throw new RuntimeException("ONNX model not found in resources!");
            }

            File tempFile = File.createTempFile("all-MiniLM-L6-v2", ".onnx");
            tempFile.deleteOnExit();
            Files.copy(modelStream, tempFile.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            session = env.createSession(tempFile.getAbsolutePath(), new OrtSession.SessionOptions());
            System.out.println("ONNX model loaded successfully!");

            // Optional: print model input info
            System.out.println("Model inputs: " + session.getInputInfo());
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize ONNX session", e);
        }
    }

    /**
     * Get embedding for a query.
     * tokenizedInput = 1 x seq_len integer IDs
     */
    public float[] getQueryEmbedding(long[][] tokenizedInput) throws OrtException {
        try (OnnxTensor inputIdsTensor = OnnxTensor.createTensor(env, tokenizedInput);
             OnnxTensor attentionMaskTensor = OnnxTensor.createTensor(env, createAttentionMask(tokenizedInput.length, tokenizedInput[0].length));
             OrtSession.Result output = session.run(Map.of(
                     "input_ids", inputIdsTensor,
                     "attention_mask", attentionMaskTensor
             ))) {

            Object rawOutput = output.get(0).getValue();

            if (rawOutput instanceof float[][][]) {
                float[][][] out3d = (float[][][]) rawOutput;
                return out3d[0][0];  // take batch 0, seq 0
            } else if (rawOutput instanceof float[][]) {
                return ((float[][]) rawOutput)[0];
            } else {
                throw new RuntimeException("Unexpected output type: " + rawOutput.getClass());
            }
        }
    }


    /**
     * Helper to create attention mask (all ones)
     */
    private long[][] createAttentionMask(int batchSize, int seqLen) {
        long[][] mask = new long[batchSize][seqLen];
        for (int i = 0; i < batchSize; i++) {
            for (int j = 0; j < seqLen; j++) {
                mask[i][j] = 1;
            }
        }
        return mask;
    }
}
