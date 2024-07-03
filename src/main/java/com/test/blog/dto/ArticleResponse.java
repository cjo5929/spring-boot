package com.test.blog.dto;

import com.test.blog.domain.Article;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class ArticleResponse {
	private final String title;
	private final String content;

	public ArticleResponse(Article article) {
		this.title = article.getTitle();
		this.content = article.getContent();
	}

}
