package com.example.blogdb.repository;

import com.example.blogdb.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedAtDesc();

    @Query("select distinct p from Post p left join fetch p.contentModules where p.id = :id")
    Optional<Post> findWithContentModulesById(@Param("id") Long id);
}
