package io.github.tropheusj.gradle_testing;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public final class Main {
	public static void main(String[] args) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		JsonObject object = new JsonObject();
		object.addProperty("test", true);
		object.addProperty("h", "yes");

		System.out.println(gson.toJson(object));
	}
}
