package com.luis.documentrag.infrastructure;

import com.luis.documentrag.domain.ChatModelPort;
import com.luis.documentrag.domain.Chunk;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class OllamaChatAdapter implements ChatModelPort {

    private static final String SYSTEM_PROMPT = """
            Você é um assistente que responde perguntas com base apenas no contexto fornecido.
            Se a resposta não estiver no contexto, diga que não sabe. Não invente informações.
            """;

    private final RestClient restClient;
    private final String model;

    public OllamaChatAdapter(
            RestClient.Builder restClientBuilder,
            @Value("${rag.chat.api-url}") String apiUrl,
            @Value("${rag.chat.model}") String model
    ) {
        this.restClient = restClientBuilder
                .baseUrl(apiUrl)
                .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                .build();
        this.model = model;
    }

    @Override
    public String generateAnswer(String question, List<Chunk> contextChunks) {
        String context = IntStream.range(0, contextChunks.size())
                .mapToObj(i -> "[" + (i + 1) + "] " + contextChunks.get(i).content())
                .collect(Collectors.joining("\n\n"));

        String userMessage = """
                Contexto:
                %s

                Pergunta: %s

                Responda só com base no contexto acima.
                """.formatted(context, question);

        ChatResponse response = restClient.post()
                .body(new ChatRequest(
                        model,
                        List.of(new Message("system", SYSTEM_PROMPT), new Message("user", userMessage)),
                        false
                ))
                .retrieve()
                .body(ChatResponse.class);

        if (response == null) {
            throw new IllegalStateException("Resposta vazia do Ollama");
        }

        return response.message().content();
    }

    private record ChatRequest(String model, List<Message> messages, boolean stream) {
    }

    private record Message(String role, String content) {
    }

    private record ChatResponse(Message message) {
    }
}
