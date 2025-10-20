package com.portfolio.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
public class EmbeddingEntry implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String type;
    private String text;
    private List<Double> embedding;

    public EmbeddingEntry(String id, String type, String text, List<Double> embedding) {
        this.id = id;
        this.type = type;
        this.text = text;
        this.embedding = embedding;
    }
}
