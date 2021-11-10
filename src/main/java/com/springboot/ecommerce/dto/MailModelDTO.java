package com.springboot.ecommerce.dto;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MailModelDTO {
	private String from = "hungnnit98@gmail.com";
	String to;
	String sub;
	String body;
	List<String> cc = new ArrayList<String>();
	List<String> bcc = new ArrayList<String>();
	List<File> files = new ArrayList<File>();
	

	public MailModelDTO(String to, String sub, String body) {
		super();
		this.to = to;
		this.sub = sub;
		this.body = body;
	}}
