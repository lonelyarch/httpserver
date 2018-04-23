package com.hsdis.network;

import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Queue;

public class SocketAccepter implements Runnable {

	private int port = 80; // default
	private ServerSocketChannel ssc = null;

	private Queue<Socket> queue = null;

	public SocketAccepter(int port, Queue<Socket> queue) {
		this.port = port;
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			this.ssc = ServerSocketChannel.open();
			this.ssc.bind(new InetSocketAddress(port));
			this.ssc.configureBlocking(false);
		} catch (Exception e) {
		}
		while (true) {
			try {
				SocketChannel sc = this.ssc.accept();
				if (null != sc) {
					this.queue.add(new Socket(sc));
				}
			} catch (Exception e) {
			}
		}
	}
}