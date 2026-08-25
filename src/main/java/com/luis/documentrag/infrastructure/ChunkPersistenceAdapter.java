package com.luis.documentrag.infrastructure;

import com.luis.documentrag.domain.Chunk;
import com.luis.documentrag.domain.ChunkRepositoryPort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class ChunkPersistenceAdapter implements ChunkRepositoryPort {

    private final JdbcTemplate jdbcTemplate;

    public ChunkPersistenceAdapter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void saveAll(List<Chunk> chunks) {
        if (chunks.isEmpty()) {
            return;
        }

        String sql = """
                INSERT INTO chunks (id, document_id, content, chunk_index, embedding)
                VALUES (?, ?, ?, ?, ?::vector)
                """;

        jdbcTemplate.batchUpdate(sql, chunks, chunks.size(), (ps, chunk) -> {
            ps.setObject(1, chunk.id());
            ps.setObject(2, chunk.documentId());
            ps.setString(3, chunk.content());
            ps.setInt(4, chunk.chunkIndex());
            ps.setString(5, toVectorLiteral(chunk.embedding()));
        });
    }

    @Override
    public List<Chunk> findMostSimilar(float[] embedding, int topK) {
        String sql = """
                SELECT id, document_id, content, chunk_index, embedding
                FROM chunks
                ORDER BY embedding <=> ?::vector
                LIMIT ?
                """;

        return jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new Chunk(
                        UUID.fromString(rs.getString("id")),
                        UUID.fromString(rs.getString("document_id")),
                        rs.getString("content"),
                        rs.getInt("chunk_index"),
                        parseVectorLiteral(rs.getString("embedding"))
                ),
                toVectorLiteral(embedding), topK
        );
    }

    private String toVectorLiteral(float[] embedding) {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < embedding.length; i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(embedding[i]);
        }
        return sb.append(']').toString();
    }

    private float[] parseVectorLiteral(String literal) {
        String trimmed = literal.substring(1, literal.length() - 1);
        if (trimmed.isBlank()) {
            return new float[0];
        }
        String[] parts = trimmed.split(",");
        float[] values = new float[parts.length];
        for (int i = 0; i < parts.length; i++) {
            values[i] = Float.parseFloat(parts[i]);
        }
        return values;
    }
}
