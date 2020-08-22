package com.fajar.websockettriel.server;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.fajar.websockettriel.app.MessageMapper;
import com.fajar.websockettriel.dto.Message;

@ServerEndpoint(value = "/test")

public class WebsocketServer {

	private Logger logger = Logger.getLogger(this.getClass().getName());
	private static final Map<String, Session> SESSION_MAP = new ConcurrentHashMap<String, Session>();

	@OnOpen

	public void onOpen(Session session) {

		logger.info("Connected ... " + session.getId());

		SESSION_MAP.put(session.getId(), session);
		try {
			session.getBasicRemote().sendText("[ID]" + session.getId());
		} catch (IOException e) {

			e.printStackTrace();
		}

		logger.info("Current Connected: " + SESSION_MAP.size());

	}

	@OnMessage
	public void onMessage(String message, Session session) {
		System.out.println("SERVER ONMessage [" + session.getId() + "]: " + message);
		if (message.equals("quit")) {

			this.disconnect(session);
		}

		try {
			Message messageObj = MessageMapper.getMessage(message);
			sendMessageTo(messageObj, session);
		} catch (Exception e) {
			System.out.println("NOT a Message DTO: " + session.getId());

			try {
				session.getBasicRemote().sendText(message);
			} catch (IOException e1) {

				e1.printStackTrace();
			}
		}
//		try {
//			session.getBasicRemote().sendText(message);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

	private void disconnect(Session session) {
		 
		try {

			session.close(new CloseReason(CloseCodes.NORMAL_CLOSURE, "Game ended"));
			SESSION_MAP.remove(session.getId());
			System.out.println("DISCONNECTED: "+session.getId());

		} catch (IOException e) {

			throw new RuntimeException(e);

		}

	}

	private void sendMessageTo(Message message, Session sender) throws IOException {
		String destination = message.getMessageTo();
		String newMsg = MessageMapper.constructMessage(sender, destination, message.getMessage());

		Session destinationSession = SESSION_MAP.get(destination);
		if (null == destinationSession) {
			System.out.println("destinationSession is NULL");
			String msg = MessageMapper.OBJECT_MAPPER.writeValueAsString(
					new Message(sender.getId(), "SERVER", "Destination (" + destination + ") NOT FOUND", new Date()));
			sender.getBasicRemote().sendText(msg);
			return;
		}
		destinationSession.getBasicRemote().sendText(newMsg);

	}

	@OnClose

	public void onClose(Session session, CloseReason closeReason) {

		logger.info(String.format("Session %s closed because of %s", session.getId(), closeReason));

	}

}
