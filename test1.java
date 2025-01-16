@Scheduled(cron = "${scheduler.cron.expression}")
    @SchedulerLock(name = "TransactionService_checkExpiredToken",lockAtLeastFor = "${scheduler.lockAtLeastFor}",lockAtMostFor = "${scheduler.lockAtMostFor}")
    public void checkExpiredToken() {
        logger.info("Token Expiry scheduler start ");
        Long currentTimeStamp = Instant.now().toEpochMilli();
        List<Token> expiredToken = tokenRepository.findByStatusAndTokenExpiryTimeLessThan(TokenStatus.ACTIVE, currentTimeStamp);
        expiredToken.forEach(token->
        {
            tokenDao.saveToken(buildTokenDtoForInvalidate(objectMapper.convertValue(token, TokenDto.class)));
         });
        logger.info("Token Expiry scheduler end ");
    }

    private TokenDto buildTokenDtoForInvalidate(TokenDto tokenDto) {
        tokenDto.setTokenValid(Boolean.FALSE);
        tokenDto.setStatus(TokenStatus.INACTIVE);
        tokenDto.setExpiredAt(System.currentTimeMillis()) ;
        tokenDto.setUpdatedAt(System.currentTimeMillis());
        tokenDto.setUpdatedBy(System.getProperty("user.name"));
        return tokenDto;
    }


@Query(value = """ 
            SELECT new com.epay.admin.dto.MerchantDVPDto(m.merchantId, m.dvpType)
            FROM Merchant m
            WHERE m.merchantId IN :merchantIds
            """)
    Optional<List<MerchantDVPDto>> findByIds(@Param("merchantIds") List<String> merchantIds);
