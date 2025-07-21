package org.treblereel.quarkus.llm.agentic.poc;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import io.serverlessworkflow.api.types.CallJava;
import io.serverlessworkflow.api.types.CallTaskJava;
import io.serverlessworkflow.api.types.Document;
import io.serverlessworkflow.api.types.Task;
import io.serverlessworkflow.api.types.TaskItem;
import io.serverlessworkflow.api.types.Workflow;
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
            new Workflow()
                    .withDocument(
                            new Document().withNamespace("test").withName("testMap").withVersion("1.0"))
                    .withDo(List.of(new TaskItem("creative_story_task",
                                            new Task().withCallTask(
                                                    new CallTaskJava(
                                                            new CallJava.CallJavaFunction<>(creativeWriterProcessor)
                                                    )
                                            )
                                    ),
                                    new TaskItem("audience_story_task",
                                            new Task().withCallTask(
                                                    new CallTaskJava(
                                                            new CallJava.CallJavaFunction<>(audienceEditorProcessor)
                                                    )
                                            )
                                    ),
                                    new TaskItem("style_story_task",
                                            new Task().withCallTask(
                                                    new CallTaskJava(
                                                            new CallJava.CallJavaFunction<>(styleEditorProcessor)
                                                    )
                                            )
                                    )
                            )
                    );
  }

  public void tellStory(Map<String, String> topic) throws ExecutionException, InterruptedException {
    try (WorkflowApplication app = WorkflowApplication.builder().build()) {
      app.workflowDefinition(workflow)
              .instance(topic)
              .start()
              .get()
              .asMap()
              .orElseThrow();
    }
  }

}
