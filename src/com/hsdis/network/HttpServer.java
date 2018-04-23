package com.hsdis.network;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class HttpServer {

	private int port = 80; // default

	private SocketAccepter accepter = null;
	private SocketProcessor processor = null;

	public HttpServer(int port) {
		this.port = port;
	}

	public void start() {
		try {
			Queue<Socket> queue = new ArrayBlockingQueue<Socket>(128);
			this.accepter = new SocketAccepter(this.port, queue);
			this.processor = new SocketProcessor(queue);
			new Thread(this.accepter).start();
			new Thread(this.processor).start();
		} catch (Exception e) {
		}
	}
}