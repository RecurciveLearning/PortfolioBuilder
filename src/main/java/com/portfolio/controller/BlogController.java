package com.portfolio.controller;

import com.portfolio.entity.Blog;
import com.portfolio.entity.Category;
import com.portfolio.repository.BlogRepository;
import com.portfolio.repository.CategoryRepository;
import com.portfolio.service.BlogService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/blogs")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BlogRepository blogRepository;

    /** List of blogs with pagination */
    @GetMapping
    public String blogList(Model model,
                           @RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "6") int size) {

        Page<Blog> blogsPage = blogService.getBlogs(page, size);

        model.addAttribute("blogs", blogsPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", blogsPage.getTotalPages());
        model.addAttribute("pageSize", size);

        return "blogs"; // Thymeleaf template: blogs.html
    }

    /** Blog detail page */
    @GetMapping("/{id}")
    public String blogDetail(@PathVariable Long id, HttpServletRequest request, Model model) {
        Blog blog = blogService.getBlogById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found with ID: " + id));

        // Shorten URL using TinyURL
        String fullUrl = request.getRequestURL().toString();
        String shortUrl = shortenUrl(fullUrl);

        // Shorten image URL for display if too long
        String imageUrl = blog.getImage();
        String shortImageUrl = (imageUrl != null && imageUrl.length() > 100)
                ? imageUrl.substring(0, 100) + "..."
                : imageUrl;

        model.addAttribute("blog", blog);
        model.addAttribute("shortImageUrl", shortImageUrl);
        model.addAttribute("currentUrl", shortUrl);
        model.addAttribute("recommendedBlogs", blogService.getRecommendedBlogs(id, 3));

        return "blog-detail"; // Thymeleaf template
    }

    /** Show blog creation form */
    @GetMapping("/new")
    public String newBlogForm(Model model) {
        model.addAttribute("blog", new Blog());
        model.addAttribute("categories", categoryRepository.findAll());
        return "blog-form"; // Thymeleaf form
    }

    /** Save blog - works for public users */
    @PostMapping("/save")
    public String saveBlog(@ModelAttribute Blog blog,
                           @RequestParam(value = "category.id", required = false) Long categoryId,
                           @RequestParam(value = "categoryName", required = false) String categoryName,
                           RedirectAttributes redirectAttributes) {

        Category category = null;

        // 1️⃣ Use existing category if selected
        if (categoryId != null) {
            category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("Category not found"));
        }
        // 2️⃣ Create new category if provided
        else if (categoryName != null && !categoryName.trim().isEmpty()) {
            category = new Category();
            category.setName(categoryName.trim());
            categoryRepository.save(category); // persist category
        }

        blog.setCategory(category);
        blogRepository.save(blog);

        redirectAttributes.addFlashAttribute("success", "Blog saved successfully!");
        return "redirect:/blogs";
    }

    /** Helper method to shorten URLs using TinyURL API */
    private String shortenUrl(String longUrl) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://tinyurl.com/api-create.php?url=" +
                            URLEncoder.encode(longUrl, StandardCharsets.UTF_8)))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
            return longUrl; // fallback
        }
    }
}
