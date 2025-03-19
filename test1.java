 Map<String, List<MerchantOrderPaymentEntity>> groupedByPayMode = merchantOrderPaymentList.stream()
                    .collect(Collectors.groupingBy(MerchantOrderPaymentEntity::getPayMode));

            //List of INB
            List<MerchantOrderPaymentEntity> inbList = groupedByPayMode.getOrDefault(PAY_MODE_INB, List.of());
            inbProducer.publish(UUID.randomUUID().toString(), gatewayPoolingMapper.mapMerchantOrderPaymentEntity(merchantOrderPaymentList));

            //List of otherINB
            List<MerchantOrderPaymentEntity> otherInbList = groupedByPayMode.getOrDefault(PAY_MODE_OTHER_INB, List.of());
