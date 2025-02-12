package com.test.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.blog.domain.Article;
import com.test.blog.dto.AddArticleRequest;
import com.test.blog.dto.UpdateArticleRequest;
import com.test.blog.repository.BlogRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BlogApiControllerTest {

	@Autowired
	protected MockMvc mvc;

	@Autowired
	protected ObjectMapper objectMapper;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	BlogRepository blogRepository;

	@BeforeEach
	public void mockMvcSetup() {
		this.mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
			.build();
		blogRepository.deleteAll();

	}

	@DisplayName("addArticle : 블로그에 글 추가 성공")
	@Test
	public void addArticle() throws Exception {
		final String url = "/api/articles";
		final String title = "title";
		final String content = "content";
		final AddArticleRequest addArticleRequest = new AddArticleRequest(title, content);

		final String requestBody = objectMapper.writeValueAsString(addArticleRequest);

		ResultActions result = mvc.perform(post(url)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.content(requestBody)
		);

		result.andExpect(status().isCreated());

		List<Article> articles = blogRepository.findAll();
		assertThat(articles.size()).isEqualTo(1); // 크기가 1인지 검증
		assertThat(articles.get(0).getTitle()).isEqualTo(title);
		assertThat(articles.get(0).getContent()).isEqualTo(content);
	}

	@DisplayName("findAllArticles: 블로그 글 목록 조회에 성공한다.")
	@Test
	public void findAllArticles() throws Exception {
		final String url = "/api/articles";
		final String title = "title";
		final String content = "content";

		blogRepository.save(Article.builder().title(title).content(content).build());

		final ResultActions resultActions = mvc.perform(get(url)
			.accept(MediaType.APPLICATION_JSON_VALUE));

		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("$[0].content").value(content))
			.andExpect(jsonPath("$[0].title").value(title));
	}

	@DisplayName("findArticle: 블로그 글 조회에 성공한다.")
	@Test
	public void findArticle() throws Exception {
		final String url = "/api/articles/{id}";
		final String title = "title";
		final String content = "content";

		Article savedArticle = blogRepository.save(Article.builder().title(title).content(content).build());

		final ResultActions resultActions = mvc.perform(get(url, savedArticle.getId()));

		resultActions.andExpect(status().isOk())
			.andExpect(jsonPath("$.content").value(content))
			.andExpect(jsonPath("$.title").value(title));
	}

	@DisplayName("deleteArticle: 블로그 글 삭제에 성공한다.")
	@Test
	public void deleteArticle() throws Exception {
		// given
		final String url = "/api/articles/{id}";
		final String title = "title";
		final String content = "content";
		Article savedArticle = blogRepository.save(Article.builder()
			.title(title)
			.content(content)
			.build());
		// when
		mvc.perform(delete(url, savedArticle.getId()))
			.andExpect(status().isOk());
		// then
		List<Article> articles = blogRepository.findAll();
		assertThat(articles).isEmpty();
	}

	@DisplayName("updateArticle: 블로그 글 수정에 성공한다.")
	@Test
	public void updateArticle() throws Exception {
		// given
		final String url = "/api/articles/{id}";
		final String title = "title";
		final String content = "content";
		Article savedArticle = blogRepository.save(Article.builder()
			.title(title)
			.content(content)
			.build());
		final String newTitle = "new title";
		final String newContent = "new content";
		UpdateArticleRequest request = new UpdateArticleRequest(newTitle,
			newContent);
		// when
		ResultActions result = mvc.perform(put(url, savedArticle.getId())
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.content(objectMapper.writeValueAsString(request)));
		// then
		result.andExpect(status().isOk());
		Article article = blogRepository.findById(savedArticle.getId()).get();
		assertThat(article.getTitle()).isEqualTo(newTitle);
		assertThat(article.getContent()).isEqualTo(newContent);
	}

}