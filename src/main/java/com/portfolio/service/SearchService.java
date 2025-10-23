package com.portfolio.service;
import com.portfolio.entity.EmbeddingEntry;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SearchService {

    private final EmbeddingLoader embeddingLoader;

    public SearchService(EmbeddingLoader embeddingLoader) {
        this.embeddingLoader = embeddingLoader;
    }


    @Cacheable(value = "searchResults", key = "#root.methodName + ':' + #query.toLowerCase().trim()")
    public List<EmbeddingEntry> searchByKeywords(String query) {
        if (query == null || query.isBlank()) return Collections.emptyList();

        List<EmbeddingEntry> embeddings = getCachedEmbeddings();
        if (embeddings.isEmpty()) return Collections.emptyList();

        String normalizedQuery = query.toLowerCase().trim();
        List<String> keywords = extractKeywords(normalizedQuery);

        return embeddings.stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(entry, calculateRelevanceScore(entry, keywords, normalizedQuery)))
                .filter(e -> e.getValue() > 0)
                .sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
                .limit(5)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }


    public String generateResponse(String query, List<EmbeddingEntry> matches) {
        if (query == null || query.isBlank()) return "Please type something to search.";
        if (matches == null || matches.isEmpty()) return generateDefaultResponse(query);

        StringBuilder response = new StringBuilder();
        String normalizedQuery = query.toLowerCase();

        // Intro based on query patterns
        if (containsPattern(normalizedQuery, "experience", "work", "job", "career")) {
            response.append("Based on my experience:\n\n");
        } else if (containsPattern(normalizedQuery, "project", "built", "created", "developed")) {
            response.append("Here are some relevant projects:\n\n");
        } else if (containsPattern(normalizedQuery, "skill", "technology", "tech", "know", "proficient")) {
            response.append("Regarding skills and technologies:\n\n");
        } else if (containsPattern(normalizedQuery, "education", "degree", "study", "university", "college")) {
            response.append("About my education:\n\n");
        } else if (containsPattern(normalizedQuery, "who", "about", "tell me", "introduce")) {
            response.append("Let me tell you about myself:\n\n");
        } else {
            response.append("Here's what I found:\n\n");
        }

        for (int i = 0; i < Math.min(3, matches.size()); i++) {
            EmbeddingEntry entry = matches.get(i);
            String cleanText = cleanText(entry.getText());

            if (i > 0) response.append("\n");

            switch (entry.getType()) {
                case "user":
                case "experience":
                case "project":
                case "skill":
                case "education":
                    response.append("• ").append(cleanText);
                    break;
                default:
                    response.append(cleanText);
            }
            response.append("\n");
        }

        return response.toString().trim();
    }


    @Cacheable(value = "allEmbeddings", key = "'all'", unless = "#result == null || #result.isEmpty()")
    public List<EmbeddingEntry> getCachedEmbeddings() {
        List<EmbeddingEntry> embeddings = embeddingLoader.getEmbeddings();
        return embeddings != null ? embeddings : Collections.emptyList();
    }

    @CacheEvict(value = {"allEmbeddings", "searchResults"}, allEntries = true)
    public void refreshCache() {
        List<EmbeddingEntry> embeddings = embeddingLoader.getEmbeddings();
        if (embeddings != null && !embeddings.isEmpty()) {
            getCachedEmbeddings();
        }
        System.out.println("Cache refreshed: allEmbeddings and searchResults cleared and embeddings reloaded.");
    }


    private List<String> extractKeywords(String query) {
        String[] stopWords = {
                "the","is","at","which","on","a","an","and","or","but","in","with","to","for","of","as","by","from","about",
                "into","through","during","before","after","above","below","between","under","again","further","then","once",
                "here","there","when","where","why","how","all","both","each","few","more","most","other","some","such",
                "no","nor","not","only","own","same","so","than","too","very","can","will","just","should","now","what",
                "who","do","does","did","your","you","me","my","i","am","are","was","were","be","been","being","have",
                "has","had","having","tell","know","get"
        };
        Set<String> stopWordSet = new HashSet<>(Arrays.asList(stopWords));

        return Arrays.stream(query.split("\\s+"))
                .map(String::toLowerCase)
                .map(w -> w.replaceAll("[^a-z0-9]", ""))
                .filter(w -> !w.isEmpty() && !stopWordSet.contains(w) && w.length() > 2)
                .collect(Collectors.toList());
    }

    private double calculateRelevanceScore(EmbeddingEntry entry, List<String> keywords, String query) {
        if (entry == null || entry.getText() == null) return 0.0;
        String entryText = entry.getText().toLowerCase();
        double score = 0.0;

        for (String keyword : keywords) {
            if (entryText.contains(keyword)) {
                score += 2.0;
                if (entryText.contains(" " + keyword + " ") || entryText.startsWith(keyword + " ") || entryText.endsWith(" " + keyword)) {
                    score += 1.0;
                }
            }
        }

        if (!keywords.isEmpty() && entryText.contains(query.toLowerCase())) score += 5.0;

        switch (entry.getType()) {
            case "user": score *= 1.5; break;
            case "experience": score *= 1.3; break;
            case "project": score *= 1.2; break;
        }
        return score;
    }

    private boolean containsPattern(String text, String... patterns) {
        if (text == null) return false;
        for (String pattern : patterns) {
            if (text.contains(pattern)) return true;
        }
        return false;
    }

    private String cleanText(String text) {
        if (text == null) return "";
        return text.replaceAll("^(User|Project|Experience|Skill|Education):\\s*", "").trim();
    }

    private String generateDefaultResponse(String query) {
        String normalizedQuery = query != null ? query.toLowerCase() : "";

        if (containsPattern(normalizedQuery, "hello", "hi", "hey", "greetings")) {
            return "Hello! I'm Himanshu Singh, a Java Backend Developer with 3.1 years of experience. How can I help you today?";
        } else if (containsPattern(normalizedQuery, "contact", "email", "reach", "get in touch")) {
            return "You can reach out to me through the contact form on this website, or check my profile for contact details.";
        } else if (containsPattern(normalizedQuery, "thanks", "thank you")) {
            return "You're welcome! Feel free to ask me anything else.";
        }

        return "I'm sorry, I couldn't find specific information about that. You can ask me about:\n" +
                "• My work experience and roles\n" +
                "• Projects I've built\n" +
                "• My technical skills\n" +
                "• My education background\n" +
                "• Or just say hi!";
    }
}
