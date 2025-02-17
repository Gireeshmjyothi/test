Caused by: java.lang.IllegalArgumentException: Validation failed for query for method public abstract java.util.Optional com.epay.payment.repository.TransactionRepository.fetchTransactionAndOrderDetail(java.lang.String,java.lang.String)
	at org.springframework.data.jpa.repository.query.SimpleJpaQuery.validateQuery(SimpleJpaQuery.java:100)
	at org.springframework.data.jpa.repository.query.SimpleJpaQuery.<init>(SimpleJpaQuery.java:70)
	at org.springframework.data.jpa.repository.query.JpaQueryFactory.fromMethodWithQueryString(JpaQueryFactory.java:60)
	at org.springframework.data.jpa.repository.query.JpaQueryLookupStrategy$DeclaredQueryLookupStrategy.resolveQuery(JpaQueryLookupStrategy.java:170)
	at org.springframework.data.jpa.repository.query.JpaQueryLookupStrategy$CreateIfNotFoundQueryLookupStrategy.resolveQuery(JpaQueryLookupStrategy.java:252)
	at org.springframework.data.jpa.repository.query.JpaQueryLookupStrategy$AbstractQueryLookupStrategy.resolveQuery(JpaQueryLookupStrategy.java:95)
	at org.springframework.data.repository.core.support.QueryExecutorMethodInterceptor.lookupQuery(QueryExecutorMethodInterceptor.java:115)
	... 84 common frames omitted
Caused by: java.lang.IllegalArgumentException: org.hibernate.query.SemanticException: Operand of 'like' is of type 'java.lang.Object' which is not a string (its JDBC type code is not string-like)
	at org.hibernate.internal.ExceptionConverterImpl.convert(ExceptionConverterImpl.java:143)
	at org.hibernate.internal.ExceptionConverterImpl.convert(ExceptionConverterImpl.java:167)
	at org.hibernate.internal.ExceptionConverterImpl.convert(ExceptionConverterImpl.java:173)
	at org.hibernate.internal.AbstractSharedSessionContract.createQuery(AbstractSharedSessionContract.java:860)
	at org.hibernate.internal.AbstractSharedSessionContract.createQuery(AbstractSharedSessionContract.java:765)
	at org.hibernate.internal.AbstractSharedSessionContract.createQuery(AbstractSharedSessionContract.java:140)
	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)
	at java.base/java.lang.reflect.Method.invoke(Method.java:580)
	at org.springframework.orm.jpa.ExtendedEntityManagerCreator$ExtendedEntityManagerInvocationHandler.invoke(ExtendedEntityManagerCreator.java:364)
	at jdk.proxy2/jdk.proxy2.$Proxy173.createQuery(Unknown Source)
	at org.springframework.data.jpa.repository.query.SimpleJpaQuery.validateQuery(SimpleJpaQuery.java:94)
	... 90 common frames omitted
Caused by: org.hibernate.query.SemanticException: Operand of 'like' is of type 'java.lang.Object' which is not a string (its JDBC type code is not string-like)



	
@Query(value = """
                SELECT t, o
                FROM Transaction t
                INNER JOIN Order o
                ON t.orderRefNumber = o.orderRefNumber
                WHERE t.atrnNum = :atrnNumber
                AND CAST(t.pushResponse AS TEXT) LIKE %:pushStatus%
            """)
Optional<List<Object[]>> fetchTransactionAndOrderDetail(@Param("atrnNumber") String atrnNumber, @Param("pushStatus") String pushStatus);
