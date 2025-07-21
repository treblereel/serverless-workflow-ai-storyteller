package org.treblereel.quarkus.llm.agentic.poc;

import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.V;


public class Agents {

    public interface CreativeWriter {

        @UserMessage("""
                You are a creative writer.
                Generate a draft of a story long no more than 3 sentence around the given topic.
                Return only the story and nothing else.
                The topic is {{topic}}.
                """)
        String generateStory(@V("topic") String topic);
    }


    public interface AudienceEditor {

        @UserMessage("""
            You are a professional editor.
            Analyze and rewrite the following story to better align with the target audience of {{audience}}.
            Return only the story and nothing else.
            The story is "{{story}}".
            """)
        String editStory(@V("story") String story, @V("audience") String audience);
    }


    public interface StyleEditor {

        @UserMessage("""
                You are a professional editor.
                Analyze and rewrite the following story to better fit and be more coherent with the {{style}} style.
                Return only the story and nothing else.
                The story is "{{story}}".
                """)
        String editStory(@V("story") String story, @V("style") String style);
    }

}
