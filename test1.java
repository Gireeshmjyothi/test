@Transactional
@Modifying
@Query("UPDATE Transaction t " +
       "SET t.transactionStatus = 'FAILED' " +
       "WHERE t.transactionStatus = 'BOOKED' " +
       "AND t.paymentStatus = 'PAYMENT_INITIATED_START' " +
       "AND t.sbiOrderRefNumber IN :sbiOrderRefNumbers")
void updateTransactionStatusToFailed(@Param("sbiOrderRefNumbers") List<String> sbiOrderRefNumbers);

@Transactional
@Modifying
@Query("UPDATE Orders o " +
       "SET o.status = 'FAILED' " +
       "WHERE o.status = 'ATTEMPTED' " +
       "AND o.sbiOrderRefNumber IN :sbiOrderRefNumbers " +
       "AND EXISTS (SELECT 1 FROM Transaction t WHERE t.sbiOrderRefNumber = o.sbiOrderRefNumber " +
       "AND t.transactionStatus = 'BOOKED' AND t.paymentStatus = 'PAYMENT_INITIATED_START')")
void updateOrderStatusToFailed(@Param("sbiOrderRefNumbers") List<String> sbiOrderRefNumbers);



@Scheduled(cron = "${scheduler.cron.expression.transaction}")
    @SchedulerLock(name = "updateStatusOfOrderAndTransaction", lockAtLeastFor = "${scheduler.lockAtLeastFor.transaction}", lockAtMostFor = "${scheduler.lockAtMostFor.transaction}")
    public void updateStatusOfOrderAndTransaction() {
        logger.info("Fetching scheduler time.");
        //TODO-Need more clarity on this, currently using current time to get expired token list.
//        SchedulerDto schedulerDto = schedulerDao.findSchedulerByName("updateStatusOfOrderAndTransaction");
        Long currentTimeStamp = Instant.now().toEpochMilli();
        logger.info("Current Time : {}", currentTimeStamp);

        List<TokenAndOrderDto> tokenAndOrderDto = tokenRepository.findTokensByTypeExpiryAndOrderStatus(TokenType.TRANSACTION, currentTimeStamp);
        List<MerchantDVPDto> merchantDVPFlagList = adminServicesClient.getMerchantDVPFlag(tokenAndOrderDto.stream().map(TokenAndOrderDto::getMerchantId).toList());
        tokenAndOrderDto = getFilteredData(merchantDVPFlagList, tokenAndOrderDto);
        transactionDao.updateStatusOfOrderAndTransaction(tokenAndOrderDto);
    }

@Scheduled(cron = "${scheduler.cron.expression.transaction}")
@SchedulerLock(name = "updateStatusOfOrderAndTransaction", lockAtLeastFor = "${scheduler.lockAtLeastFor.transaction}", lockAtMostFor = "${scheduler.lockAtMostFor.transaction}")
public void updateStatusOfOrderAndTransaction() {
    logger.info("Fetching scheduler time.");

    // Fetch the last lock_until time from ShedLock table
    LocalDateTime lastLockUntilTime = schedulerDao.findLockUntilTime("updateStatusOfOrderAndTransaction");
    Long currentTimeStamp = Instant.now().toEpochMilli();
    logger.info("Current Time : {}", currentTimeStamp);

    // Fetch data that needs processing since the last lock_until time
    List<TokenAndOrderDto> tokenAndOrderDto = tokenRepository.findTokensByTypeExpiryAndOrderStatusSince(TokenType.TRANSACTION, lastLockUntilTime);
    List<MerchantDVPDto> merchantDVPFlagList = adminServicesClient.getMerchantDVPFlag(tokenAndOrderDto.stream().map(TokenAndOrderDto::getMerchantId).toList());
    tokenAndOrderDto = getFilteredData(merchantDVPFlagList, tokenAndOrderDto);
    transactionDao.updateStatusOfOrderAndTransaction(tokenAndOrderDto);

    logger.info("Status update completed.");
}
