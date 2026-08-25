package com.luis.documentrag.infrastructure;

import com.luis.documentrag.domain.EmbeddingPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class OllamaEmbeddingAdapter implements EmbeddingPort {

    private final RestClient restClient;
    private final String model;

    public OllamaEmbeddingAdapter(
            RestClient.Builder restClientBuilder,
            @Value("${rag.embedding.api-url}") String apiUrl,
            @Value("${rag.embedding.model}") String model
    ) {
        this.restClient = restClientBuilder
                .baseUrl(apiUrl)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.model = model;
    }

    @Override
    public float[] embed(String text) {
        return embedAll(List.of(text)).get(0);
    }

    @Override
    public List<float[]> embedAll(List<String> texts) {
        if (texts.isEmpty()) {
            return List.of();
        }

        EmbedResponse response = restClient.post()
                .body(new EmbedRequest(model, texts))
                .retrieve()
                .body(EmbedResponse.class);

        if (response == null) {
            throw new IllegalStateException("Resposta vazia do Ollama");
        }

        return response.embeddings();
    }

    private record EmbedRequest(String model, List<String> input) {
    }

    private record EmbedResponse(List<float[]> embeddings) {
    }
}
