package com.luis.documentrag.application.usecase;

import com.luis.documentrag.domain.Chunk;
import com.luis.documentrag.domain.ChunkRepositoryPort;
import com.luis.documentrag.domain.Document;
import com.luis.documentrag.domain.DocumentRepositoryPort;
import com.luis.documentrag.domain.EmbeddingPort;
import com.luis.documentrag.domain.TextChunker;
import com.luis.documentrag.domain.TextExtractorPort;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class IngestDocumentUseCase {

    private final TextExtractorPort textExtractorPort;
    private final TextChunker textChunker;
    private final EmbeddingPort embeddingPort;
    private final DocumentRepositoryPort documentRepositoryPort;
    private final ChunkRepositoryPort chunkRepositoryPort;

    public IngestDocumentUseCase(
            TextExtractorPort textExtractorPort,
            TextChunker textChunker,
            EmbeddingPort embeddingPort,
            DocumentRepositoryPort documentRepositoryPort,
            ChunkRepositoryPort chunkRepositoryPort
    ) {
        this.textExtractorPort = textExtractorPort;
        this.textChunker = textChunker;
        this.embeddingPort = embeddingPort;
        this.documentRepositoryPort = documentRepositoryPort;
        this.chunkRepositoryPort = chunkRepositoryPort;
    }

    public Document ingest(byte[] fileBytes, String filename, String contentType) {
        Document document = new Document(UUID.randomUUID(), filename, contentType, Instant.now());
        documentRepositoryPort.save(document);

        String text = textExtractorPort.extractText(fileBytes, filename);
        List<String> pieces = textChunker.chunk(text);
        List<float[]> embeddings = embeddingPort.embedAll(pieces);

        List<Chunk> chunks = new java.util.ArrayList<>();
        for (int i = 0; i < pieces.size(); i++) {
            chunks.add(new Chunk(UUID.randomUUID(), document.id(), pieces.get(i), i, embeddings.get(i)));
        }
        chunkRepositoryPort.saveAll(chunks);

        return document;
    }
}
