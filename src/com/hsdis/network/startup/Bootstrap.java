package com.hsdis.network.startup;

import com.hsdis.network.HttpServer;

public class Bootstrap {

	public static void main(String[] args) {
		new HttpServer(8088).start();
	}
}