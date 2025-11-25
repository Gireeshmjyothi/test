private List<EGrasSuccessResponse> buildOrderStatusResponse(List<OrderStatusResponse> orderStatusResponseList){
        List<EGrasSuccessResponse> eGrasSuccessResponseList = // Map data and return list<egrassuccessresponse>
        return null;
    }


@Data
@Builder
public class OrderStatusResponse {
    private OrderInfo orderInfo;
    private List<PaymentInfo> paymentInfoList;
}


@Data
@Builder
public class EGrasSuccessResponse {
    @JsonProperty("BID")
    private String bid;

    @JsonProperty("PRN")
    private String prn;

    @JsonProperty("GRN")
    private String grn;

    @JsonProperty("AMT")
    private Double amt;

    @JsonProperty("PAYSTATUS")
    private Character payStatus;

    @JsonProperty("TransCompletionDateTime")
    private String transCompletionDateTime;
}
