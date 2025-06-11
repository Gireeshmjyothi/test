Dataset<Row> kafkaDf = dataset.selectExpr(
    "upper(hex(RFD_ID)) AS key",
    "to_json(named_struct(" +
        "'RFD_ID', upper(hex(RFD_ID)), " +
        "'ATRN_NUM', ATRN_NUM, " +
        "'STATUS', '" + status + "'" +  // dynamically inject status
    ")) AS value"
);
