Dataset<Row> targetDuplicates = getDuplicates(reconFileDtls, "ATRN_NUM")
    .join(reconFileDtls, "ATRN_NUM")
    .select("tgt.ATRN_NUM", "tgt.ID", /* other specific columns you need */)
    .distinct();
