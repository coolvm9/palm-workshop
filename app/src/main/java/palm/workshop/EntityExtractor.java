package palm.workshop;

import dev.langchain4j.model.vertexai.VertexAiChatModel;
import dev.langchain4j.service.AiServices;
import dev.langchain4j.service.UserMessage;

public class EntityExtractor {

    static class Person {
        String name;
        int age;
    }

    interface PersonExtractor {
//        @UserMessage("""
//            Extract the name and age of the person described below.
//            Return a JSON document with a "name" and an "age" property, \
//            following this structure: {"name": "John Doe", "age": 34}
//            Return only JSON, without any markdown markup surrounding it.
//            Here is the document describing the person:
//            ---
//            {{it}}
//            ---
//            JSON:
//            """)
        Person extractPerson(String text);
    }

    public static void main(String[] args) {
        VertexAiChatModel model = VertexAiChatModel.builder()
                .endpoint("us-central1-aiplatform.googleapis.com:443")
                .project("neat-vent-381323")
                .location("us-central1")
                .publisher("google")
                .modelName("chat-bison@001")
                .maxOutputTokens(300)
                .build();

        PersonExtractor extractor = AiServices.create(PersonExtractor.class, model);

        Person person = extractor.extractPerson("""
            Anna is a 23 year old artist based in Brooklyn, New York. She was born and 
            raised in the suburbs of Chicago, where she developed a love for art at a 
            young age. She attended the School of the Art Institute of Chicago, where 
            she studied painting and drawing. After graduating, she moved to New York 
            City to pursue her art career. Anna's work is inspired by her personal 
            experiences and observations of the world around her. She often uses bright 
            colors and bold lines to create vibrant and energetic paintings. Her work 
            has been exhibited in galleries and museums in New York City and Chicago.    
            """
        );

        System.out.println(person.name);
        System.out.println(person.age);
    }
}
