package no.hvl.dat159.dweet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DweetClientHeating {

	private String API_DWEET_END_POINT = "dweet.io";

	private JsonParser jsonParser = new JsonParser();
	private String thingName = "dat159-sensor";

	public DweetClientHeating() {

	}


	public boolean publish(int heating) throws IOException {

		JsonObject content = new JsonObject();

		content.addProperty("status", heating);

		thingName = URLEncoder.encode(thingName, "UTF-8");

		URL url = new URL("http://localhost:8080/heatsensor/current");

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		connection.setRequestMethod("PUT");
		connection.setDoInput(true);
		connection.setDoOutput(true);

		PrintWriter out = new PrintWriter(connection.getOutputStream());

		out.println(content.toString());

		out.flush();
		out.close();

		JsonObject response = readResponse(connection.getInputStream());
		System.out.println("Status: " + response.get("status").getAsString());

		connection.disconnect();

		return (true);
	}

	public String get() throws IOException {


		thingName = URLEncoder.encode(thingName, "UTF-8");

		URL url = new URL("http://localhost:8080/heatsensor/current");

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestMethod("GET");
		connection.setDoInput(true);
		connection.setDoOutput(true);

		JsonObject response = readResponse(connection.getInputStream());
		connection.disconnect();
		System.out.println("Getting heat status: " + response.get("status").getAsString());
		return response.get("status").getAsString();

	}

	private JsonObject readResponse(InputStream in) {

		Scanner scan = new Scanner(in);
		StringBuilder sn = new StringBuilder();

		while (scan.hasNext())
			sn.append(scan.nextLine()).append('\n');

		scan.close();

		return jsonParser.parse(sn.toString()).getAsJsonObject();
	}
}
