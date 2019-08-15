    "Thread-4" daemon prio=5 tid=1130d6000 nid=0x1006d8000 waiting for monitor entry [00000000]
   java.lang.Thread.State: BLOCKED (on object monitor)

"AWT-Shutdown" prio=5 tid=113b1c000 nid=0x11b616000 in Object.wait() [11b615000]
   java.lang.Thread.State: WAITING (on object monitor)
    at java.lang.Object.wait(Native Method)
    - waiting on <7e1f45168> (a java.lang.Object)
    at java.lang.Object.wait(Object.java:485)
    at sun.awt.AWTAutoShutdown.run(AWTAutoShutdown.java:265)
    - locked <7e1f45168> (a java.lang.Object)
    at java.lang.Thread.run(Thread.java:680)

"AWT-AppKit" daemon prio=5 tid=1139db000 nid=0x7fff70596cc0 waiting for monitor entry [00000000]
   java.lang.Thread.State: BLOCKED (on object monitor)

"Thread-1" prio=5 tid=113962000 nid=0x11b293000 waiting for monitor entry [11b290000]
   java.lang.Thread.State: BLOCKED (on object monitor)
    at java.lang.Runtime.loadLibrary0(Runtime.java:815)
    - waiting to lock <7e1e04f80> (a java.lang.Runtime)
    at java.lang.System.loadLibrary(System.java:1045)
    at sun.security.pkcs11.wrapper.PKCS11$1.run(PKCS11.java:88)
    at java.security.AccessController.doPrivileged(Native Method)
    at sun.security.pkcs11.wrapper.PKCS11.<clinit>(PKCS11.java:86)
    at sun.security.pkcs11.SunPKCS11.<init>(SunPKCS11.java:281)
    at sun.security.pkcs11.SunPKCS11.<init>(SunPKCS11.java:86)
    at sun.reflect.NativeConstructorAccessorImpl.newInstance0(Native Method)
    at sun.reflect.NativeConstructorAccessorImpl.newInstance(NativeConstructorAccessorImpl.java:39)
    at sun.reflect.DelegatingConstructorAccessorImpl.newInstance(DelegatingConstructorAccessorImpl.java:27)
    at java.lang.reflect.Constructor.newInstance(Constructor.java:513)
    at sun.security.jca.ProviderConfig$4.run(ProviderConfig.java:262)
    at java.security.AccessController.doPrivileged(Native Method)
    at sun.security.jca.ProviderConfig.doLoadProvider(ProviderConfig.java:244)
    at sun.security.jca.ProviderConfig.getProvider(ProviderConfig.java:224)
    - locked <7e1e3a020> (a sun.misc.Launcher$AppClassLoader)
    at sun.security.jca.ProviderList.getProvider(ProviderList.java:215)
    at sun.security.jca.ProviderList.getService(ProviderList.java:313)
    at sun.security.jca.GetInstance.getInstance(GetInstance.java:140)
    at java.security.Security.getImpl(Security.java:659)
    at java.security.MessageDigest.getInstance(MessageDigest.java:129)
    at pl.gizarma.starter2.util.MD5.getMD5Bytes(MD5.java:23)
    - locked <7fb495f30> (a java.lang.Class for pl.gizarma.starter2.util.MD5)
    at pl.gizarma.starter2.util.MD5.fromBytes(MD5.java:14)
    at pl.gizarma.starter2.util.MD5.fromString(MD5.java:9)
    at pl.gizarma.starter2.net.UrlCache.getUrl(UrlCache.java:69)
    - locked <7e1fb2600> (a pl.gizarma.starter2.net.UrlCache)
    at pl.gizarma.starter2.net.UrlCache.getUrl(UrlCache.java:61)
    at pl.gizarma.starter2.net.RetryingUrlReader.readUseCache(RetryingUrlReader.java:75)
    at pl.gizarma.starter2.net.RetryingUrlReader.read(RetryingUrlReader.java:60)
    at pl.gizarma.starter2.net.UpdateDownloader.download(UpdateDownloader.java:29)
    at pl.gizarma.starter2.job.UpdateJob.run(UpdateJob.java:20)
    at pl.gizarma.starter2.Starter2Main$1.run(Starter2Main.java:51)

"Low Memory Detector" daemon prio=5 tid=113035000 nid=0x11af39000 runnable [00000000]
   java.lang.Thread.State: RUNNABLE

