package no.hvl.dat159;

import static spark.Spark.*;
import com.google.gson.*;
import no.hvl.dat159.roomcontrol.*;

public class Main {

	static Temperature temp = null;
	// static Room room = new Room(temp.getTemperature());
	static HeatingController heat;

	public static void main(String[] args) {
		heat = new HeatingController();
		temp = new Temperature();

		port(8080);

		after((req, res) -> {
			res.type("application/json");
		});

		get("/tempsensor/current", (req, res) -> tempToJson());
		get("/heatsensor/current", (req, res) -> heatToJson());

		put("/tempsensor/current", (req, res) -> {

			Gson gson = new Gson();

			temp = gson.fromJson(req.body(), Temperature.class);

			return tempToJson();

		});

		put("/heatsensor/current", (req, res) -> {

			Gson gson = new Gson();

			heat = gson.fromJson(req.body(), HeatingController.class);

			return heatToJson();

		});

	}

	static String tempToJson() {
		Gson gson = new Gson();
		String jsonInString = gson.toJson(temp);
		return jsonInString;
	}

	static String heatToJson() {
		Gson gson2 = new Gson();
		String jsonInString = gson2.toJson(heat);
		return jsonInString;
	}
}