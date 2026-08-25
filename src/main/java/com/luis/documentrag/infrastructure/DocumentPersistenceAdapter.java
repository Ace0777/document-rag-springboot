package com.luis.documentrag.infrastructure;

import com.luis.documentrag.domain.Document;
import com.luis.documentrag.domain.DocumentRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class DocumentPersistenceAdapter implements DocumentRepositoryPort {

    private final DocumentJpaRepository jpaRepository;

    public DocumentPersistenceAdapter(DocumentJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Document save(Document document) {
        DocumentEntity entity = new DocumentEntity(
                document.id(),
                document.filename(),
                document.contentType(),
                document.uploadedAt()
        );
        DocumentEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Document> findById(UUID id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    private Document toDomain(DocumentEntity entity) {
        return new Document(entity.getId(), entity.getFilename(), entity.getContentType(), entity.getUploadedAt());
    }
}
