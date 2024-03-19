package com.adrik.pdfutilityai.controller;


import com.adrik.pdfutilityai.service.PdfService;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;
import java.util.Objects;


@RestController
public class PdfTestController {

    @Autowired
    private PdfService pdfService;

    @PostMapping("/pdf/ai/upload")
    public Map uploadPDF(@RequestParam(value = "pdfFile") MultipartFile pdfFile) throws IOException {
        if (!Objects.equals(pdfFile.getContentType(), "application/pdf")) {
            throw new IllegalArgumentException("Invalid file type");
        }
        pdfService.uploadFile(pdfFile);
        return Map.of("result", "Stored in Vector DB");
    }

    @GetMapping("/pdf/ai/search")
    public Map getQueryValue(@RequestParam(value = "query") String message) {
        String responseFromAgent = pdfService.getQueryFromAgent(message);
        return Map.of("queryResult", responseFromAgent);
    }

}
