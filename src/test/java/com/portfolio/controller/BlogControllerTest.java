package com.portfolio.controller;

import com.portfolio.entity.Blog;
import com.portfolio.service.BlogService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlogController.class)
class BlogControllerTest {

    @Autowired private MockMvc mockMvc;
    @MockBean private BlogService blogService;

    @Test
    void listBlogs_ShouldReturnBlogsPage() throws Exception {
        Blog blog = new Blog();
        blog.setTitle("Test Blog");
        Page<Blog> page = new PageImpl<>(Arrays.asList(blog));
        when(blogService.getBlogs(anyInt(), anyInt())).thenReturn(page);
        
        mockMvc.perform(get("/blogs"))
                .andExpect(status().isOk())
                .andExpect(view().name("blogs"))
                .andExpect(model().attributeExists("blogs"));
    }

    @Test
    void viewBlog_WithValidId_ShouldReturnBlogDetail() throws Exception {
        Blog blog = new Blog();
        blog.setId(1L);
        blog.setTitle("Test");
        when(blogService.getBlogById(1L)).thenReturn(Optional.of(blog));
        when(blogService.getRecommendedBlogs(anyLong(), anyInt())).thenReturn(Arrays.asList());
        
        mockMvc.perform(get("/blogs/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("blog-detail"));
    }

    @Test
    void viewBlog_WithInvalidId_ShouldReturn404() throws Exception {
        when(blogService.getBlogById(999L)).thenReturn(Optional.empty());
        
        mockMvc.perform(get("/blogs/999"))
                .andExpect(status().isNotFound());
    }
}
