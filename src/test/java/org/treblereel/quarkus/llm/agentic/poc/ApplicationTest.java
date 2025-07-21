package org.treblereel.quarkus.llm.agentic.poc;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ApplicationTest {


  private final StoryTeller storyTeller = new StoryTeller();

  @Test
  public void test() {
    Map<String, String> map = new HashMap<>();
    map.put("topic", "dragons and wizards");
    map.put("style", "fantasy");
    map.put("audience", "young adults");

    Map<String, Object> result;
    try {
      result = storyTeller.tellStory(map);
    } catch (ExecutionException | InterruptedException e) {
      throw new RuntimeException("Error during storytelling", e);
    }

    assertNotNull(result.get("story"), "Result should not be null");

    System.out.println("Storytelling completed successfully.");
    System.out.println();
    System.out.println();
    System.out.println("Final Story: " + result.get("story"));
  }

}
