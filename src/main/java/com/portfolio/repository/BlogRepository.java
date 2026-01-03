package com.portfolio.repository;
import com.portfolio.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import jakarta.persistence.QueryHint;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    List<Blog> findTop3ByIdNotOrderByCreatedAtDesc(Long id);
    
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    List<Blog> findByIdNotOrderByCreatedAtDesc(Long excludeId, Pageable pageable);

    @Query("SELECT b FROM Blog b WHERE b.id <> :excludeId ORDER BY b.createdAt DESC")
    @QueryHints(@QueryHint(name = "org.hibernate.cacheable", value = "true"))
    Page<Blog> findRecommendedBlogs(@Param("excludeId") Long excludeId, Pageable pageable);
}

