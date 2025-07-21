package org.treblereel.quarkus.llm.agentic.poc;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class ApplicationTest {


  private final StoryTeller storyTeller = new StoryTeller();

  @Test
  public void test() {
    Map<String, String> map = new HashMap<>();
    map.put("topic", "dragons and wizards");
    map.put("style", "fantasy");
    map.put("audience", "young adults");

    try {
      storyTeller.tellStory(map);
    } catch (ExecutionException | InterruptedException e) {
      throw new RuntimeException("Error during storytelling", e);
    }

    System.out.println("Storytelling completed successfully.");
    System.out.println();
    System.out.println();
    System.out.println("Final Story: " + map.get("story"));
  }
}
