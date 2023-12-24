package palm.workshop;

import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.input.structured.StructuredPrompt;
import dev.langchain4j.model.input.structured.StructuredPromptProcessor;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.vertexai.VertexAiLanguageModel;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TextPrompts_3 {

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
        RecipeCreationPrompt createRecipePrompt = new RecipeCreationPrompt();
        createRecipePrompt.dish = "salad";
        createRecipePrompt.ingredients = Arrays.asList("cucumber", "tomato", "feta", "onion", "olives");
        Prompt prompt = StructuredPromptProcessor.toPrompt(createRecipePrompt);

        Response<String> response = model.generate(prompt);

        System.out.println(response.content());
    }
}
