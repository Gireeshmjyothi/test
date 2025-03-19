Map<Boolean, List<MerchantOrderPaymentEntity>> groupedByGatewayMapId = merchantOrderPaymentList.stream()
    .collect(Collectors.partitioningBy(e -> e.getGatewayMapId() == 404));

// List of INB (gatewayMapId == 404)
List<MerchantOrderPaymentEntity> inbList = groupedByGatewayMapId.getOrDefault(true, List.of());
inbProducer.publish(UUID.randomUUID().toString(), gatewayPoolingMapper.mapMerchantOrderPaymentEntity(inbList));

// List of other INB (gatewayMapId != 404)
List<MerchantOrderPaymentEntity> otherInbList = groupedByGatewayMapId.getOrDefault(false, List.of());
