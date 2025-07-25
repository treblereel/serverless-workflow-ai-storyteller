package org.treblereel.quarkus.llm.agentic.poc;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.junit.jupiter.api.Test;


public class ApplicationTest {


  private final StoryTeller storyTeller = new StoryTeller();

  @Test
  public void test() throws ExecutionException, InterruptedException {

    Map<String, String> map = new HashMap<>();
    map.put("topic", "dragons and wizards");
    map.put("style", "fantasy");
    map.put("audience", "young adults");

    String story = storyTeller.tellStory(map);

    System.out.println("Storytelling completed successfully.");
    System.out.println();
    System.out.println("Final Story: " + story);

  }

}
