package com.finki.ukim.mk.backend.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class LlmParametersUtils {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final TypeReference<Map<String, Object>> MAP_TYPE_REF = new TypeReference<>() {
  };

  public static String serialize(Map<String, Object> parameters) {
    if (parameters == null || parameters.isEmpty()) {
      return "{}";
    }

    try {
      return OBJECT_MAPPER.writeValueAsString(parameters);
    } catch (JsonProcessingException e) {
      log.error("Failed to serialize LLM parameters: {}", parameters, e);
      return "{}";
    }
  }

  public static Map<String, Object> deserialize(String jsonParams) {
    if (jsonParams == null || jsonParams.trim().isEmpty()) {
      return new HashMap<>();
    }

    try {
      return OBJECT_MAPPER.readValue(jsonParams, MAP_TYPE_REF);
    } catch (JsonProcessingException e) {
      log.error("Failed to deserialize LLM parameters: {}", jsonParams, e);
      return new HashMap<>();
    }
  }

  public static Optional<Double> getDoubleParameter(Map<String, Object> parameters, String key) {
    if (parameters == null || key == null) {
      return Optional.empty();
    }

    Object value = parameters.get(key);
    if (value == null) {
      return Optional.empty();
    }

    try {
      if (value instanceof Number number) {
        return Optional.of(number.doubleValue());
      } else if (value instanceof String stringValue) {
        return Optional.of(Double.parseDouble(stringValue));
      }
    } catch (NumberFormatException e) {
      log.warn("Invalid double parameter value for key '{}': {}", key, value);
    }

    return Optional.empty();
  }

  public static Optional<Integer> getIntParameter(Map<String, Object> parameters, String key) {
    if (parameters == null || key == null) {
      return Optional.empty();
    }

    Object value = parameters.get(key);
    if (value == null) {
      return Optional.empty();
    }

    try {
      if (value instanceof Number number) {
        return Optional.of(number.intValue());
      } else if (value instanceof String stringValue) {
        return Optional.of(Integer.parseInt(stringValue));
      }
    } catch (NumberFormatException e) {
      log.warn("Invalid integer parameter value for key '{}': {}", key, value);
    }

    return Optional.empty();
  }

  public static Optional<String> getStringParameter(Map<String, Object> parameters, String key) {
    if (parameters == null || key == null) {
      return Optional.empty();
    }

    Object value = parameters.get(key);
    return value != null ? Optional.of(value.toString()) : Optional.empty();
  }

  public static Optional<Boolean> getBooleanParameter(Map<String, Object> parameters, String key) {
    if (parameters == null || key == null) {
      return Optional.empty();
    }

    Object value = parameters.get(key);
    return switch (value) {
      case Boolean booleanValue -> Optional.of(booleanValue);
      case String stringValue -> Optional.of(Boolean.parseBoolean(stringValue));
      case null, default -> Optional.empty();
    };

  }

  public static ParameterBuilder builder() {
    return new ParameterBuilder();
  }


  public static class ParameterBuilder {
    private final Map<String, Object> parameters = new HashMap<>();

    public ParameterBuilder temperature(Double temperature) {
      if (temperature != null) {
        parameters.put("temperature", temperature);
      }
      return this;
    }

    public ParameterBuilder topP(Double topP) {
      if (topP != null) {
        parameters.put("topP", topP);
      }
      return this;
    }

    public ParameterBuilder maxTokens(Integer maxTokens) {
      if (maxTokens != null) {
        parameters.put("maxTokens", maxTokens);
      }
      return this;
    }

    public ParameterBuilder parameter(String key, Object value) {
      if (key != null && value != null) {
        parameters.put(key, value);
      }
      return this;
    }

    public Map<String, Object> build() {
      return new HashMap<>(parameters);
    }

    public String buildAsJson() {
      return serialize(parameters);
    }
  }
}
