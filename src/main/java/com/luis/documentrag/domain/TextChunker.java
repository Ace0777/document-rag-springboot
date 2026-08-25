package com.luis.documentrag.domain;

import java.util.ArrayList;
import java.util.List;

public class TextChunker {

    private static final int DEFAULT_CHUNK_SIZE = 1000;
    private static final int DEFAULT_OVERLAP = 200;

    public List<String> chunk(String text) {
        return chunk(text, DEFAULT_CHUNK_SIZE, DEFAULT_OVERLAP);
    }

    public List<String> chunk(String text, int chunkSize, int overlap) {
        if (chunkSize <= 0) {
            throw new IllegalArgumentException("chunkSize deve ser maior que zero");
        }
        if (overlap < 0 || overlap >= chunkSize) {
            throw new IllegalArgumentException("overlap deve ser >= 0 e menor que chunkSize");
        }
        if (text == null || text.isBlank()) {
            return List.of();
        }

        String trimmed = text.strip();
        List<String> chunks = new ArrayList<>();
        int step = chunkSize - overlap;

        for (int start = 0; start < trimmed.length(); start += step) {
            int end = Math.min(start + chunkSize, trimmed.length());
            String piece = trimmed.substring(start, end).strip();
            if (!piece.isEmpty()) {
                chunks.add(piece);
            }
            if (end == trimmed.length()) {
                break;
            }
        }

        return chunks;
    }
}
