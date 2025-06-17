Dataset<Row> matchedWithStatus = matched.withColumn("RECON_STATUS", lit("MATCHED"));
        Dataset<Row> unmatchedWithStatus = unmatched.withColumn("RECON_STATUS", lit("UNMATCHED"));
        Dataset<Row> duplicateWithStatus = reconFileDetailDuplicate.withColumn("RECON_STATUS", lit("DUPLICATE"));

        Dataset<Row> matchedConverted = convertRfdIdToBytes(matchedWithStatus);
        Dataset<Row> unmatchedConverted = convertRfdIdToBytes(unmatchedWithStatus);
        Dataset<Row> duplicateConverted = convertRfdIdToBytes(duplicateWithStatus);

        Dataset<Row> finalReconStatusUpdate = matchedConverted
                .union(unmatchedConverted)
                .union(duplicateConverted);

        finalReconStatusUpdate.printSchema();


private Dataset<Row> convertRfdIdToBytes(Dataset<Row> dataset) {
        return dataset.withColumn("RFD_ID", callUDF("uuidToBytes", col("RFD_ID")))
                .select("RFD_ID", "RECON_STATUS");
    }


@Bean
    public SparkSession sparkSession() {
        SparkSession spark = SparkSession.builder()
                .appName(appName)
                .master(master)
                .config("spark.driver.memory", "3g")
                .config("spark.ui.enabled", "false") // disable web UI
                .getOrCreate();
        registerUuidToHexUdf(spark);
        return spark;
    }

    public void registerUuidToHexUdf(SparkSession spark) {
        spark.udf().register("uuidToHex", (UDF1<String, String>) uuidStr -> {
            UUID uuid = UUID.fromString(uuidStr);
            return uuid.toString().replace("-", "").toUpperCase();  // optional: no dashes, uppercase
        }, DataTypes.StringType);
    }


