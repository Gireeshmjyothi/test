private void validateExistsByOrderRefNumber(String orderRefNumber, MerchantInfoDTO merchantInfoDTO) {
        if(merchantInfoDTO.getMerchantVolVelFlag().equalsIgnoreCase("N") && transactionDao.getSbiOrderRefNumberCountByOrderRefNumber(orderRefNumber) <= 6){
            return;
        }else{
            if (orderDao.existsByOrderRefNumber(orderRefNumber)) {
                logger.info("validateOrderRequest, Validate Order orderRefNumber :"+orderRefNumber);
                addError("OrderRefNumber", OrderErrorConstant.ALREADY_EXIST_ERROR_CODE, OrderErrorConstant.ALREADY_EXIST_ERROR_MESSAGE);
            }
        }
        throwIfErrors();
    }
