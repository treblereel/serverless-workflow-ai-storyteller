package org.treblereel.quarkus.llm.agentic.poc;

import dev.langchain4j.agentic.AgentServices;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import io.serverlessworkflow.api.types.Workflow;
import io.serverlessworkflow.fluent.spec.WorkflowBuilder;
import io.serverlessworkflow.impl.WorkflowApplication;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ExecutionException;

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

    private final Agents.AudienceEditor audience = AgentServices.agentBuilder(Agents.AudienceEditor.class)
            .chatModel(BASE_MODEL)
            .outputName("story")
            .build();

    private final Agents.StyleEditor editor = AgentServices.agentBuilder(Agents.StyleEditor.class)
            .chatModel(BASE_MODEL)
            .outputName("story")
            .build();

    private final Workflow workflow;

    public StoryTeller() {
        workflow = WorkflowBuilder.workflow("flowCallQuery")
                        .tasks(
                                d -> d.callAgentAI("creative_story_task", c -> c.withAgent(writer).build())
                                        .callAgentAI("audience_story_task", c -> c.withAgent(audience).build())
                                        .callAgentAI("style_story_task", c -> c.withAgent(editor).build())
                        ).build();
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
