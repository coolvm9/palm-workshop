package palm.workshop;

import dev.langchain4j.chain.ConversationalChain;
import dev.langchain4j.chain.ConversationalRetrievalChain;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.parser.PdfDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.input.PromptTemplate;
import dev.langchain4j.model.vertexai.VertexAiChatModel;
import dev.langchain4j.model.vertexai.VertexAiEmbeddingModel;
import dev.langchain4j.retriever.EmbeddingStoreRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.elasticsearch.ElasticsearchEmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;

import java.io.File;
import java.io.FileInputStream;

public class ChatPrompts_1 {
    public static void main(String[] args) throws Exception {
        VertexAiEmbeddingModel embeddingModel;
        VertexAiChatModel model;
        PdfDocumentParser pdfParser;
        Document document;

        EmbeddingStore<TextSegment> embeddingStore;
        EmbeddingStoreIngestor storeIngestor;

        try {
            // Embedding Model
            embeddingModel = VertexAiEmbeddingModel.builder()
                    .endpoint(Constants.GCP_AI_PLATFORM_URL)
                    .project(Constants.GCP_PROJECT_NAME)
                    .location(Constants.GCP_AI_LOCATION)
                    .publisher(Constants.GCP_MODEL_PUBLISHER)
                    .modelName(Constants.GCP_TEXT_EMBEDDING_MODEL)
                    .maxRetries(3)
                    .build();
            // Chat Model
          model = VertexAiChatModel.builder()
                .endpoint(Constants.GCP_AI_PLATFORM_URL)
                .project(Constants.GCP_PROJECT_NAME)
                .location(Constants.GCP_AI_LOCATION)
                .publisher(Constants.GCP_MODEL_PUBLISHER)
                .modelName(Constants.GCP_CHAT_MODEL)
                .maxOutputTokens(400)
                .maxRetries(3)
                .build();

            // Step 1
            pdfParser = new PdfDocumentParser();
            document = pdfParser.parse(
                    new FileInputStream(new File("/Users/satyaanumolu/POCs/palm-workshop/app/src/main/resources/attention-is-all-you-need.pdf")));
//            document = pdfParser.parse(
//                    new FileInputStream(new File("/Users/satyaanumolu/POCs/palm-workshop/app/src/main/resources/test.pdf")));

            // Add Metadata
            document.metadata().add("lob","lob1");
            document.metadata().add("fileName","attention-is-all-you-need");



            // Step 2 : Chunk and Embed into Vector DB
            // In Memory Store Option 1
//        InMemoryEmbeddingStore<TextSegment> embeddingStore =
//                new InMemoryEmbeddingStore<>();

            // Elastic Search Option 2

            embeddingStore = ElasticsearchEmbeddingStore.builder()
                    .serverUrl(Constants.LOCAL_ELASTIC_INSTANCE_URL)
                    .indexName(Constants.LOCAL_ELASTIC_INDEX)
                    .dimension(768)
                    .build();

//            // Step 3 Ingest into VectorDB
//            storeIngestor = EmbeddingStoreIngestor.builder()
//                    .documentSplitter(DocumentSplitters.recursive(500, 0))
//                    .embeddingModel(embeddingModel)
//                    .embeddingStore(embeddingStore)
//                    .build();
//
//            storeIngestor.ingest(document);




        // Step 4 : Retriever Chain
        EmbeddingStoreRetriever retriever = EmbeddingStoreRetriever.from(embeddingStore, embeddingModel);

        // Step 5 : Conversation Retrieval Chain
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

        String question = "What neural network architecture can be used for language models?";
        String result = rag.execute(question);

        System.out.println("Question : " + question);

        System.out.println("Result  : ---------------");

        System.out.println(result);
        System.out.println("------------");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }finally {

        }
        System.exit(-1);
    }
}
