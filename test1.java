Map<String, Object> map = objectMapper.readValue(message, new TypeReference<Map<String, Object>>() {});
String artnNumber = (String) map.getOrDefault("atrnNumber", "");
String mId = (String) map.getOrDefault("merchantId", "");
