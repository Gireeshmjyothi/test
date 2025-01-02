@Query("SELECT t FROM Order t WHERE t.orderHash =:orderHash AND t.status IN ('CREATED')")
    Optional<Order> findActiveOrderByHash(@Param("orderHash") String orderHash);
