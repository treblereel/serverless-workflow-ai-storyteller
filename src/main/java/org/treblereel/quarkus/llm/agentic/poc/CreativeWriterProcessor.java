package org.treblereel.quarkus.llm.agentic.poc;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Map;
import java.util.function.Function;

@ApplicationScoped
public class CreativeWriterProcessor implements Function<Map<String, String>, Map<String, String>> {

  @Inject
  Agents.CreativeWriter creativeWriter;

  public Map<String, String> apply(Map<String, String> ruleBook) {
    String story = creativeWriter.generateStory(ruleBook.get("topic"));
    ruleBook.put("story", story);
    return ruleBook;
  }
}
