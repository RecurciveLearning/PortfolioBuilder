package com.portfolio.repository;
import com.portfolio.entity.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog, Long> {
    List<Blog> findTop3ByIdNotOrderByCreatedAtDesc(Long id);
    // Generic form if you want variable limit
    List<Blog> findByIdNotOrderByCreatedAtDesc(Long excludeId, Pageable pageable);

    @Query("SELECT b FROM Blog b WHERE b.id <> :excludeId ORDER BY b.createdAt DESC")
    Page<Blog> findRecommendedBlogs(@Param("excludeId") Long excludeId, Pageable pageable);

}

