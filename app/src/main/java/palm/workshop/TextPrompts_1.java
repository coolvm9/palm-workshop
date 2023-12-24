package palm.workshop;

import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.vertexai.VertexAiLanguageModel;

public class TextPrompts_1 {
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
        Response<String> response = model.generate("""
                What is  the name and age of the person described below.
                Give me an answer in JSON format
              
                Here is the document describing the person:
                ---
                Anna is a 23 year old artist based in Brooklyn, New York. She was born and 
                raised in the suburbs of Chicago, where she developed a love for art at a 
                young age. She attended the School of the Art Institute of Chicago, where 
                she studied painting and drawing. After graduating, she moved to New York 
                City to pursue her art career. Anna's work is inspired by her personal 
                experiences and observations of the world around her. She often uses bright 
                colors and bold lines to create vibrant and energetic paintings. Her work 
                has been exhibited in galleries and museums in New York City and Chicago.  
                ---
                JSON: 
                """
        );

        System.out.println(response.content());
    }
}
