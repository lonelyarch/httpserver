package com.hsdis.network.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils {

	public static String read(String file) {
		StringBuilder res = new StringBuilder();
		String path = System.getProperty("user.dir") + "\\src\\web\\" + file;
		try {
			InputStream is = new FileInputStream(new File(path));
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = null;
			while ((line = br.readLine()) != null) {
				res.append(line);
			}
			br.close();
		} catch (Exception e) {
		}
		return res.toString();
	}
}