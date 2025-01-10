public List<Transaction> findTransactions(String mid, Date startDate, Date endDate) {
    // Adjust startDate to the start of the day (00:00:00.000)
    Calendar calStart = Calendar.getInstance();
    calStart.setTime(startDate);
    calStart.set(Calendar.HOUR_OF_DAY, 0);
    calStart.set(Calendar.MINUTE, 0);
    calStart.set(Calendar.SECOND, 0);
    calStart.set(Calendar.MILLISECOND, 0);
    Date adjustedStartDate = calStart.getTime();

    // Adjust endDate to the end of the day (23:59:59.999)
    Calendar calEnd = Calendar.getInstance();
    calEnd.setTime(endDate);
    calEnd.set(Calendar.HOUR_OF_DAY, 23);
    calEnd.set(Calendar.MINUTE, 59);
    calEnd.set(Calendar.SECOND, 59);
    calEnd.set(Calendar.MILLISECOND, 999);
    Date adjustedEndDate = calEnd.getTime();

    // Call repository method with adjusted start and end date
    return transactionRepository.findTransactionsByMidAndDateRange(mid, adjustedStartDate, adjustedEndDate);
}

@Query(value = "SELECT * FROM transaction t " +
               "WHERE t.mid = :mid " +
               "AND t.date >= :startDate " +
               "AND t.date <= :endDate " +
               "ORDER BY t.date", nativeQuery = true)
List<Transaction> findTransactionsByMidAndDateRange(@Param("mid") String mid,
                                                    @Param("startDate") Date startDate,
                                                    @Param("endDate") Date endDate);
