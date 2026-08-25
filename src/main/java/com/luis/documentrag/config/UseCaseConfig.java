package com.luis.documentrag.config;

import com.luis.documentrag.application.usecase.AskQuestionUseCase;
import com.luis.documentrag.application.usecase.IngestDocumentUseCase;
import com.luis.documentrag.domain.ChatModelPort;
import com.luis.documentrag.domain.ChunkRepositoryPort;
import com.luis.documentrag.domain.DocumentRepositoryPort;
import com.luis.documentrag.domain.EmbeddingPort;
import com.luis.documentrag.domain.TextChunker;
import com.luis.documentrag.domain.TextExtractorPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    public TextChunker textChunker() {
        return new TextChunker();
    }

    @Bean
    public IngestDocumentUseCase ingestDocumentUseCase(
            TextExtractorPort textExtractorPort,
            TextChunker textChunker,
            EmbeddingPort embeddingPort,
            DocumentRepositoryPort documentRepositoryPort,
            ChunkRepositoryPort chunkRepositoryPort
    ) {
        return new IngestDocumentUseCase(
                textExtractorPort, textChunker, embeddingPort, documentRepositoryPort, chunkRepositoryPort);
    }

    @Bean
    public AskQuestionUseCase askQuestionUseCase(
            EmbeddingPort embeddingPort,
            ChunkRepositoryPort chunkRepositoryPort,
            ChatModelPort chatModelPort
    ) {
        return new AskQuestionUseCase(embeddingPort, chunkRepositoryPort, chatModelPort);
    }
}
