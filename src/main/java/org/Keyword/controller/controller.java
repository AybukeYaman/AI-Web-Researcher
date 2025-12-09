package org.Keyword.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class controller {

    @GetMapping("/")
    public String home() {
        return "keyword_index"; // templates/keyword_index.html
    }

    @PostMapping("/search")
    public String search(@RequestParam String keyword, Model model) {
        model.addAttribute("keyword", keyword);
        return "processing"; 
    }
}