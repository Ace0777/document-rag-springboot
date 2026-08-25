package com.luis.documentrag.infrastructure;

import com.luis.documentrag.domain.TextExtractorPort;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UncheckedIOException;

@Component
public class PdfBoxTextExtractorAdapter implements TextExtractorPort {

    @Override
    public String extractText(byte[] bytes, String filename) {
        try (PDDocument document = Loader.loadPDF(bytes)) {
            return new PDFTextStripper().getText(document);
        } catch (IOException e) {
            throw new UncheckedIOException("Falha ao extrair texto do arquivo: " + filename, e);
        }
    }
}
