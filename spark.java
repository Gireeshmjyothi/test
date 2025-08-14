 ERROR | com.epay.operations.recon.ReconSparkAppMain:72 | principal=  | scenario=ReconSparkAppMain | operation=main | correlation=85232293-a936-4155-952e-e5e1a0203055 | main | Exception while the recon process, error message: [UNRESOLVED_COLUMN.WITH_SUGGESTION] A column, variable, or function parameter with name `txn`.`MERCHANT_ID` cannot be resolved. Did you mean one of the following? [`MERCHANT_ID`, `txn`.`ATRN_NUM`, `MATCHED_ATRN`, `RECON_STATUS`, `REMARK`]. SQLSTATE: 42703;
'Project [TXN_AMOUNT#42, ATRN_NUM#41, ROW_NUMBER#72L, ATRN_NUM#52, CASE WHEN ((RECON_STATUS#99 = UNMATCHED) AND (REMARK#100 = Debit amount mismatch)) THEN 'txn.MERCHANT_ID ELSE MERCHANT_ID END AS MERCHANT_ID#101, TXN_AMOUNT#56, MULTI_ACCOUNT#55, exactMatch#73, match_rank#74, MATCHED_ATRN#78, isAtrnMatched#98, RECON_STATUS#99, REMARK#100]
+- Project [TXN_AMOUNT#42, ATRN_NUM#41, ROW_NUMBER#72L, ATRN_NUM#52, MERCHANT_ID#77, TXN_AMOUNT#56, MULTI_ACCOUNT#55, exactMatch#73, match_rank#74, MATCHED_ATRN#78, isAtrnMatched#98, RECON_STATUS#99, CASE WHEN ((RECON_STATUS#99 = UNMATCHED) AND isnull(ATRN_NUM#52)) THEN ATRN missing WHEN (((RECON_STATUS#99 = UNMATCHED) AND isnotnull(ATRN_NUM#52)) AND isnull(TXN_AMOUNT#56)) THEN Debit amount mismatch ELSE cast(null as string) END AS REMARK#100]
   +- Project [TXN_AMOUNT#42, ATRN_NUM#41, ROW_NUMBER#72L, ATRN_NUM#52, MERCHANT_ID#77, TXN_AMOUNT#56, MULTI_ACCOUNT#55, exactMatch#73, match_rank#74, MATCHED_ATRN#78, isAtrnMatched#98, CASE WHEN ((exactMatch#73 = 1) AND (match_rank#74 = 1)) THEN MATCHED WHEN ((exactMatch#73 = 1) AND (match_rank#74 > 1)) THEN DUPLICATE WHEN ((exactMatch#73 = 0) AND (isAtrnMatched#98 = true)) THEN DUPLICATE ELSE UNMATCHED END AS RECON_STATUS#99]
      +- Project [TXN_AMOUNT#42, ATRN_NUM#41, ROW_NUMBER#72L, ATRN_NUM#52, MERCHANT_ID#77, TXN_AMOUNT#56, MULTI_ACCOUNT#55, exactMatch#73, match_rank#74, MATCHED_ATRN#78, isnotnull(MATCHED_ATRN#78) AS isAtrnMatched#98]
         +- Join LeftOuter, (ATRN_NUM#41 = MATCHED_ATRN#78)
            :- Project [TXN_AMOUNT#42, ATRN_NUM#41, ROW_NUMBER#72L, ATRN_NUM#52, coalesce(MERCHANT_ID#53, cast(null as string)) AS MERCHANT_ID#77, TXN_AMOUNT#56, MULTI_ACCOUNT#55, exactMatch#73, match_rank#74]
            :  +- Project [TXN_AMOUNT#42, ATRN_NUM#41, ROW_NUMBER#72L, ATRN_NUM#52, MERCHANT_ID#53, TXN_AMOUNT#56, MULTI_ACCOUNT#55, exactMatch#73, match_rank#74]
            :     +- Project [TXN_AMOUNT#42, ATRN_NUM#41, ROW_NUMBER#72L, ATRN_NUM#52, MERCHANT_ID#53, TXN_AMOUNT#56, MULTI_ACCOUNT#55, exactMatch#73, _we0#76, CASE WHEN (exactMatch#73 = 1) THEN _we0#76 END AS match_rank#74]
            :        +- Window [row_number() windowspecdefinition(ATRN_NUM#41, TXN_AMOUNT#42, ROW_NUMBER#72L ASC NULLS FIRST, specifiedwindowframe(RowFrame, unboundedpreceding$(), currentrow$())) AS _we0#76], [ATRN_NUM#41, TXN_AMOUNT#42], [ROW_NUMBER#72L ASC NULLS FIRST]
            :           +- Project [TXN_AMOUNT#42, ATRN_NUM#41, ROW_NUMBER#72L, ATRN_NUM#52, MERCHANT_ID#53, TXN_AMOUNT#56, MULTI_ACCOUNT#55, exactMatch#73]
            :              +- Project [TXN_AMOUNT#42, ATRN_NUM#41, ROW_NUMBER#72L, ATRN_NUM#52, MERCHANT_ID#53, TXN_AMOUNT#56, MULTI_ACCOUNT#55, CASE WHEN isnotnull(ATRN_NUM#52) THEN 1 ELSE 0 END AS exactMatch#73]
            :                 +- Join LeftOuter, ((ATRN_NUM#41 = ATRN_NUM#52) AND (TXN_AMOUNT#42 = cast(TXN_AMOUNT#56 as double)))
            :                    :- SubqueryAlias recon
            :                    :  +- Project [TXN_AMOUNT#42, ATRN_NUM#41, monotonically_increasing_id() AS ROW_NUMBER#72L]
            :                    :     +- Project [paymentamount#39 AS TXN_AMOUNT#42, ATRN_NUM#41]
            :                    :        +- Project [paymentamount#39, atrn#40 AS ATRN_NUM#41]
            :                    :           +- Project [_c2#19 AS paymentamount#39, _c1#18 AS atrn#40]
            :                    :              +- Relation [_c0#17,_c1#18,_c2#19,_c3#20] csv
            :                    +- SubqueryAlias txn
            :                       +- Project [ATRN_NUM#52, MERCHANT_ID#53, trim(cast(TXN_AMOUNT#54 as string), None) AS TXN_AMOUNT#56, MULTI_ACCOUNT#55]
            :                          +- Relation [ATRN_NUM#52,MERCHANT_ID#53,TXN_AMOUNT#54,MULTI_ACCOUNT#55] JDBCRelation(MERCHANT_TXN) [numPartitions=1]
            +- Deduplicate [MATCHED_ATRN#78]
               +- Project [ATRN_NUM#85 AS MATCHED_ATRN#78]
                  +- Filter (match_rank#95 = 1)
                     +- Project [TXN_AMOUNT#86, ATRN_NUM#85, ROW_NUMBER#87L, ATRN_NUM#88, coalesce(MERCHANT_ID#89, cast(null as string)) AS MERCHANT_ID#96, TXN_AMOUNT#92, MULTI_ACCOUNT#91, exactMatch#93, match_rank#95]
                        +- Project [TXN_AMOUNT#86, ATRN_NUM#85, ROW_NUMBER#87L, ATRN_NUM#88, MERCHANT_ID#89, TXN_AMOUNT#92, MULTI_ACCOUNT#91, exactMatch#93, match_rank#95]
                           +- Project [TXN_AMOUNT#86, ATRN_NUM#85, ROW_NUMBER#87L, ATRN_NUM#88, MERCHANT_ID#89, TXN_AMOUNT#92, MULTI_ACCOUNT#91, exactMatch#93, _we0#94, CASE WHEN (exactMatch#93 = 1) THEN _we0#94 END AS match_rank#95]
                              +- Window [row_number() windowspecdefinition(ATRN_NUM#85, TXN_AMOUNT#86, ROW_NUMBER#87L ASC NULLS FIRST, specifiedwindowframe(RowFrame, unboundedpreceding$(), currentrow$())) AS _we0#94], [ATRN_NUM#85, TXN_AMOUNT#86], [ROW_NUMBER#87L ASC NULLS FIRST]
                                 +- Project [TXN_AMOUNT#86, ATRN_NUM#85, ROW_NUMBER#87L, ATRN_NUM#88, MERCHANT_ID#89, TXN_AMOUNT#92, MULTI_ACCOUNT#91, exactMatch#93]
                                    +- Project [TXN_AMOUNT#86, ATRN_NUM#85, ROW_NUMBER#87L, ATRN_NUM#88, MERCHANT_ID#89, TXN_AMOUNT#92, MULTI_ACCOUNT#91, CASE WHEN isnotnull(ATRN_NUM#88) THEN 1 ELSE 0 END AS exactMatch#93]
                                       +- Join LeftOuter, ((ATRN_NUM#85 = ATRN_NUM#88) AND (TXN_AMOUNT#86 = cast(TXN_AMOUNT#92 as double)))
                                          :- SubqueryAlias recon
                                          :  +- Project [TXN_AMOUNT#86, ATRN_NUM#85, monotonically_increasing_id() AS ROW_NUMBER#87L]
                                          :     +- Project [paymentamount#83 AS TXN_AMOUNT#86, ATRN_NUM#85]
                                          :        +- Project [paymentamount#83, atrn#84 AS ATRN_NUM#85]
                                          :           +- Project [_c2#81 AS paymentamount#83, _c1#80 AS atrn#84]
                                          :              +- Relation [_c0#79,_c1#80,_c2#81,_c3#82] csv
                                          +- SubqueryAlias txn
                                             +- Project [ATRN_NUM#88, MERCHANT_ID#89, trim(cast(TXN_AMOUNT#90 as string), None) AS TXN_AMOUNT#92, MULTI_ACCOUNT#91]
                                                +- Relation [ATRN_NUM#88,MERCHANT_ID#89,TXN_AMOUNT#90,MULTI_ACCOUNT#91] JDBCRelation(MERCHANT_TXN) [numPartitions=1]
