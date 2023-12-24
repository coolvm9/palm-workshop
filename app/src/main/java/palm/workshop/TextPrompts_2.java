package palm.workshop;

import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.vertexai.VertexAiLanguageModel;

import java.util.HashMap;
import java.util.Map;

public class TextPrompts_2 {
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
                Create a recipe for a {{dish}} with the following ingredients: \
                {{ingredients}}, and give it a name.
                """
        );

        Map<String, Object> variables = new HashMap<>();
        variables.put("dish", "starter");
        variables.put("ingredients", "cinnamon , chicken");

        Prompt prompt = promptTemplate.apply(variables);

        Response<String> response = model.generate(prompt);

        System.out.println(response.content());
    }
}
