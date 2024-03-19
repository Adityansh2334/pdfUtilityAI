package com.adrik.pdfutilityai.controller;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class PdfTestController {

    @Autowired
    private VectorStore vectorStore;


    @PostMapping("/pdf/ai/upload")
    public Map embed(@RequestParam(value = "pdfFile") MultipartFile pdfFile) {
        if (!Objects.equals(pdfFile.getContentType(), "application/pdf")) {
            throw new IllegalArgumentException("Invalid file type");
        }
        
       vectorStore.add(List.of(document));
//        List<Document> results = vectorStore.similaritySearch(SearchRequest.query(message).withTopK(2));
        return Map.of("embedding", results);
    }




}
