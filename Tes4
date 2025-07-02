public static String buildQueryForTransaction(UUID rfsId){
        String hexUUID = rfsId.toString().replace("-", "").toUpperCase();
        return String.format("(SELECT m.* FROM MERCHANT_ORDER_PAYMENTS m, RECON_FILE_DTLS r WHERE r.ATRN_NUM = m.ATRN_NUM AND r.RFS_ID = HEXTORAW('%s'))", hexUUID);
    }
