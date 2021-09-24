package io.ib67.util.serialization.bukkit;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.lang.reflect.Type;

public class LocationSerializer implements JsonSerializer<Location>,JsonDeserializer<Location> {
    public JsonElement serialize(Location src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jo = new JsonObject();
        jo.add("x", new JsonPrimitive(src.getX()));
        jo.add("y", new JsonPrimitive(src.getY()));
        jo.add("z", new JsonPrimitive(src.getZ()));
        jo.add("world", new JsonPrimitive(src.getWorld().getName()));
        return jo;
    }
    @Override
    public Location deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new Location(Bukkit.getWorld(json.getAsJsonObject().get("world").getAsString()), json.getAsJsonObject().get("x").getAsDouble(), json.getAsJsonObject().get("y").getAsDouble(), json.getAsJsonObject().get("z").getAsDouble());
    }
}
