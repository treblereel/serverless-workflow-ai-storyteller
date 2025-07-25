package org.treblereel.quarkus.llm.agentic.poc;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import dev.langchain4j.agentic.AgentServices;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import io.serverlessworkflow.ai.api.types.CallAgentAI;
import io.serverlessworkflow.ai.api.types.CallTaskAIChatModel;
import io.serverlessworkflow.api.types.Document;
import io.serverlessworkflow.api.types.Output;
import io.serverlessworkflow.api.types.Task;
import io.serverlessworkflow.api.types.TaskItem;
import io.serverlessworkflow.api.types.Workflow;
import io.serverlessworkflow.api.types.func.OutputAsFunction;
import io.serverlessworkflow.impl.WorkflowApplication;

public class StoryTeller {

  public static final ChatModel BASE_MODEL = OpenAiChatModel.builder()
          .apiKey(System.getenv("OPENAI_API_KEY"))
          .modelName("gpt-4o")
          .timeout(Duration.ofMinutes(10))
          .temperature(0.0)
          .logRequests(true)
          .logResponses(true)
          .build();

  private final Agents.CreativeWriter writer = AgentServices.agentBuilder(Agents.CreativeWriter.class)
          .chatModel(BASE_MODEL)
          .outputName("story")
          .build();

  private final Agents.StyleEditor editor = AgentServices.agentBuilder(Agents.StyleEditor.class)
          .chatModel(BASE_MODEL)
          .outputName("story")
          .build();

  private final Workflow workflow;

  public StoryTeller() {
    workflow =
            new Workflow()
                    .withDocument(
                            new Document().withNamespace("test").withName("testMap").withVersion("1.0"))
                    .withDo(List.of(new TaskItem("creative_story_task",
                                            new Task().withCallTask(
                                                    new CallTaskAIChatModel(
                                                            new CallAgentAI(writer)
                                                    )
                                            )
                                    ),
                                    new TaskItem("audience_story_task",
                                            new Task().withCallTask(
                                                    new CallTaskAIChatModel(
                                                            CallAgentAI.builder()
                                                                    .withAgentClass(Agents.AudienceEditor.class)
                                                                    .withChatModel(BASE_MODEL)
                                                                    .withOutputName("story")
                                                                    .build()
                                                    )
                                            )
                                    ),
                                    new TaskItem("style_story_task",
                                            new Task().withCallTask(
                                                    new CallTaskAIChatModel(
                                                            new CallAgentAI(editor)
                                                    )
                                            )
                                    )
                            )
                    ).withOutput(
                            new Output()
                                    .withAs(new OutputAsFunction().<Map<String,Object>, String>withFunction(map -> map.get("story").toString()))
                    );
  }

  public String tellStory(Map<String, String> topic) throws ExecutionException, InterruptedException {
    try (WorkflowApplication app = WorkflowApplication.builder().build()) {
      return app.workflowDefinition(workflow)
              .instance(topic)
              .start()
              .get()
              .asText()
              .orElseThrow();
    }
  }

}
