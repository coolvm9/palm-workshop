package palm.workshop;

import dev.langchain4j.chain.ConversationalChain;
import dev.langchain4j.model.vertexai.VertexAiChatModel;

public class ChatPrompts {
    public static void main(String[] args) {
        VertexAiChatModel model = VertexAiChatModel.builder()
                .endpoint(Constants.GCP_AI_PLATFORM_URL)
                .project(Constants.GCP_PROJECT_NAME)
                .location(Constants.GCP_AI_LOCATION)
                .publisher(Constants.GCP_MODEL_PUBLISHER)
                .modelName(Constants.GCP_CHAT_MODEL)
                .maxOutputTokens(400)
                .maxRetries(3)
                .build();

        ConversationalChain chain = ConversationalChain.builder()
                .chatLanguageModel(model)
                .build();

        String message = "What are large language models?";
        String answer = chain.execute(message);
        System.out.println(answer);

        System.out.println("---------------------------");

        message = "What can you do with them?";
        answer = chain.execute(message);
        System.out.println(answer);

        System.out.println("---------------------------");

        message = "Can you name some of them?";
        answer = chain.execute(message);
        System.out.println(answer);
    }
}