"C2 CompilerThread1" daemon prio=9 tid=113034800 nid=0x11ae36000 waiting on condition [00000000]
   java.lang.Thread.State: RUNNABLE

"C2 CompilerThread0" daemon prio=9 tid=113033800 nid=0x11ad33000 waiting on condition [00000000]
   java.lang.Thread.State: RUNNABLE

"Signal Dispatcher" daemon prio=9 tid=113033000 nid=0x11ac30000 waiting on condition [00000000]
   java.lang.Thread.State: RUNNABLE

"Surrogate Locker Thread (Concurrent GC)" daemon prio=5 tid=113032000 nid=0x11ab2d000 waiting on condition [00000000]
   java.lang.Thread.State: RUNNABLE

"Finalizer" daemon prio=8 tid=11387d000 nid=0x11aa2a000 in Object.wait() [11aa29000]
   java.lang.Thread.State: WAITING (on object monitor)
    at java.lang.Object.wait(Native Method)
    - waiting on <7e1e01300> (a java.lang.ref.ReferenceQueue$Lock)
    at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:118)
    - locked <7e1e01300> (a java.lang.ref.ReferenceQueue$Lock)
    at java.lang.ref.ReferenceQueue.remove(ReferenceQueue.java:134)
    at java.lang.ref.Finalizer$FinalizerThread.run(Finalizer.java:171)

"Reference Handler" daemon prio=10 tid=113029000 nid=0x11a7cb000 in Object.wait() [11a7ca000]
   java.lang.Thread.State: WAITING (on object monitor)
    at java.lang.Object.wait(Native Method)
    - waiting on <7e1e011d8> (a java.lang.ref.Reference$Lock)
    at java.lang.Object.wait(Object.java:485)
    at java.lang.ref.Reference$ReferenceHandler.run(Reference.java:116)
    - locked <7e1e011d8> (a java.lang.ref.Reference$Lock)

"main" prio=5 tid=113800800 nid=0x1017fb000 runnable [1017f8000]
   java.lang.Thread.State: RUNNABLE
    at java.lang.ClassLoader$NativeLibrary.load(Native Method)
    at java.lang.ClassLoader.loadLibrary0(ClassLoader.java:1827)
    - locked <7e1e010a8> (a java.util.Vector)
    - locked <7e1e01100> (a java.util.Vector)
    at java.lang.ClassLoader.loadLibrary(ClassLoader.java:1724)
    at java.lang.Runtime.loadLibrary0(Runtime.java:823)
    - locked <7e1e04f80> (a java.lang.Runtime)
    at java.lang.System.loadLibrary(System.java:1045)
    at sun.security.action.LoadLibraryAction.run(LoadLibraryAction.java:50)
    at java.security.AccessController.doPrivileged(Native Method)
    at sun.awt.NativeLibLoader.loadLibraries(NativeLibLoader.java:38)
    at sun.awt.DebugHelper.<clinit>(DebugHelper.java:29)
    at java.awt.Component.<clinit>(Component.java:566)
    at pl.gizarma.starter2.Starter2Main.prepareMainFrame(Starter2Main.java:23)
    at pl.gizarma.starter2.Starter2Main.main(Starter2Main.java:17)

"VM Thread" prio=9 tid=113024800 nid=0x11a927000 runnable 

"Gang worker#0 (Parallel GC Threads)" prio=9 tid=113802000 nid=0x112dc4000 runnable 

"Gang worker#1 (Parallel GC Threads)" prio=9 tid=113802800 nid=0x112ec7000 runnable 

"Concurrent Mark-Sweep GC Thread" prio=9 tid=11384d000 nid=0x11a498000 runnable 
"VM Periodic Task Thread" prio=10 tid=113897000 nid=0x11b03c000 waiting on condition 

"Exception Catcher Thread" prio=10 tid=113801800 nid=0x112c01000 runnable 
JNI global references: 1004

Heap
 par new generation   total 19136K, used 3772K [7e1e00000, 7e32c0000, 7e4790000)
  eden space 17024K,  22% used [7e1e00000, 7e21af1b8, 7e2ea0000)
  from space 2112K,   0% used [7e2ea0000, 7e2ea0000, 7e30b0000)
  to   space 2112K,   0% used [7e30b0000, 7e30b0000, 7e32c0000)
 concurrent mark-sweep generation total 63872K, used 0K [7e4790000, 7e85f0000, 7fae00000)
 concurrent-mark-sweep perm gen total 21248K, used 7162K [7fae00000, 7fc2c0000, 800000000)
