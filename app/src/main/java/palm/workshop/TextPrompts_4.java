package palm.workshop;

import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.input.structured.StructuredPrompt;
import dev.langchain4j.model.input.structured.StructuredPromptProcessor;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.vertexai.VertexAiLanguageModel;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TextPrompts_4 {

    @StructuredPrompt("Create a recipe of a {{dish}} that can be prepared using only {{ingredients}}")
    static class RecipeCreationPrompt {
        String dish;
        List<String> ingredients;
    }
    public static void main(String[] args) {
        VertexAiLanguageModel model = VertexAiLanguageModel.builder()
                .endpoint(Constants.GCP_AI_PLATFORM_URL)
                .project(Constants.GCP_PROJECT_NAME)
                .location(Constants.GCP_AI_LOCATION)
                .publisher(Constants.GCP_MODEL_PUBLISHER)
                .modelName(Constants.GCP_TEXT_MODEL)
                .maxOutputTokens(500)
                .temperature(0.2)
                .build();
        PromptTemplate promptTemplate = PromptTemplate.from("""
            Analyze the sentiment of the text below. Respond only with one word to describe the sentiment.

            INPUT: This is fantastic news!
            OUTPUT: POSITIVE

            INPUT: Pi is roughly equal to 3.14
            OUTPUT: NEUTRAL

            INPUT: I really disliked the pizza. Who would use pineapples as a pizza topping?
            OUTPUT: NEGATIVE

            INPUT: {{text}}
            OUTPUT: 
            """);

        Prompt prompt = promptTemplate.apply(
                Map.of("text", "I love strawberries!"));

        Response<String> response = model.generate(prompt);

        System.out.println(response.content());
    }
}
