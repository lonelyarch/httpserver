package com.hsdis.network;

import java.nio.channels.SocketChannel;

public class Socket {

	private SocketChannel channel = null;

	public SocketChannel getChannel() {
		return channel;
	}

	public Socket(SocketChannel channel) {
		this.channel = channel;
	}
}