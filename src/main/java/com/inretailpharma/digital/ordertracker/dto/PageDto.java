package com.inretailpharma.digital.ordertracker.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.Data;

@Data
public class PageDto<T> {
	private Integer totalPages;
	private Integer currentPage;
	private List<T> data;
	
	public PageDto(Page<T> page) {
		this.data = page.getContent();
		this.currentPage = page.getNumber();
		this.totalPages = page.getTotalPages();
	}	
}
