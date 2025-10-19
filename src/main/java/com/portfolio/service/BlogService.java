package com.portfolio.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import com.portfolio.entity.Blog;
import com.portfolio.entity.Category;
import com.portfolio.repository.BlogRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.portfolio.repository.CategoryRepository;




@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    public Page<Blog> getBlogs(int page, int size){
        return blogRepository.findAll(PageRequest.of(page, size));
    }

    public Optional<Blog> getBlogById(Long id){
        return blogRepository.findById(id);
    }

    // safer: use repository query with limit instead of loading all blogs

    public List<Blog> getRecommendedBlogs(Long excludeId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return blogRepository.findRecommendedBlogs(excludeId, pageable).getContent();
    }

    // Create/save blog (handles category creation if needed)
    public Blog saveBlog(Blog blog) {
        Category cat = blog.getCategory();
        if (cat != null && cat.getId() == null && cat.getName() != null) {
            Category existing = categoryRepository.findByName(cat.getName()).orElse(null);
            if (existing != null) {
                blog.setCategory(existing);
            }
        }
        return blogRepository.save(blog);
    }
}
