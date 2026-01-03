package com.portfolio.repository;

import com.portfolio.entity.Blog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BlogRepositoryTest {

    @Autowired private BlogRepository blogRepository;

    @Test
    void findRecommendedBlogs_ShouldExcludeGivenId() {
        Blog blog1 = new Blog();
        blog1.setTitle("Blog 1");
        blog1.setCreatedAt(LocalDateTime.now());
        blogRepository.save(blog1);

        Blog blog2 = new Blog();
        blog2.setTitle("Blog 2");
        blog2.setCreatedAt(LocalDateTime.now());
        blogRepository.save(blog2);

        Page<Blog> results = blogRepository.findRecommendedBlogs(blog1.getId(), PageRequest.of(0, 10));
        
        assertEquals(1, results.getContent().size());
        assertNotEquals(blog1.getId(), results.getContent().get(0).getId());
    }

    @Test
    void save_ShouldPersistBlog() {
        Blog blog = new Blog();
        blog.setTitle("Test");
        blog.setContent("Content");
        
        Blog saved = blogRepository.save(blog);
        
        assertNotNull(saved.getId());
        assertEquals("Test", saved.getTitle());
    }
}
