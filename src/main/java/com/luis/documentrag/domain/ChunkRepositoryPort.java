package com.luis.documentrag.domain;

import java.util.List;

public interface ChunkRepositoryPort {

    void saveAll(List<Chunk> chunks);

    List<Chunk> findMostSimilar(float[] embedding, int topK);
}
