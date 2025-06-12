Dataset<Row> inputDataset = ...; // Your original dataset

inputDataset
    .withColumn("STATUS", functions.lit(status))
    .selectExpr(
        "upper(hex(RFD_ID)) AS key",
        "to_json(named_struct(" +
            "'RFD_ID', upper(hex(RFD_ID)), " +
            "'ATRN_NUM', ATRN_NUM, " +
            "'STATUS', STATUS" +
        ")) AS value"
    )
    .writeStream()
    .format("kafka")
    .option("kafka.bootstrap.servers", "your-kafka:9092")
    .option("topic", topic)
    .option("checkpointLocation", "/path/to/kafka/checkpoint") // required
    .outputMode("append")
    .start()
    .awaitTermination();
