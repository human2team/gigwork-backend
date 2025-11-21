package com.example.gigwork.controller;

import com.example.gigwork.entity.Category;
import com.example.gigwork.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;

    // GET /api/categories?kind=01&depth=1
    // GET /api/categories?kind=01&depth=2&start=A01&end=A99
    @GetMapping
    public ResponseEntity<List<Map<String, String>>> getCategories(
            @RequestParam("kind") String kind,
            @RequestParam("depth") Integer depth,
            @RequestParam(value = "start", required = false) String start,
            @RequestParam(value = "end", required = false) String end,
            @RequestParam(value = "parent", required = false) String parent
    ) {
        try {
            List<Category> categories;
            if (depth != null && depth == 2) {
                if (parent != null && !parent.isBlank()) {
                    // parent=A00 -> A01~A99 범위 조회(더 명확)
                    String letter = parent.substring(0, 1);
                    String computedStart = letter + "01";
                    String computedEnd = letter + "99";
                    categories = categoryRepository.findRangeByKindAndDepthOrderBySeq(kind, depth, computedStart, computedEnd);
                    // 혹시라도 비어오면 LIKE로 한 번 더 시도
                    if (categories.isEmpty()) {
                        String prefix = letter + "__";
                        categories = categoryRepository.findByKindAndDepthAndCdLikeOrderBySeq(kind, depth, prefix);
                    }
                } else if (start != null && end != null) {
                    categories = categoryRepository.findRangeByKindAndDepthOrderBySeq(kind, depth, start, end);
                } else {
                    // depth=2인데 범위정보가 없으면 빈 배열
                    categories = List.of();
                }
            } else {
                categories = categoryRepository.findByKindAndDepthOrderBySeq(kind, depth);
            }
            List<Map<String, String>> result = categories.stream()
                    .map(c -> Map.of("cd", c.getCd(), "nm", c.getNm()))
                    .collect(java.util.stream.Collectors.toList());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            // 에러 시 200 + 빈 배열 반환해 프론트가 폴백을 사용하도록
            return ResponseEntity.ok(List.of());
        }
    }
}


