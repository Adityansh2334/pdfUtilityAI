package com.adrik.pdfutilityai.service;

import com.adrik.pdfutilityai.PdfUtilityAiAgent;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

import static dev.langchain4j.data.document.loader.FileSystemDocumentLoader.loadDocument;

@Service
public class PdfService {

    @Autowired
    private PdfUtilityAiAgent pdfUtilityAiAgent;

    @Autowired
    private EmbeddingStoreIngestor embeddingStoreIngestor;

    public String getQueryFromAgent(String message) {
        return pdfUtilityAiAgent.chat(message);
    }

    public String uploadFile(MultipartFile multipartFile) throws IOException {

        Path tempFile = convertMultipartFileToPath(multipartFile);

        // 2. Load an example document ("Miles of Smiles" terms of use)
        Document document = loadDocument(tempFile, new ApachePdfBoxDocumentParser());
        embeddingStoreIngestor.ingest(document);

        return "Success";
    }

    private Path convertMultipartFileToPath(MultipartFile multipartFile) throws IOException {
        // Create a temporary directory to store the file
        Path tempDir = Files.createTempDirectory("tempPdfUtilityAi");

        // Create a Path for the temporary file
        Path tempFile = tempDir.resolve(Objects.requireNonNull(multipartFile.getOriginalFilename()));

        // Transfer the content of MultipartFile to the temporary file
        multipartFile.transferTo(tempFile.toFile());

        return tempFile;
    }

}
