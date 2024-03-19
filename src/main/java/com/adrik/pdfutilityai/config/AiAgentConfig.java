package com.adrik.pdfutilityai.config;

import com.adrik.pdfutilityai.PdfUtilityAiAgent;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.embedding.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;


@Configuration
public class AiAgentConfig {

    @Bean
    PdfUtilityAiAgent pdfUtilityAgent(ChatLanguageModel chatLanguageModel,
                                      ContentRetriever contentRetriever) {
        return AiServices.builder(PdfUtilityAiAgent.class)
                .chatLanguageModel(chatLanguageModel)
                .chatMemory(MessageWindowChatMemory.withMaxMessages(20))
                .contentRetriever(contentRetriever)
                .build();
    }

    @Bean
    ContentRetriever contentRetriever(EmbeddingStore<TextSegment> embeddingStore, EmbeddingModel embeddingModel) {

        // You will need to adjust these parameters to find the optimal setting, which will depend on two main factors:
        // - The nature of your data
        // - The embedding model you are using
        int maxResults = 1;
        double minScore = 0.6;

        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(maxResults)
                .minScore(minScore)
                .build();
    }

    @Bean
    EmbeddingModel embeddingModel() {
        return new AllMiniLmL6V2EmbeddingModel();
    }

    @Bean
    EmbeddingStore<TextSegment> embeddingStore(EmbeddingModel embeddingModel) throws IOException {

        // Normally, you would already have your embedding store filled with your data.
        // However, for the purpose of this demonstration, we will:

        // 1. Create an pgVector embedding store
//        DockerImageName dockerImageName = DockerImageName.parse("ankane/pgvector:latest");
//        try (PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>(dockerImageName)) {
//            postgreSQLContainer.start();
            return PgVectorEmbeddingStore.builder()
                    .host("localhost")
                    .port(5432)
                    .database("postgres")
                    .user("postgres")
                    .password("postgres")
                    .table("vectorTest")
                    .dimension(384)
                    .build();

    }

    @Bean
    public EmbeddingStoreIngestor embeddigStoreIngestor(EmbeddingStore<TextSegment> embeddingStore,EmbeddingModel embeddingModel) {
        // 3. Split the document into segments 100 tokens each
        // 4. Convert segments into embeddings
        // 5. Store embeddings into embedding store
        // All this can be done manually, but we will use EmbeddingStoreIngestor to automate this:
        DocumentSplitter documentSplitter = DocumentSplitters.recursive(300, 0);
        return EmbeddingStoreIngestor.builder()
                .documentSplitter(documentSplitter)
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
    }


}
