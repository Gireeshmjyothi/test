 @Query(value = """
            SELECT RFS_ID, RECON_STATUS, COUNT(*)
            FROM RECON_FILE_DTLS
            WHERE RFS_ID = :rfsId group by RFS_ID, RECON_STATUS
            """, nativeQuery = true)
    Object findReconStatusCountByRfsId(@Param("rfsId") UUID rfsId);
