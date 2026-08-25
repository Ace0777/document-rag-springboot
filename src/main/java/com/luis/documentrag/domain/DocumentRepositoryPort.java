package com.luis.documentrag.domain;

import java.util.Optional;
import java.util.UUID;

public interface DocumentRepositoryPort {

    Document save(Document document);

    Optional<Document> findById(UUID id);
}
