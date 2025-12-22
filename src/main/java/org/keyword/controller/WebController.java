package org.keyword.controller;

import java.util.ArrayList;
import java.util.List;

import org.keyword.chatGPT.OpenAIService;
import org.keyword.model.ReportItem;
import org.keyword.service.ContentParser;
import org.keyword.service.SearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

    private final SearchService searchService;
    private final ContentParser contentParser;
    private final OpenAIService openAIService;

    public WebController(SearchService searchService, ContentParser contentParser, OpenAIService openAIService) {
        this.searchService = searchService;
        this.contentParser = contentParser;
        this.openAIService = openAIService;
    }

    @GetMapping("/")
    public String home() {
        return "keyword_index";
    }

    @PostMapping("/search")
    public String search(@RequestParam("keyword") String keyword, Model model) {
        System.out.println("Aranan kelime: " + keyword);
        
        // find links
        List<String> urls = searchService.searchGoogle(keyword);
        
        List<ReportItem> reports = new ArrayList<>();
        StringBuilder allSummariesText = new StringBuilder();

        // extract requests and summarize
        for (String url : urls) {
            String content = contentParser.extractText(url);
            
            if (!content.isEmpty()) {
                System.out.println("Özetleniyor: " + url);
                String summary = openAIService.summarize(content);
                
                reports.add(new ReportItem(url, summary));
                
                // keep in memory
                allSummariesText.append("Source: ").append(url).append("\nContent: ").append(summary).append("\n\n");
            }
        }

        // Create conclusion
        String finalConclusion = "Yeterli veri toplanamadığı için sonuç oluşturulamadı.";
        if (!reports.isEmpty()) {
            System.out.println("Final rapor hazırlanıyor...");
            finalConclusion = openAIService.generateConclusion(allSummariesText.toString());
        }

        // send results to HTML page
        model.addAttribute("keyword", keyword);
        model.addAttribute("reports", reports); // Özetler ve URL'ler
        model.addAttribute("conclusion", finalConclusion); // İstenen Final Conclusion

        return "processing"; 
    }
}