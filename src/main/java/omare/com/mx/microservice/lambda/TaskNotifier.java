package omare.com.mx.microservice.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.HttpResponse;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TaskNotifier implements RequestHandler<Object, String> {

    private static final String TASK_API_URL = "https://localhost:8080/task-management/api/v1/tasks";

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String handleRequest(Object input, Context context) {
        try {
            String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

            URI uri = new URIBuilder(TASK_API_URL)
                    .addParameter("dueBefore", today)
                    .addParameter("completed", "false")
                    .build();

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet request = new HttpGet(uri);
            // Add Authorization header if needed:
            // request.addHeader("Authorization", "Bearer <token>");

            HttpResponse response = httpClient.execute(request);
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                context.getLogger().log("Failed to fetch tasks. Status: " + statusCode);
                return "Error: Failed to fetch tasks.";
            }

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(response.getEntity().getContent()));
            JsonNode tasks = objectMapper.readTree(reader);

            if (tasks.isArray()) {
                for (JsonNode task : tasks) {
                    String id = task.get("id").asText();
                    String title = task.get("title").asText();
                    String dueDate = task.get("dueDate").asText();
                    context.getLogger().log("Overdue Task: " + id + ", Title: " + title + ", Due: " + dueDate);

                    //Integrate with SNS
                    AmazonSNS snsClient = AmazonSNSClientBuilder.defaultClient();
                    snsClient.publish("topicArn", "Overdue task: " + title);

                }
            }

            httpClient.close();
            return "Success";
        } catch (Exception e) {
            context.getLogger().log("Exception: " + e.getMessage());
            return "Error: Exception occurred.";
        }
    }
}
