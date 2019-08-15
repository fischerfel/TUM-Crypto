C:\Users\Scepion1d>java -cp C:\Users\Scepion1d\Dropbox\Workspace\IntellijIDEA\pr
ofiler\out\artifacts\loader\loader.jar;C:\Users\Scepion1d\Dropbox\Workspace\Inte
llijIDEA\app\out\production\app -Djava.system.class.loader=CustomClassLoader Main
0
1
2
0
1
2
3
0
1
2
3
java.lang.IllegalArgumentException: Non-positive latency: 0
        at sun.misc.GC$LatencyRequest.<init>(GC.java:190)
        at sun.misc.GC$LatencyRequest.<init>(GC.java:156)
        at sun.misc.GC.requestLatency(GC.java:254)
        at sun.rmi.transport.DGCClient$EndpointEntry.lookup(DGCClient.java:212)
        at sun.rmi.transport.DGCClient.registerRefs(DGCClient.java:120)
        at sun.rmi.transport.ConnectionInputStream.registerRefs(ConnectionInputS
tream.java:80)
        at sun.rmi.transport.StreamRemoteCall.releaseInputStream(StreamRemoteCal
l.java:138)
        at sun.rmi.transport.StreamRemoteCall.done(StreamRemoteCall.java:292)
        at sun.rmi.server.UnicastRef.done(UnicastRef.java:431)
        at sun.rmi.registry.RegistryImpl_Stub.lookup(Unknown Source)
        at CustomClassLoader.initNotifier(CustomClassLoader.java:22)
        at CustomClassLoader.loadClass(CustomClassLoader.java:35)
        at java.lang.ClassLoader.loadClass(ClassLoader.java:247)
        at sun.security.jca.ProviderConfig$3.run(ProviderConfig.java:234)
        at java.security.AccessController.doPrivileged(Native Method)
        at sun.security.jca.ProviderConfig.doLoadProvider(ProviderConfig.java:22
5)
        at sun.security.jca.ProviderConfig.getProvider(ProviderConfig.java:205)
        at sun.security.jca.ProviderList.getProvider(ProviderList.java:215)
        at sun.security.jca.ProviderList.getService(ProviderList.java:313)
        at sun.security.jca.GetInstance.getInstance(GetInstance.java:140)
        at java.security.Security.getImpl(Security.java:659)
        at java.security.MessageDigest.getInstance(MessageDigest.java:129)
        at java.rmi.dgc.VMID.computeAddressHash(VMID.java:140)
        at java.rmi.dgc.VMID.<clinit>(VMID.java:27)
        at sun.rmi.transport.DGCClient.<clinit>(DGCClient.java:66)
        at sun.rmi.transport.ConnectionInputStream.registerRefs(ConnectionInputS
tream.java:80)
        at sun.rmi.transport.StreamRemoteCall.releaseInputStream(StreamRemoteCal
l.java:138)
        at sun.rmi.transport.StreamRemoteCall.done(StreamRemoteCall.java:292)
        at sun.rmi.server.UnicastRef.done(UnicastRef.java:431)
        at sun.rmi.registry.RegistryImpl_Stub.lookup(Unknown Source)
        at CustomClassLoader.initNotifier(CustomClassLoader.java:22)
        at CustomClassLoader.loadClass(CustomClassLoader.java:35)
        at java.lang.ClassLoader.loadClass(ClassLoader.java:247)
        at sun.security.jca.ProviderConfig$3.run(ProviderConfig.java:234)
        at java.security.AccessController.doPrivileged(Native Method)
        at sun.security.jca.ProviderConfig.doLoadProvider(ProviderConfig.java:22
5)
        at sun.security.jca.ProviderConfig.getProvider(ProviderConfig.java:205)
        at sun.security.jca.ProviderList.getProvider(ProviderList.java:215)
        at sun.security.jca.ProviderList$3.get(ProviderList.java:130)
        at sun.security.jca.ProviderList$3.get(ProviderList.java:125)
        at java.util.AbstractList$Itr.next(AbstractList.java:345)
        at java.security.SecureRandom.getPrngAlgorithm(SecureRandom.java:522)
        at java.security.SecureRandom.getDefaultPRNG(SecureRandom.java:165)
        at java.security.SecureRandom.<init>(SecureRandom.java:133)
        at java.rmi.server.UID.<init>(UID.java:92)
        at java.rmi.server.ObjID.<clinit>(ObjID.java:71)
        at java.rmi.registry.LocateRegistry.getRegistry(LocateRegistry.java:158)

        at java.rmi.registry.LocateRegistry.getRegistry(LocateRegistry.java:106)

        at java.rmi.registry.LocateRegistry.getRegistry(LocateRegistry.java:73)
        at CustomClassLoader.initNotifier(CustomClassLoader.java:20)
        at CustomClassLoader.loadClass(CustomClassLoader.java:35)
        at java.lang.ClassLoader.loadClass(ClassLoader.java:247)