org.apache.spark.sql.AnalysisException: [UNRESOLVED_ROUTINE] Cannot resolve function `uuidToBytes` on search path [`system`.`builtin`, `system`.`session`, `spark_catalog`.`default`].
	at org.apache.spark.sql.errors.QueryCompilationErrors$.unresolvedRoutineError(QueryCompilationErrors.scala:732)
	at org.apache.spark.sql.catalyst.analysis.Analyzer$LookupFunctions$$anonfun$apply$20.applyOrElse(Analyzer.scala:2083)
	at org.apache.spark.sql.catalyst.analysis.Analyzer$LookupFunctions$$anonfun$apply$20.applyOrElse(Analyzer.scala:2066)
	at org.apache.spark.sql.catalyst.trees.TreeNode.$anonfun$transformDownWithPruning$1(TreeNode.scala:461)
	at org.apache.spark.sql.catalyst.trees.CurrentOrigin$.withOrigin(origin.scala:76)
	at org.apache.spark.sql.catalyst.trees.TreeNode.transformDownWithPruning(TreeNode.scala:461)
	at org.apache.spark.sql.catalyst.trees.TreeNode.$anonfun$transformDownWithPruning$3(TreeNode.scala:466)
	at org.apache.spark.sql.catalyst.trees.UnaryLike.mapChildren(TreeNode.scala:1216)
	at org.apache.spark.sql.catalyst.trees.UnaryLike.mapChildren$(TreeNode.scala:1215)
	at org.apache.spark.sql.catalyst.expressions.UnaryExpression.mapChildren(Expression.scala:533)
	at org.apache.spark.sql.catalyst.trees.TreeNode.transformDownWithPruning(TreeNode.scala:466)
	at org.apache.spark.sql.catalyst.plans.QueryPlan.$anonfun$transformExpressionsDownWithPruning$1(QueryPlan.scala:169)
	at org.apache.spark.sql.catalyst.plans.QueryPlan.$anonfun$mapExpressions$1(QueryPlan.scala:210)
	at org.apache.spark.sql.catalyst.trees.CurrentOrigin$.withOrigin(origin.scala:76)
	at org.apache.spark.sql.catalyst.plans.QueryPlan.transformExpression$1(QueryPlan.scala:210)
	at org.apache.spark.sql.catalyst.plans.QueryPlan.recursiveTransform$1(QueryPlan.scala:221)
	at org.apache.spark.sql.catalyst.plans.QueryPlan.$anonfun$mapExpressions$3(QueryPlan.scala:226)
	at scala.collection.TraversableLike.$anonfun$map$1(TraversableLike.scala:286)
	at scala.collection.Iterator.foreach(Iterator.scala:943)
	at scala.collection.Iterator.foreach$(Iterator.scala:943)
	at scala.collection.AbstractIterator.foreach(Iterator.scala:1431)
	at scala.collection.IterableLike.foreach(IterableLike.scala:74)
	at scala.collection.IterableLike.foreach$(IterableLike.scala:73)
	at scala.collection.AbstractIterable.foreach(Iterable.scala:56)
	at scala.collection.TraversableLike.map(TraversableLike.scala:286)
	at scala.collection.TraversableLike.map$(TraversableLike.scala:279)
	at scala.collection.AbstractTraversable.map(Traversable.scala:108)
	at org.apache.spark.sql.catalyst.plans.QueryPlan.recursiveTransform$1(QueryPlan.scala:226)
	at org.apache.spark.sql.catalyst.plans.QueryPlan.$anonfun$mapExpressions$4(QueryPlan.scala:231)
	at org.apache.spark.sql.catalyst.trees.TreeNode.mapProductIterator(TreeNode.scala:304)
	at org.apache.spark.sql.catalyst.plans.QueryPlan.mapExpressions(QueryPlan.scala:231)
	at org.apache.spark.sql.catalyst.plans.QueryPlan.transformExpressionsDownWithPruning(QueryPlan.scala:169)
	at org.apache.spark.sql.catalyst.plans.QueryPlan.transformExpressionsWithPruning(QueryPlan.scala:140)
	at org.apache.spark.sql.catalyst.plans.logical.AnalysisHelper$$anonfun$resolveExpressionsWithPruning$1.applyOrElse(AnalysisHelper.scala:245)
	at org.apache.spark.sql.catalyst.plans.logical.AnalysisHelper$$anonfun$resolveExpressionsWithPruning$1.applyOrElse(AnalysisHelper.scala:244)
	at org.apache.spark.sql.catalyst.plans.logical.AnalysisHelper.$anonfun$resolveOperatorsDownWithPruning$2(AnalysisHelper.scala:170)
	at org.apache.spark.sql.catalyst.trees.CurrentOrigin$.withOrigin(origin.scala:76)
	at org.apache.spark.sql.catalyst.plans.logical.AnalysisHelper.$anonfun$resolveOperatorsDownWithPruning$1(AnalysisHelper.scala:170)
	at org.apache.spark.sql.catalyst.plans.logical.AnalysisHelper$.allowInvokingTransformsInAnalyzer(AnalysisHelper.scala:323)
	at org.apache.spark.sql.catalyst.plans.logical.AnalysisHelper.resolveOperatorsDownWithPruning(AnalysisHelper.scala:168)
	at org.apache.spark.sql.catalyst.plans.logical.AnalysisHelper.resolveOperatorsDownWithPruning$(AnalysisHelper.scala:164)
	at org.apache.spark.sql.catalyst.plans.logical.LogicalPlan.resolveOperatorsDownWithPruning(LogicalPlan.scala:32)
	at org.apache.spark.sql.catalyst.plans.logical.AnalysisHelper.resolveOperatorsWithPruning(AnalysisHelper.scala:99)
	at org.apache.spark.sql.catalyst.plans.logical.AnalysisHelper.resolveOperatorsWithPruning$(AnalysisHelper.scala:96)
	at org.apache.spark.sql.catalyst.plans.logical.LogicalPlan.resolveOperatorsWithPruning(LogicalPlan.scala:32)
	at org.apache.spark.sql.catalyst.plans.logical.AnalysisHelper.resolveExpressionsWithPruning(AnalysisHelper.scala:244)
	at org.apache.spark.sql.catalyst.plans.logical.AnalysisHelper.resolveExpressionsWithPruning$(AnalysisHelper.scala:242)
	at org.apache.spark.sql.catalyst.plans.logical.LogicalPlan.resolveExpressionsWithPruning(LogicalPlan.scala:32)
	at org.apache.spark.sql.catalyst.analysis.Analyzer$LookupFunctions$.apply(Analyzer.scala:2066)
	at org.apache.spark.sql.catalyst.analysis.Analyzer$LookupFunctions$.apply(Analyzer.scala:2062)
	at org.apache.spark.sql.catalyst.rules.RuleExecutor.$anonfun$execute$2(RuleExecutor.scala:222)
	at scala.collection.IndexedSeqOptimized.foldLeft(IndexedSeqOptimized.scala:60)
	at scala.collection.IndexedSeqOptimized.foldLeft$(IndexedSeqOptimized.scala:68)
	at scala.collection.mutable.WrappedArray.foldLeft(WrappedArray.scala:38)
	at org.apache.spark.sql.catalyst.rules.RuleExecutor.$anonfun$execute$1(RuleExecutor.scala:219)
	at org.apache.spark.sql.catalyst.rules.RuleExecutor.$anonfun$execute$1$adapted(RuleExecutor.scala:211)
	at scala.collection.immutable.List.foreach(List.scala:431)
	at org.apache.spark.sql.catalyst.rules.RuleExecutor.execute(RuleExecutor.scala:211)
	at org.apache.spark.sql.catalyst.analysis.Analyzer.org$apache$spark$sql$catalyst$analysis$Analyzer$$executeSameContext(Analyzer.scala:240)
	at org.apache.spark.sql.catalyst.analysis.Analyzer.$anonfun$execute$1(Analyzer.scala:236)
	at org.apache.spark.sql.catalyst.analysis.AnalysisContext$.withNewAnalysisContext(Analyzer.scala:187)
	at org.apache.spark.sql.catalyst.analysis.Analyzer.execute(Analyzer.scala:236)
	at org.apache.spark.sql.catalyst.analysis.Analyzer.execute(Analyzer.scala:202)
	at org.apache.spark.sql.catalyst.rules.RuleExecutor.$anonfun$executeAndTrack$1(RuleExecutor.scala:182)
	at org.apache.spark.sql.catalyst.QueryPlanningTracker$.withTracker(QueryPlanningTracker.scala:89)
	at org.apache.spark.sql.catalyst.rules.RuleExecutor.executeAndTrack(RuleExecutor.scala:182)
	at org.apache.spark.sql.catalyst.analysis.Analyzer.$anonfun$executeAndCheck$1(Analyzer.scala:223)
	at org.apache.spark.sql.catalyst.plans.logical.AnalysisHelper$.markInAnalyzer(AnalysisHelper.scala:330)
	at org.apache.spark.sql.catalyst.analysis.Analyzer.executeAndCheck(Analyzer.scala:222)
	at org.apache.spark.sql.execution.QueryExecution.$anonfun$analyzed$1(QueryExecution.scala:77)
	at org.apache.spark.sql.catalyst.QueryPlanningTracker.measurePhase(QueryPlanningTracker.scala:138)
	at org.apache.spark.sql.execution.QueryExecution.$anonfun$executePhase$2(QueryExecution.scala:219)
	at org.apache.spark.sql.execution.QueryExecution$.withInternalError(QueryExecution.scala:546)
	at org.apache.spark.sql.execution.QueryExecution.$anonfun$executePhase$1(QueryExecution.scala:219)
	at org.apache.spark.sql.SparkSession.withActive(SparkSession.scala:900)
	at org.apache.spark.sql.execution.QueryExecution.executePhase(QueryExecution.scala:218)
	at org.apache.spark.sql.execution.QueryExecution.analyzed$lzycompute(QueryExecution.scala:77)
	at org.apache.spark.sql.execution.QueryExecution.analyzed(QueryExecution.scala:74)
	at org.apache.spark.sql.execution.QueryExecution.assertAnalyzed(QueryExecution.scala:66)
	at org.apache.spark.sql.Dataset$.$anonfun$ofRows$1(Dataset.scala:91)
	at org.apache.spark.sql.SparkSession.withActive(SparkSession.scala:900)
	at org.apache.spark.sql.Dataset$.ofRows(Dataset.scala:89)
	at org.apache.spark.sql.Dataset.withPlan(Dataset.scala:4352)
	at org.apache.spark.sql.Dataset.select(Dataset.scala:1542)
	at org.apache.spark.sql.Dataset.withColumns(Dataset.scala:2783)
	at org.apache.spark.sql.Dataset.withColumn(Dataset.scala:2722)
	at com.epay.rns.service.ReconService.convertRfdIdToBytes(ReconService.java:140)
	at com.epay.rns.service.ReconService.reconProcess(ReconService.java:109)
	at com.epay.rns.controller.SparkController.reconProcess(SparkController.java:38)
	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:103)
	at java.base/java.lang.reflect.Method.invoke(Method.java:580)
	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:255)
	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:188)
	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:118)
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:926)
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:831)
	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)
	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1089)
	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:979)
	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1014)
	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:903)
	at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:564)
	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:885)
	at jakarta.servlet.http.HttpServlet.service(HttpServlet.java:658)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:195)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)
	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:51)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)
	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)
	at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)
	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:116)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:164)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:140)
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:167)
