package palm.workshop;

import dev.langchain4j.chain.ConversationalRetrievalChain;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.Metadata;
import dev.langchain4j.data.document.parser.PdfDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentByParagraphSplitter;
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
import java.util.List;

public class DocSplitter {
    private static String PROJECT_ID = "neat-vent-381323";
    private static String AI_PLATFORM_MODEL_END_POINT = "us-central1-aiplatform.googleapis.com:443";
    private static String MODEL_LOCATION = "us-central1";
    private static String MODEL_PUBLISHER = "google";

    // Embedding Model , Chat Model
    private static String EMB_MODEL_NAME = "textembedding-gecko@001";
    private static String CHAT_MODE_NAME ="chat-bison@001";




    public static void main(String[] args) throws IOException {
        int maxSegmentSize = 100;
        int maxOverlap = 10;

        // Load the document
        PdfDocumentParser pdfParser = new PdfDocumentParser();
        Document document = pdfParser.parse(new FileInputStream(new File("/Users/satyaanumolu/POCs/palm-workshop/app/src/main/resources/attention-is-all-you-need.pdf")));
        DocumentSplitter splitter = new DocumentByParagraphSplitter(maxSegmentSize, maxOverlap);
        List<TextSegment> segments = splitter.split(document);

        for(TextSegment segment : segments){
            System.out.println(segment.text());
        }


    }

}
