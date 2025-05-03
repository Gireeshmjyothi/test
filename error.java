        at java.base/java.lang.Thread.run(Thread.java:1583)
25/05/03 11:35:03 ERROR TransportRequestHandler: Error while invoking RpcHandler#receive() on RPC id 6148087390888286572
java.io.InvalidClassException: org.apache.spark.rpc.netty.RpcEndpointVerifier$CheckExistence; local class incompatible: stream classdesc serialVersionUID = 5378738997755484868, local class serialVersionUID = 7789290765573734431
        at java.base/java.io.ObjectStreamClass.initNonProxy(ObjectStreamClass.java:598)
        at java.base/java.io.ObjectInputStream.readNonProxyDesc(ObjectInputStream.java:2078)
        at java.base/java.io.ObjectInputStream.readClassDesc(ObjectInputStream.java:1927)
        at java.base/java.io.ObjectInputStream.readOrdinaryObject(ObjectInputStream.java:2252)
