@Query("SELECT m FROM /Admin_Merchant_Info m WHERE m.mId = $1 AND m.orderExpiryTime > $2")
    MerchantCache findMerchantInfoByMId(@Param("mId") String mId, @Param("minutes") Long minutes);
