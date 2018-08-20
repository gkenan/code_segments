package com.personal.segments.util;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * jackson 工具类
 *
 */
public class JsonUtils {

	private JsonUtils() {

	}

	private static final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 将json字符串转为给定的java对象
	 * 
	 * @param jsonStr
	 * @param valueType
	 * @return
	 * @throws IOException
	 */
	public static <T> T readValue(String jsonStr, Class<T> valueType) throws IOException {
		return objectMapper.readValue(jsonStr, valueType);
	}

	/**
	 * 将java对象转为json字符串
	 * 
	 * @param obj
	 * @return
	 * @throws JsonProcessingException
	 */
	public static String toJson(Object obj) throws JsonProcessingException {
		return objectMapper.writeValueAsString(obj);
	}

	/**
	 * 获取泛型的JavaType
	 * 
	 * @param collectionClass
	 * @param elementClasses
	 * @return
	 */
	public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
		return objectMapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
	}

	/**
	 * 将json字符串转为Map
	 * 
	 * @param jsonStr
	 * @param keyClass
	 *            key具体的类型
	 * @param valueClass
	 *            value具体的类型
	 * @return
	 * @throws IOException
	 */
	public static <K, V> Map<K, V> toMap(String jsonStr, Class<K> keyClass, Class<V> valueClass) throws IOException {
		JavaType collectionType = getCollectionType(Map.class, keyClass, valueClass);
		return objectMapper.readValue(jsonStr, collectionType);
	}

	/**
	 * 将json字符串转为List
	 * 
	 * @param jsonStr
	 * @param elementClass
	 *            List元素的具体类型
	 * @return
	 * @throws IOException
	 */
	public static <T> List<T> toList(String jsonStr, Class<T> elementClass) throws IOException {
		JavaType collectionType = getCollectionType(List.class, elementClass);
		return objectMapper.readValue(jsonStr, collectionType);
	}
	

	/**
	 * 将json字符串转为JsonNode对象
	 * @param data
	 * @return
	 * @throws IOException
	 */
	public static JsonNode readTree(String data) throws IOException {
		return objectMapper.readTree(data);
	}

}
