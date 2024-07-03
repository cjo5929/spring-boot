package com.test.blog.service;

import java.util.List;

import com.test.blog.domain.Article;
import com.test.blog.dto.AddArticleRequest;
import com.test.blog.dto.UpdateArticleRequest;
import com.test.blog.repository.BlogRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BlogService {

	private final BlogRepository blogRepository;

	// 글 작성
	public Article save(AddArticleRequest request) {
		return blogRepository.save(request.toEntity());
	}

	// 글 전체조회
	public List<Article> findAll() {
		return blogRepository.findAll();
	}

	// 글 1개 조회
	public Article findById(Long id) {
		return blogRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("not found: " + id));
	}

	// 글 삭제
	public void delete(Long id) {
		blogRepository.deleteById(id);
	}

	// 글 수정
	@Transactional
	public Article update(Long id, UpdateArticleRequest request) {
		Article article = findById(id);

		article.update(request.getTitle(), request.getContent());

		return article;
	}

}
