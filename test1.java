Map<String, Object> map = objectMapper.readValue(message, Map.class);
        String artnNumber = map.get("atrnNumber").toString();
        String mId = map.get("merchantId").toString();
