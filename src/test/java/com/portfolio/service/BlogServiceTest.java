package com.portfolio.service;

import com.portfolio.entity.Blog;
import com.portfolio.entity.Category;
import com.portfolio.repository.BlogRepository;
import com.portfolio.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BlogServiceTest {

    @Mock private BlogRepository blogRepository;
    @Mock private CategoryRepository categoryRepository;
    @InjectMocks private BlogService blogService;

    @Test
    void getBlogs_ShouldReturnPage() {
        Blog blog = new Blog();
        Page<Blog> page = new PageImpl<>(Arrays.asList(blog));
        when(blogRepository.findAll(any(PageRequest.class))).thenReturn(page);
        
        Page<Blog> result = blogService.getBlogs(0, 10);
        
        assertEquals(1, result.getContent().size());
    }

    @Test
    void getBlogById_ShouldReturnBlog() {
        Blog blog = new Blog();
        blog.setId(1L);
        when(blogRepository.findById(1L)).thenReturn(Optional.of(blog));
        
        Optional<Blog> result = blogService.getBlogById(1L);
        
        assertTrue(result.isPresent());
    }

    @Test
    void saveBlog_WithExistingCategory_ShouldSave() {
        Blog blog = new Blog();
        Category category = new Category();
        category.setId(1L);
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));
        when(blogRepository.save(any())).thenReturn(blog);
        
        Blog result = blogService.saveBlog(blog, 1L, null);
        
        assertNotNull(result);
        verify(blogRepository).save(blog);
    }

    @Test
    void saveBlog_WithNewCategory_ShouldCreateAndSave() {
        Blog blog = new Blog();
        Category category = new Category();
        category.setName("Tech");
        when(categoryRepository.findByName("Tech")).thenReturn(Optional.empty());
        when(categoryRepository.save(any())).thenReturn(category);
        when(blogRepository.save(any())).thenReturn(blog);
        
        Blog result = blogService.saveBlog(blog, null, "Tech");
        
        assertNotNull(result);
        verify(categoryRepository).save(any());
    }
}
