package org.treblereel.quarkus.llm.agentic.poc;

import java.util.Map;
import java.util.function.Function;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;

import static org.treblereel.quarkus.llm.agentic.poc.Agents.AudienceEditor;


public class AudienceEditorProcessor implements Function<Map<String, String>, Map<String, String>> {

  private final AudienceEditor audienceEditor;

  public AudienceEditorProcessor(ChatModel model) {
    audienceEditor = AiServices.create(AudienceEditor.class, model);
  }

  @Override
  public Map<String, String> apply(Map<String, String> ruleBook) {
    String story = audienceEditor.editStory(ruleBook.get("story"),
            ruleBook.get("audience"));
    ruleBook.put("story", story);
    return ruleBook;
  }
}
