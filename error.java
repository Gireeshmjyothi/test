@Query(value = "SELECT * FROM transaction t " +
               "WHERE t.mid = :mid " +
               "AND t.date BETWEEN :startDate AND :endDate " +
               "ORDER BY t.date", nativeQuery = true)
List<Transaction> findTransactionsByMidAndDateRange(@Param("mid") String mid,
                                                    @Param("startDate") Date startDate,
                                                    @Param("endDate") Date endDate);
