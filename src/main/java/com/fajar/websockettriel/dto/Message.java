package com.fajar.websockettriel.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Message implements Serializable{/**
	 * 
	 */
	private static final long serialVersionUID = 288170501686629409L;
	private String messageTo;
	private String messageFrom;
	private String message;
	private Date date;

}
