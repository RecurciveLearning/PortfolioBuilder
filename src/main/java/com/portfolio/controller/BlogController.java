package com.portfolio.controller;

import com.portfolio.entity.Blog;
import com.portfolio.entity.Category;
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

    /**
     * List of all blogs with pagination
     */
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


    @GetMapping("/{id}")
    public String blogDetail(@PathVariable Long id, HttpServletRequest request, Model model) {
        Blog blog = blogService.getBlogById(id)
                .orElseThrow(() -> new RuntimeException("Blog not found with ID: " + id));

        // Shorten share URL
        String fullUrl = request.getRequestURL().toString();
        String shortUrl = shortenUrl(fullUrl);

        // If image URL is very long, truncate it for display
        String imageUrl = blog.getImage();
        String shortImageUrl = null;
        if (imageUrl != null && imageUrl.length() > 100) {
            shortImageUrl = imageUrl.substring(0, 100) + "...";
        } else {
            shortImageUrl = imageUrl;
        }

        model.addAttribute("blog", blog);
        model.addAttribute("shortImageUrl", shortImageUrl);
        model.addAttribute("currentUrl", shortUrl);
        model.addAttribute("recommendedBlogs", blogService.getRecommendedBlogs(id, 3));

        return "blog-detail"; // Thymeleaf template: blog-detail.html
    }


    @GetMapping("/new")
    public String newBlogForm(Model model) {
        model.addAttribute("blog", new Blog());
        model.addAttribute("categories", categoryRepository.findAll());
        return "blog-form"; // Thymeleaf form for admin/blog creation
    }


    @PostMapping("/save")
    public String saveBlog(@ModelAttribute Blog blog,
                           @RequestParam(required = false) String categoryName,
                           RedirectAttributes ra) {

        if (blog.getCategory() == null && categoryName != null && !categoryName.isBlank()) {
            Category cat = categoryRepository.findByName(categoryName)
                    .orElseGet(() -> new Category(categoryName));
            blog.setCategory(cat);
        }

        blogService.saveBlog(blog);
        ra.addFlashAttribute("message", "Blog saved successfully!");
        return "redirect:/blogs";
    }

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
            return longUrl; // fallback if TinyURL API fails
        }
    }
}
