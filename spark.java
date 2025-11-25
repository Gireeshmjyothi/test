private List<EGrasSuccessResponse> buildOrderStatusResponse(List<OrderStatusResponse> orderStatusResponseList) {

    return orderStatusResponseList.stream()
            .flatMap(orderStatus -> orderStatus.getPaymentInfoList().stream()
                    .map(payment -> EGrasSuccessResponse.builder()
                            .bid(payment.getBid())
                            .prn(orderStatus.getOrderInfo().getPrn())
                            .grn(payment.getGrn())
                            .amt(payment.getAmount())
                            .payStatus(payment.getPayStatus())
                            .transCompletionDateTime(payment.getTransactionDateTime())
                            .build()
                    )
            )
            .collect(Collectors.toList());
}
