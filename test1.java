start locator --name=locator
Starting a GemFire Locator in F:\Epay\GemFire_Local_Server\GemFire_Local_Server\vmware-gemfire-10.1.2_torun_gfsh\vmware-gemfire-10.1.2\bin\locator...
The Locator process terminated unexpectedly with exit status 1. Please refer to the log file in F:\Epay\GemFire_Local_Server\GemFire_Local_Server\vmware-gemfire-10.1.2_torun_gfsh\vmware-gemfire-10.1.2\bin\locator for full details.

Exception in thread "main" com.vmware.gemfire.deployment.modules.internal.LauncherException: org.apache.geode.SystemConnectException: Problem starting up membership services
    at gemfire//com.vmware.gemfire.deployment.modules.internal.Launcher.launch(Launcher.java:90)
    at gemfire//com.vmware.gemfire.deployment.modules.internal.Launcher.main(Launcher.java:103)
    at org.jboss.modules.Module.run(Module.java:353)
    at org.jboss.modules.Module.run(Module.java:321)
    at org.jboss.modules.Main.main(Main.java:604)
    at com.vmware.gemfire.bootstrap.internal.Launcher.launch(Launcher.java:64)
    at com.vmware.gemfire.bootstrap.LocatorLauncher.main(LocatorLauncher.java:31)
Caused by: org.apache.geode.SystemConnectException: Problem starting up membership services
    at gemfire//org.apache.geode.distributed.internal.DistributionImpl.start(DistributionImpl.java:258)
    at gemfire//org.apache.geode.distributed.internal.DistributionImpl.createDistribution(DistributionImpl.java:292)
    at gemfire//org.apache.geode.distributed.internal.ClusterDistributionManager.<init>(ClusterDistributionManager.java:491)
    at gemfire//org.apache.geode.distributed.internal.ClusterDistributionManager.<init>(ClusterDistributionManager.java:438)
    at gemfire//org.apache.geode.distributed.internal.ClusterDistributionManager.create(ClusterDistributionManager.java:335)
    at gemfire//org.apache.geode.distributed.internal.InternalDistributedSystem$DefaultClusterDistributionManagerConstructor.create(InternalDistributedSystem.java:3001)
    at gemfire//org.apache.geode.distributed.internal.InternalDistributedSystem.initialize(InternalDistributedSystem.java:753)
    at gemfire//org.apache.geode.distributed.internal.InternalDistributedSystem$Builder.build(InternalDistributedSystem.java:3055)
    at gemfire//org.apache.geode.distributed.internal.InternalDistributedSystem.connectInternal(InternalDistributedSystem.java:289)
    at gemfire//org.apache.geode.distributed.internal.InternalLocator.startDistributedSystem(InternalLocator.java:747)
    at gemfire//org.apache.geode.distributed.internal.InternalLocator.startLocator(InternalLocator.java:411)
    at gemfire//org.apache.geode.distributed.LocatorLauncher.start(LocatorLauncher.java:786)
    at gemfire//org.apache.geode.distributed.LocatorLauncher.run(LocatorLauncher.java:692)
    at gemfire//org.apache.geode.distributed.LocatorLauncher.main(LocatorLauncher.java:229)
    at gemfire//com.vmware.gemfire.deployment.modules.internal.Launcher.invokeMainClass(Launcher.java:99)
    at gemfire//com.vmware.gemfire.deployment.modules.internal.Launcher.launch(Launcher.java:88)
    ... 6 more
Caused by: org.apache.geode.distributed.internal.membership.api.MemberStartupException: Unable to bind to port in range [41000,61000]
    at gemfire//org.apache.geode.distributed.internal.membership.gms.messenger.MultiprotocolMessenger.start(MultiprotocolMessenger.java:172)
    at gemfire//org.apache.geode.distributed.internal.membership.gms.Services.start(Services.java:260)
    at gemfire//org.apache.geode.distributed.internal.membership.gms.GMSMembership.start(GMSMembership.java:1620)
    at gemfire//org.apache.geode.distributed.internal.DistributionImpl.start(DistributionImpl.java:243)
