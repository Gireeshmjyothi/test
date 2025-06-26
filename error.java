/**
 * This method deletes data from RECON_STATUS_STAGE table by rfsId.
 *
 * @param rfsId the identifier to delete records by
 */
public void clearStageTableByRfsId(String rfsId) {
    jdbcTemplate.update("DELETE FROM RECON_STATUS_STAGE WHERE rfs_id = ?", rfsId);
}
