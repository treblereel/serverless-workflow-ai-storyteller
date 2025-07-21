package org.treblereel.quarkus.llm.agentic.poc;

import java.util.Map;
import java.util.function.Function;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;

import static org.treblereel.quarkus.llm.agentic.poc.Agents.CreativeWriter;

public class CreativeWriterProcessor implements Function<Map<String, String>, Map<String, String>> {

  private final CreativeWriter creativeWriter;

  public CreativeWriterProcessor(ChatModel model) {
    creativeWriter = AiServices.create(CreativeWriter.class, model);
  }

  public Map<String, String> apply(Map<String, String> ruleBook) {
    String story = creativeWriter.generateStory(ruleBook.get("topic"));
    ruleBook.put("story", story);
    return ruleBook;
  }
}
