package com.fajar.websockettriel.app;

import java.io.IOException;
import java.util.Date;

import javax.websocket.Session;

import com.fajar.websockettriel.dto.Message;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MessageMapper {

	public final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	public static Message getMessage(String json) {
		try {
			return OBJECT_MAPPER.readValue(json, Message.class);
		} catch (IOException e) {

			System.out.println("NOT A MESSAGE DTO");
			return null;
		}
	}

	public static String messageAsString(Message msg) {
		try {
			return OBJECT_MAPPER.writeValueAsString(msg);
		} catch (JsonProcessingException e) {

			e.printStackTrace();
			return "{}";
		}
	}

	public static String constructMessage(Session session, String destination, String msg) {

		Message message = new Message(destination, session.getId(), msg, new Date());
		return messageAsString(message);

	}

}
