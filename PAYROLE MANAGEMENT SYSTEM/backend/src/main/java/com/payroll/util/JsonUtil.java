package com.payroll.util;

import com.google.gson.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * JsonUtil - Utility for JSON serialization, deserialization, and HTTP response dispatching.
 */
public class JsonUtil {

    private static final Gson GSON = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .registerTypeAdapter(LocalDate.class, new JsonSerializer<LocalDate>() {
                @Override
                public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
                    return new JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE));
                }
            })
            .registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
                @Override
                public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
                    return LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);
                }
            })
            .registerTypeAdapter(BigDecimal.class, new JsonSerializer<BigDecimal>() {
                @Override
                public JsonElement serialize(BigDecimal src, Type typeOfSrc, JsonSerializationContext context) {
                    return new JsonPrimitive(src.setScale(2, RoundingMode.HALF_UP).toPlainString());
                }
            })
            .create();

    private JsonUtil() {
        // Prevent instantiation
    }

    public static Gson getGson() {
        return GSON;
    }

    /**
     * Reads entire HTTP request body as String.
     */
    public static String readRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    /**
     * Parses request body into a specific class.
     */
    public static <T> T fromJson(HttpServletRequest request, Class<T> classOfT) throws IOException {
        String body = readRequestBody(request);
        return GSON.fromJson(body, classOfT);
    }

    /**
     * Parses JSON string into a specific class.
     */
    public static <T> T fromJson(String json, Class<T> classOfT) {
        return GSON.fromJson(json, classOfT);
    }

    /**
     * Parses JSON string into a generic Type (e.g. Map, List).
     */
    public static <T> T fromJson(String json, Type typeOfT) {
        return GSON.fromJson(json, typeOfT);
    }

    /**
     * Converts any Java object to JSON String.
     */
    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }

    /**
     * Sends a successful JSON response with HTTP 200 (or specified status).
     */
    public static void sendSuccess(HttpServletResponse response, Object data, String message) throws IOException {
        sendJsonResponse(response, HttpServletResponse.SC_OK, true, message, data);
    }

    /**
     * Sends an error JSON response with custom status code and message.
     */
    public static void sendError(HttpServletResponse response, int statusCode, String message) throws IOException {
        sendJsonResponse(response, statusCode, false, message, null);
    }

    /**
     * Sends structured JSON payload.
     */
    public static void sendJsonResponse(HttpServletResponse response, int statusCode, boolean success, String message, Object data) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(statusCode);

        Map<String, Object> payload = new HashMap<>();
        payload.put("success", success);
        payload.put("message", message);
        if (data != null) {
            payload.put("data", data);
        }

        try (PrintWriter writer = response.getWriter()) {
            writer.write(GSON.toJson(payload));
            writer.flush();
        }
    }
}
