package org.treblereel.quarkus.llm.agentic.poc;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Map;
import java.util.function.Function;

import static org.treblereel.quarkus.llm.agentic.poc.Agents.*;


@ApplicationScoped
public class AudienceEditorProcessor implements Function<Map<String, String>, Map<String, String>> {

  @Inject
  AudienceEditor audienceEditor;

  @Override
  public Map<String, String> apply(Map<String, String> ruleBook) {
    String story = audienceEditor.editStory(ruleBook.get("story"),
            ruleBook.get("audience"));
    ruleBook.put("story", story);
    return ruleBook;
  }
}
