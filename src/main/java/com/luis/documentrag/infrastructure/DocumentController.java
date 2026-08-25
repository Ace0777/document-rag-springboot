package com.luis.documentrag.infrastructure;

import com.luis.documentrag.application.usecase.IngestDocumentUseCase;
import com.luis.documentrag.domain.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;

@RestController
public class DocumentController {

    private final IngestDocumentUseCase ingestDocumentUseCase;

    public DocumentController(IngestDocumentUseCase ingestDocumentUseCase) {
        this.ingestDocumentUseCase = ingestDocumentUseCase;
    }

    @PostMapping(value = "/api/documents", consumes = "multipart/form-data")
    public ResponseEntity<Document> ingest(@RequestParam("file") MultipartFile file) {
        try {
            Document document = ingestDocumentUseCase.ingest(
                    file.getBytes(), file.getOriginalFilename(), file.getContentType());
            return ResponseEntity.ok(document);
        } catch (IOException e) {
            throw new UncheckedIOException("Falha ao ler o arquivo enviado", e);
        }
    }
}
