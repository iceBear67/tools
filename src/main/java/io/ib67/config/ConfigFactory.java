package io.ib67.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.ib67.util.serialization.SimpleConfig;
import lombok.Builder;
import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Builder
public class ConfigFactory {
    @Builder.Default
    private final Gson serializer = new GsonBuilder().setPrettyPrinting().create();
    @Builder.Default
    private final Path rootDir = Path.of(".");
    private final Map<Class<?>, SimpleConfig<?>> configs = new HashMap<>();
    private final Set<Class<?>> staticConfigClasses = new HashSet<>();
    private final JsonParser JSON_PARSER = new JsonParser(); // for the fucking compatibilities

    @SuppressWarnings("unchecked")
    @SneakyThrows
    public <C> C load(Class<C> configClass) {
        if (!configClass.isAnnotationPresent(Config.class)) {
            throw new IllegalArgumentException("Class must be annotated with @Config");
        }
        Config config = configClass.getAnnotation(Config.class);
        if (config.staticMode()) {
            staticConfigClasses.add(configClass);
            var path = rootDir.resolve(config.value());
            if (!Files.exists(path)) {
                // stay default values.
                Files.createFile(path);
                save(configClass);
                return null;
            }
            initConfigClass(configClass, path);
            return null;
        } else {
            var sc = configs.computeIfAbsent(configClass, c -> new SimpleConfig<C>(rootDir.toFile(), configClass, serializer));
            sc.setConfigFileName(config.value());
            sc.saveDefault();
            sc.reloadConfig();
            return (C) sc.get();
        }

    }

    public void saveAll() {
        configs.values().forEach(SimpleConfig::saveConfig);
        staticConfigClasses.forEach(this::save);
    }

    public void save(Class<?> configClass) {
        if (!staticConfigClasses.contains(configClass)) {
            throw new IllegalArgumentException("This @Config class has not initialized before");
        }
        var config = configClass.getAnnotation(Config.class);
        saveConfigClass(configClass, rootDir.resolve(config.value()));
    }

    public <C> void save(C config) {
        if (!configs.containsKey(config.getClass())) {
            throw new IllegalArgumentException("Config has not initialized before");
        }
        var sc = configs.get(config.getClass());
        sc.setRaw(config);
        sc.saveConfig();
    }

    @SneakyThrows
    private void saveConfigClass(Class<?> configClass, Path pathToConfig) {
        var jo = new JsonObject();
        for (Field field : configClass.getDeclaredFields()) {
            field.setAccessible(true);
            jo.add(field.getName(), serializer.toJsonTree(field.get(null)));
        }
        Files.writeString(pathToConfig, jo.toString());
    }

    @SneakyThrows
    private void initConfigClass(Class<?> configClass, Path pathToConfig) {
        var jo = JSON_PARSER.parse(Files.readString(pathToConfig)).getAsJsonObject();
        for (Field field : configClass.getDeclaredFields()) {
            field.setAccessible(true);
            if (jo.has(field.getName())) {
                field.set(null, serializer.fromJson(jo.get(field.getName()), field.getType()));
            }
        }
    }
}
