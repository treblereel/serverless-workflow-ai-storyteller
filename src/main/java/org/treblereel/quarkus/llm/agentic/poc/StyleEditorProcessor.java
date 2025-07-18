package org.treblereel.quarkus.llm.agentic.poc;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Map;
import java.util.function.Function;

import static org.treblereel.quarkus.llm.agentic.poc.Agents.*;

@ApplicationScoped
public class StyleEditorProcessor implements Function<Map<String, String>, Map<String, String>> {

  @Inject
  StyleEditor styleEditor;

  public Map<String, String> apply(Map<String, String> ruleBook) {
    String story = styleEditor.editStory(ruleBook.get("story"),
            ruleBook.get("style"));
    ruleBook.put("story", story);
    return ruleBook;
  }
}
