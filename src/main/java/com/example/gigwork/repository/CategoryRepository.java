package com.example.gigwork.repository;

import com.example.gigwork.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, String> {
    // 대분류
    @Query(value = "SELECT * FROM category WHERE kind = :kind AND depth = :depth ORDER BY seq", nativeQuery = true)
    List<Category> findByKindAndDepthOrderBySeq(@Param("kind") String kind, @Param("depth") Integer depth);

    // 소분류 - 코드 범위(BETWEEN)
    @Query(value = "SELECT * FROM category WHERE kind = :kind AND depth = :depth AND cd BETWEEN :start AND :end ORDER BY seq", nativeQuery = true)
    List<Category> findRangeByKindAndDepthOrderBySeq(@Param("kind") String kind,
                                                     @Param("depth") Integer depth,
                                                     @Param("start") String start,
                                                     @Param("end") String end);

    // 소분류 - 접두(LIKE 'A__')
    @Query(value = "SELECT * FROM category WHERE kind = :kind AND depth = :depth AND cd LIKE :prefix ORDER BY seq", nativeQuery = true)
    List<Category> findByKindAndDepthAndCdLikeOrderBySeq(@Param("kind") String kind,
                                                         @Param("depth") Integer depth,
                                                         @Param("prefix") String prefix);
}


