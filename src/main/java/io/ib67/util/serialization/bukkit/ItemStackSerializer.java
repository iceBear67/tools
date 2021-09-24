package io.ib67.util.serialization.bukkit;

import com.comphenix.protocol.utility.StreamSerializer;
import com.google.gson.*;
import lombok.SneakyThrows;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;

public class ItemStackSerializer implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {
    protected static final StreamSerializer SERIALIZER = new StreamSerializer();

    @SneakyThrows
    @Override
    public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jo = new JsonObject();
        jo.addProperty("item", SERIALIZER.serializeItemStack(src));
        return jo;
    }
    @SneakyThrows
    @Override
    public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return ItemStackSerializer.SERIALIZER.deserializeItemStack(json.getAsJsonObject().get("item").toString());
    }
}
