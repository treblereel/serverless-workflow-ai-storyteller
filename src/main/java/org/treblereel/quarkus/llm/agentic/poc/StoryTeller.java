package org.treblereel.quarkus.llm.agentic.poc;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import io.serverlessworkflow.api.types.Workflow;
import io.serverlessworkflow.fluent.java.JavaWorkflowBuilder;
import io.serverlessworkflow.impl.WorkflowApplication;

public class StoryTeller {

  private final ChatModel BASE_MODEL = OpenAiChatModel.builder()
          .apiKey(System.getenv("OPENAI_API_KEY"))
          .modelName("gpt-4o")
          .timeout(Duration.ofMinutes(10))
          .temperature(0.0)
          .logRequests(true)
          .logResponses(true)
          .build();

  private final CreativeWriterProcessor creativeWriterProcessor = new CreativeWriterProcessor(BASE_MODEL);

  private final AudienceEditorProcessor audienceEditorProcessor = new AudienceEditorProcessor(BASE_MODEL);

  private final StyleEditorProcessor styleEditorProcessor = new StyleEditorProcessor(BASE_MODEL);

  private final Workflow workflow;

  public StoryTeller() {
    workflow =
            JavaWorkflowBuilder.
                    workflow("testJavaCall")
                    .doTasks(tasks ->
                            tasks.callFn(callJava -> callJava.function(creativeWriterProcessor))
                                    .callFn(callJava -> callJava.function(audienceEditorProcessor))
                                    .callFn(callJava -> callJava.function(styleEditorProcessor)))
                    .build();
  }

  public Map<String, Object> tellStory(Map<String, String> topic) throws ExecutionException, InterruptedException {
    try (WorkflowApplication app = WorkflowApplication.builder().build()) {
      return app.workflowDefinition(workflow)
              .instance(topic)
              .start()
              .get()
              .asMap()
              .orElseThrow();
    }
  }

}
