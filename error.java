.withColumn("source_json", functions.to_json(functions.struct(structFromColumnsStartingWith(dataset, "SRC_"))))
.withColumn("recon_json", functions.to_json(functions.struct(structFromColumnsStartingWith(dataset, "TGT_"))))
