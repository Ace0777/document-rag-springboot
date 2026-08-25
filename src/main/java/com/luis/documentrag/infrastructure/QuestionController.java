package com.luis.documentrag.infrastructure;

import com.luis.documentrag.application.usecase.AskQuestionUseCase;
import com.luis.documentrag.domain.Answer;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionController {

    private final AskQuestionUseCase askQuestionUseCase;

    public QuestionController(AskQuestionUseCase askQuestionUseCase) {
        this.askQuestionUseCase = askQuestionUseCase;
    }

    @PostMapping("/api/questions")
    public Answer ask(@Valid @RequestBody QuestionRequest request) {
        return askQuestionUseCase.ask(request.question());
    }

    public record QuestionRequest(@NotBlank String question) {
    }
}
