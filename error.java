/**
     * This method is used to truncate stage table.
     */
    public void clearStageTable() {
        jdbcTemplate.update("TRUNCATE TABLE RECON_STATUS_STAGE");
    }
