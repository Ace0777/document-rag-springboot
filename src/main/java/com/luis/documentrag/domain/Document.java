package com.luis.documentrag.domain;

import java.time.Instant;
import java.util.UUID;

public record Document(
        UUID id,
        String filename,
        String contentType,
        Instant uploadedAt
) {
}
