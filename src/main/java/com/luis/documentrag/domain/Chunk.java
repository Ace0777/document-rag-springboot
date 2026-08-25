package com.luis.documentrag.domain;

import java.util.UUID;

public record Chunk(
        UUID id,
        UUID documentId,
        String content,
        int chunkIndex,
        float[] embedding
) {
}
