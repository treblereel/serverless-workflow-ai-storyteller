package org.treblereel.quarkus.llm.agentic.poc;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


@QuarkusTest
public class ApplicationTest {


  @Inject
  StoryTeller storyTeller;

  @Test
  public void test2() {
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
