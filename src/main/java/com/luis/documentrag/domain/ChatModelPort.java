package com.luis.documentrag.domain;

import java.util.List;

public interface ChatModelPort {

    String generateAnswer(String question, List<Chunk> contextChunks);
}
