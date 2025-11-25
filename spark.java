private List<EGrasSuccessResponse> buildOrderStatusResponse(List<OrderStatusResponse> orderStatusResponseList) {

    return orderStatusResponseList.stream()
            .flatMap(orderStatus -> {

                OrderInfo orderInfo = orderStatus.getOrderInfo();

                return orderStatus.getPaymentInfoList().stream().map(payment -> 
                        EGrasSuccessResponse.builder()
                                .bid(payment.getBid())                       // From PaymentInfo
                                .prn(orderInfo.getPrn())                     // From OrderInfo
                                .grn(payment.getGrn())                       // From PaymentInfo
                                .amt(payment.getAmount())                    // From PaymentInfo
                                .payStatus(payment.getPayStatus())           // From PaymentInfo
                                .transCompletionDateTime(payment.getTxnDate()) // From PaymentInfo
                                // add more mapping as needed
                                .build()
                );
            })
            .collect(Collectors.toList());
}
