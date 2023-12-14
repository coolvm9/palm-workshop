package palm.workshop;

import dev.langchain4j.chain.ConversationalRetrievalChain;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.PdfDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.vertexai.VertexAiChatModel;
import dev.langchain4j.model.vertexai.VertexAiEmbeddingModel;
import dev.langchain4j.retriever.EmbeddingStoreRetriever;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class RAGqa {
    private static String PROJECT_ID = "neat-vent-381323";
    private static String AI_PLATFORM_MODEL_END_POINT = "us-central1-aiplatform.googleapis.com:443";
    private static String MODEL_LOCATION = "us-central1";
    private static String MODEL_PUBLISHER = "google";

    // Embedding Model , Chat Model
    private static String EMB_MODEL_NAME = "textembedding-gecko@001";
    private static String CHAT_MODE_NAME ="chat-bison@001";




    public static void main(String[] args) throws IOException {

        // Load the document
        PdfDocumentParser pdfParser = new PdfDocumentParser();
        Document document = pdfParser.parse(new FileInputStream(new File("/Users/satyaanumolu/POCs/palm-workshop/attention-is-all-you-need.pdf")));
        // Initialize the model
        VertexAiEmbeddingModel embeddingModel = VertexAiEmbeddingModel.builder()
                .endpoint(AI_PLATFORM_MODEL_END_POINT)
                .project(PROJECT_ID)
                .location(MODEL_LOCATION)
                .publisher(MODEL_PUBLISHER)
                .modelName(EMB_MODEL_NAME)
                .maxRetries(3)
                .build();
        // Vector DB (in Memory) // Change to VectorDB
        InMemoryEmbeddingStore<TextSegment> embeddingStore =
                new InMemoryEmbeddingStore<>();
        // Store Chunks
        // How to pass additional information on to the index
        EmbeddingStoreIngestor storeIngestor = EmbeddingStoreIngestor.builder()
                .documentSplitter(DocumentSplitters.recursive(500, 100))
                .embeddingModel(embeddingModel)
                .embeddingStore(embeddingStore)
                .build();
        // Storing Document
        storeIngestor.ingest(document);


        // Retriever reference to embedding store and the model
        EmbeddingStoreRetriever retriever = EmbeddingStoreRetriever.from(embeddingStore, embeddingModel);


        // Retrieval for Answers
        VertexAiChatModel model = VertexAiChatModel.builder()
                .endpoint(AI_PLATFORM_MODEL_END_POINT)
                .project(PROJECT_ID)
                .location(MODEL_LOCATION)
                .publisher(MODEL_PUBLISHER)
                .modelName(CHAT_MODE_NAME)
                .maxOutputTokens(1000)
                .build();

        ConversationalRetrievalChain rag = ConversationalRetrievalChain.builder()
                .chatLanguageModel(model)
                .retriever(retriever)
                .promptTemplate(PromptTemplate.from("""
                Answer to the following query the best as you can: {{question}}
                Base your answer on the information provided below:
                {{information}}
                """
                ))
                .build();

        String question  = "What neural network architecture can be used for language models?";
        System.out.println("Question ------------ " + question);
        String result = rag.execute(question);
        System.out.println(result);
        System.out.println("------------");

        question  = "What are the different components of a transformer neural network?";
        System.out.println("Question ------------ " + question);
        result = rag.execute(question);
        System.out.println(result);
        System.out.println("------------");

        question  = "What is attention in large language models?";
        System.out.println("Question ------------ " + question);
        result = rag.execute(question);
        System.out.println(result);
        System.out.println("------------");

        question  = "What is the name of the process that transforms text into vectors?";
        System.out.println("Question ------------ " + question);
        result = rag.execute(question);
        System.out.println(result);
    }

}
