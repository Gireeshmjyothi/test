public interface ReconStatusCountProjection {
    UUID getRfsId();
    String getReconStatus();
    Long getCount();
}

@Query(value = """
    SELECT RFS_ID as rfsId, RECON_STATUS as reconStatus, COUNT(*) as count
    FROM RECON_FILE_DTLS
    WHERE RFS_ID = :rfsId
    GROUP BY RFS_ID, RECON_STATUS
    """, nativeQuery = true)
List<ReconStatusCountProjection> findReconStatusCountByRfsId(@Param("rfsId") UUID rfsId);
