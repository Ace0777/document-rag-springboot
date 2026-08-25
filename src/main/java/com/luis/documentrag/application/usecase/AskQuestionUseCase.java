package com.luis.documentrag.application.usecase;

import com.luis.documentrag.domain.Answer;
import com.luis.documentrag.domain.ChatModelPort;
import com.luis.documentrag.domain.Chunk;
import com.luis.documentrag.domain.ChunkRepositoryPort;
import com.luis.documentrag.domain.EmbeddingPort;

import java.util.List;

public class AskQuestionUseCase {

    private static final int TOP_K = 5;

    private final EmbeddingPort embeddingPort;
    private final ChunkRepositoryPort chunkRepositoryPort;
    private final ChatModelPort chatModelPort;

    public AskQuestionUseCase(
            EmbeddingPort embeddingPort,
            ChunkRepositoryPort chunkRepositoryPort,
            ChatModelPort chatModelPort
    ) {
        this.embeddingPort = embeddingPort;
        this.chunkRepositoryPort = chunkRepositoryPort;
        this.chatModelPort = chatModelPort;
    }

    public Answer ask(String question) {
        float[] questionEmbedding = embeddingPort.embed(question);
        List<Chunk> contextChunks = chunkRepositoryPort.findMostSimilar(questionEmbedding, TOP_K);
        String text = chatModelPort.generateAnswer(question, contextChunks);
        return new Answer(text, contextChunks);
    }
}
