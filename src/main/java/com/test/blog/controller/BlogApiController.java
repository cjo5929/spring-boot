package com.test.blog.controller;

import java.util.ArrayList;
import java.util.List;

import com.test.blog.domain.Article;
import com.test.blog.dto.AddArticleRequest;
import com.test.blog.dto.ArticleResponse;
import com.test.blog.dto.UpdateArticleRequest;
import com.test.blog.service.BlogService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BlogApiController {

	private final BlogService blogService;

	// 글 작성
	@PostMapping("/api/articles")
	public ResponseEntity<Article> addArticle(@RequestBody AddArticleRequest request) {
		Article savedArticle = blogService.save(request);
		System.out.println("ASdasdasd");
		return ResponseEntity.status(HttpStatus.CREATED)
			.body(savedArticle);
	}

	// 글 전체조회
	@GetMapping("/api/articles")
	public ResponseEntity<List<ArticleResponse>> findAllArticles() {
		List<Article> articleList = blogService.findAll();
		List<ArticleResponse> articles = new ArrayList<>();

		for (Article article : articleList) {
			articles.add(new ArticleResponse(article));
		}
		System.out.println(articleList);
		return ResponseEntity.ok().body(articles);
	}

	// 글 1개 조회
	@GetMapping("/api/articles/{id}")
	public ResponseEntity<ArticleResponse> findArticleById(@PathVariable Long id) {
		Article article = blogService.findById(id);
		return ResponseEntity.ok().body(new ArticleResponse(article));
	}

	// 글 삭제
	@DeleteMapping("/api/articles/{id}")
	public ResponseEntity<Void> deleteArticle(@PathVariable Long id) {
		blogService.delete(id);
		return ResponseEntity.ok().build();
	}

	// 글 수정
	@PutMapping("/api/articles/{id}")
	public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody UpdateArticleRequest request) {

		Article article = blogService.update(id, request);
		return ResponseEntity.ok().body(article);
	}
}
