package palm.workshop;

import dev.langchain4j.model.output.Response;
import dev.langchain4j.model.vertexai.VertexAiLanguageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpCall {
    private static Logger logger = LoggerFactory.getLogger(HttpCall.class);
    private static String GCP_AI_END_POINT = "us-central1-aiplatform.googleapis.com:443";
    private static String GCP_PROJECT_ID = "neat-vent-381323";
    private static String GCP_REGION="us-central1";
    private static String GCP_MODEL_NAME = "text-bison@001";
    private static String MODEL_PUBLISHER = "google";


    public static void main(String[] args) {
        try {
            HttpCall call = new HttpCall();
            String promptText = "Give me Java code to call Vertex AI with Langchain4j? ";
            String response = call.getResponse(GCP_AI_END_POINT, GCP_PROJECT_ID, GCP_REGION, MODEL_PUBLISHER,GCP_MODEL_NAME, 3, promptText);
            System.out.println("Question :" + promptText + "\nANSWER " + response);

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private String getResponse(String endPoint, String gcpProjectId, String gcpRegion, String publisher, String modelName, int retries, String promptText) throws Exception {
        String result ="";
        try {
            String prompt = String.format("{\"prompt\":%s}", promptText);
            VertexAiLanguageModel vertexAiLanguageModel = VertexAiLanguageModel.builder()
                    .endpoint(endPoint)
                    .project(gcpProjectId)
                    .location(gcpRegion)
                    .publisher(publisher)
                    .modelName(modelName)
                    .temperature(1.0)
                    .maxOutputTokens(50)
                    .topK(0)
                    .topP(0.0)
                    .maxRetries(retries)
                    .build();

            Response<String> modelResponse = vertexAiLanguageModel.generate(prompt);
            result = modelResponse.content();
//            logger.info("Result: " + modelResponse.content());
        }catch (Exception e){
            throw e;
        }
        return  result;
    }
}
