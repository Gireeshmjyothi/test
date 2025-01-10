@Query("SELECT t FROM Transaction t " +
       "WHERE t.mid = :mid " +
       "AND t.date >= :startDate " +
       "AND t.date <= :endDate " +
       "ORDER BY t.date")
List<Transaction> findTransactionsByMidAndDateRange(@Param("mid") String mid,
                                                    @Param("startDate") Date startDate,
                                                    @Param("endDate") Date endDate);
