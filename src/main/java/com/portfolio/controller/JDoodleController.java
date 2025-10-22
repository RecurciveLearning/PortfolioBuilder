package com.portfolio.controller;
import com.portfolio.service.JDoodleService;
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
    public Map<String, Object> runCode(
            @RequestParam String code,
            @RequestParam String language,
            @RequestParam String versionIndex,
            @RequestParam(required = false) String input) {
        return jdoodleService.executeCode(code, language, versionIndex, input);
    }
}



