package com.example.fn;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class HelloFunction {
	public static final String CONTAINER_URL = "https://gse00013735.storage.oraclecloud.com/v1/Storage-gse00013735/fn_container/";
	public static final String AUTH_URL = "https://gse00013735.storage.oraclecloud.com/auth/v1.0";

	public static class Input {
		public String user;
		public String password;
		public String content;

		@Override
		public String toString() {
			return "Input [user=" + user + ", password=" + password + ", content=" + content + "]";
		}
	}

	public static class Result {
		public int responseCode;

		@Override
		public String toString() {
			return "Result [responseCode=" + responseCode + "]";
		}
	}

	public void handleRequest(Input input) throws IOException {
		Result result = new Result();
		System.out.println(input);
		StringBuffer buffer = new StringBuffer(CONTAINER_URL).append("log_").append(new Date().getTime())
				.append(".json");
		String name = buffer.toString();
		System.out.println("Name: " + name);
		result.responseCode = putObject(getToken(input.user, input.password), input.content, name);
		System.out.println(result);
	}

	private int putObject(String token, String content, String name) throws IOException {
		URL url = new URL(name);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("PUT");
		conn.setRequestProperty("X-Auth-Token", token);
		conn.setDoOutput(true);

		/* Send the request data. */
		DataOutputStream output = new DataOutputStream(conn.getOutputStream());
		output.writeBytes(content);
		output.flush();

		int responseCode = conn.getResponseCode();
		return responseCode;
	}

	private String getToken(String user, String password) throws IOException {
		URL url = new URL(AUTH_URL);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.setRequestProperty("X-Storage-User", user);
		conn.setRequestProperty("X-Storage-Pass", password);
		String token = conn.getHeaderField("X-Auth-Token");
		return token;
	}

}
