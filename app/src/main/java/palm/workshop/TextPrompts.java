package palm.workshop;

import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.vertexai.VertexAiLanguageModel;

public class TextPrompts {
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

        Response<String> response = model.generate("What are large language models?");

        System.out.println(response.content());
    }
}
