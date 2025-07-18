package org.treblereel.quarkus.llm.agentic.poc;

import io.serverlessworkflow.api.types.CallJava;
import io.serverlessworkflow.api.types.CallTaskJava;
import io.serverlessworkflow.api.types.Document;
import io.serverlessworkflow.api.types.Task;
import io.serverlessworkflow.api.types.TaskItem;
import io.serverlessworkflow.api.types.Workflow;
import io.serverlessworkflow.impl.WorkflowApplication;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@ApplicationScoped
public class StoryTeller {

  @Inject
  CreativeWriterProcessor creativeWriterProcessor;

  @Inject
  AudienceEditorProcessor audienceEditorProcessor;

  @Inject
  StyleEditorProcessor styleEditorProcessor;

  Workflow workflow;

  @PostConstruct
  void init() {
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
