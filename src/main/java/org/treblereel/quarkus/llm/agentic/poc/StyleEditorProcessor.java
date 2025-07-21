package org.treblereel.quarkus.llm.agentic.poc;


import java.util.Map;
import java.util.function.Function;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;

import static org.treblereel.quarkus.llm.agentic.poc.Agents.StyleEditor;

public class StyleEditorProcessor implements Function<Map<String, String>, Map<String, String>> {

  private final StyleEditor styleEditor;

  public StyleEditorProcessor(ChatModel model) {
    styleEditor = AiServices.create(StyleEditor.class, model);
  }

  public Map<String, String> apply(Map<String, String> ruleBook) {
    String story = styleEditor.editStory(ruleBook.get("story"),
            ruleBook.get("style"));
    ruleBook.put("story", story);
    return ruleBook;
  }
}
