package com.portfolio.controller;
import com.portfolio.service.JDoodleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;


@RestController
@RequestMapping("/api/jdoodle")
@CrossOrigin("*")
public class JDoodleController {

    private final JDoodleService jdoodleService;

    public JDoodleController(JDoodleService jdoodleService) {
        this.jdoodleService = jdoodleService;
    }

    @PostMapping("/run")
    public ResponseEntity<Map<String, Object>> runCode(@RequestBody Map<String, String> payload) {
        String script = payload.get("script");
        String language = payload.get("language");
        String versionIndex = payload.get("versionIndex");
        String stdin = payload.getOrDefault("stdin", "");

        Map<String, Object> result = jdoodleService.executeCode(script, language, versionIndex, stdin);
        return ResponseEntity.ok(result);
    }
}



