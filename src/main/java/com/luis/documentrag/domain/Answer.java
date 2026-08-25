package com.luis.documentrag.domain;

import java.util.List;

public record Answer(
        String text,
        List<Chunk> sources
) {
}
