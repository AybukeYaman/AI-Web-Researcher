package org.keyword.chatGPT;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class OpenAIService {

    // Put API KEY here
    private static final String API_KEY = "API_KEY"; 

    // "gemini-2.5-flash" is used
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + API_KEY;

    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public OpenAIService() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(30)) // Cevap süresini biraz daha artırdık
                .build();
        this.objectMapper = new ObjectMapper();
    }

    // Summarize part
    public String summarize(String content) {
        // if length of content is more than 10.000 characters, takeo nly the first 10.000 characters just to be safe.
        String truncated = content.length() > 10000 ? content.substring(0, 10000) : content;

        // clean characters that can cause error in JSON format
        String safeContent = escapeJson(truncated);
        
        String prompt = "Summarize the following website content: " + safeContent;
        String jsonBody = "{ \"contents\": [{ \"parts\": [{ \"text\": \"" + prompt + "\" }] }] }";

        return sendRequest(jsonBody);
    }

    // Conclusion
    public String generateConclusion(String allSummaries) {
        String safeSummaries = escapeJson(allSummaries);
        String prompt = "Here are summaries from different sources based on a keyword search:\\n" + safeSummaries + 
                        "\\n\\nBased on these summaries, write a final comparative conclusion paragraph.";
        
        String jsonBody = "{ \"contents\": [{ \"parts\": [{ \"text\": \"" + prompt + "\" }] }] }";

        return sendRequest(jsonBody);
    }

    // Clean JSON characters
    private String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", " ")
                   .replace("\r", " ")
                   .replace("\t", " ");
    }

    // Sending requests
    private String sendRequest(String jsonBody) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(API_URL))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode rootNode = objectMapper.readTree(response.body());
                if (rootNode.has("candidates") && !rootNode.path("candidates").isEmpty()) {
                    return rootNode.path("candidates").get(0)
                                   .path("content").get("parts").get(0)
                                   .path("text").asText();
                } else {
                    return "Model boş cevap döndü.";
                }
            } else {
                return "API Hatası (" + response.statusCode() + "): " + response.body();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Sistem Hatası: " + e.getMessage();
        }
    }
}