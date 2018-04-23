package com.hsdis.network;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.Queue;
import java.util.Set;
import com.hsdis.network.util.FileUtils;

public class SocketProcessor implements Runnable {

	private Queue<Socket> queue = null;
	private Selector selector = null;

	public SocketProcessor(Queue<Socket> queue) throws Exception {
		this.queue = queue;
		this.selector = Selector.open();
	}

	@Override
	public void run() {
		try {
			while (true) {
				execute();
				handler();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void execute() throws Exception {
		Socket socket = this.queue.poll();
		while (null != socket) {
			SocketChannel channel = socket.getChannel();
			channel.configureBlocking(false);
			SelectionKey key = channel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			key.attach(socket);
			socket = this.queue.poll();
		}
	}

	private void handler() throws Exception {
		while (true) {
			int ready = selector.selectNow();
			if (ready == 0)
				break;
			Set<SelectionKey> keys = selector.selectedKeys();
			Iterator<SelectionKey> iterator = keys.iterator();
			while (iterator.hasNext()) {
				SelectionKey key = iterator.next();
				Socket socket = (Socket) key.attachment();
				if (key.isReadable()) {
					reader(socket.getChannel());
				}
				if (key.isWritable()) {
					writer(socket.getChannel());
				}
				iterator.remove();
			}
		}
	}

	private void reader(SocketChannel channel) throws Exception {
		ByteBuffer buf = ByteBuffer.allocate(1024 * 1024);
		int bytes = channel.read(buf);
		while (bytes > 0) {
			buf.flip();
			while (buf.hasRemaining()) {
				System.out.print((char) buf.get());
			}
			buf.clear();
			bytes = channel.read(buf);
		}
	}

	private void writer(SocketChannel channel) throws Exception {
		StringBuffer sb = new StringBuffer();
		String html = FileUtils.read("index.html");
		sb.append("HTTP/1.1 200 OK");
		sb.append("\r\n");
		sb.append(("Date: " + new Date()));
		sb.append("\r\n".getBytes());
		sb.append("Content-Type: text/html");
		sb.append("\r\n".getBytes());
		sb.append(("Content-Length: " + html.length()));
		sb.append("\r\n");
		sb.append("\r\n");
		sb.append(html);
		channel.write(ByteBuffer.wrap(sb.toString().getBytes()));
		channel.close();
	}
}