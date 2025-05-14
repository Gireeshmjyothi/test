java.lang.IllegalStateException: LiveListenerBus is stopped.
	at org.apache.spark.scheduler.LiveListenerBus.addToQueue(LiveListenerBus.scala:92)
	at org.apache.spark.scheduler.LiveListenerBus.addToStatusQueue(LiveListenerBus.scala:75)
	at org.apache.spark.sql.internal.SharedState.<init>(SharedState.scala:115)
