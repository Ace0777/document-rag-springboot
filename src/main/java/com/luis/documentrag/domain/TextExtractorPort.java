package com.luis.documentrag.domain;

public interface TextExtractorPort {

    String extractText(byte[] bytes, String filename);
}
