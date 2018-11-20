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

public class DweetClientTemperature {

	private String API_DWEET_END_POINT = "dweet.io";

	private JsonParser jsonParser = new JsonParser();
	private String thingName = "dat159-sensor";

	public DweetClientTemperature() {

	}

	public static void main(String[] args) throws IOException {
		DweetClientTemperature dc = new DweetClientTemperature();
		dc.publish(10);

		dc.get();
	}

	public boolean publish(int temperature) throws IOException {

		JsonObject content = new JsonObject();

		content.addProperty("temperature", temperature);

		thingName = URLEncoder.encode(thingName, "UTF-8");

		URL url = new URL("http://localhost:8080/tempsensor/current");

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
		System.out.println("Putting temperature. " + response.get("temperature").getAsString());

		connection.disconnect();

		return (true);
	}

	public String get() throws IOException {


		thingName = URLEncoder.encode(thingName, "UTF-8");

		URL url = new URL("http://localhost:8080/tempsensor/current");

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();

		connection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
		connection.setRequestProperty("Accept", "application/json");
		connection.setRequestMethod("GET");
		connection.setDoInput(true);
		connection.setDoOutput(true);

		JsonObject response = readResponse(connection.getInputStream());
		System.out.println("Temperature: " + response.get("temperature").getAsString());
		connection.disconnect();
		return response.get("temperature").toString();

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
