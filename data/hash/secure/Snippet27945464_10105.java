java.lang.ExceptionInInitializerError                                                                                                                         
        at xsbt.boot.Update.settings$lzycompute(Update.scala:79)                                                                                              
        at xsbt.boot.Update.settings(Update.scala:74)                                                                                                         
        at xsbt.boot.Update.ivyLockFile$lzycompute(Update.scala:96)                                                                                           
        at xsbt.boot.Update.apply(Update.scala:103)                                                                                                           
        at xsbt.boot.Launch.update(Launch.scala:355)                                                                                                          
        at xsbt.boot.Launch.xsbt$boot$Launch$$retrieve$1(Launch.scala:210)                                                                                    
        at xsbt.boot.Launch$$anonfun$3.apply(Launch.scala:218)                                                                                                
        at scala.Option.getOrElse(Option.scala:120)                                                                                                           
        at xsbt.boot.Launch.xsbt$boot$Launch$$getAppProvider0(Launch.scala:218)                                                                               
        at xsbt.boot.Launch$$anon$2.call(Launch.scala:198)                                                                                                    
        at xsbt.boot.Locks$GlobalLock.withChannel$1(Locks.scala:98)                                                                                           
        at xsbt.boot.Locks$GlobalLock.xsbt$boot$Locks$GlobalLock$$withChannelRetries$1(Locks.scala:81)                                                        
        at xsbt.boot.Locks$GlobalLock$$anonfun$withFileLock$1.apply(Locks.scala:102)                                                                          
        at xsbt.boot.Using$.withResource(Using.scala:11)                                                                                                      
        at xsbt.boot.Using$.apply(Using.scala:10)                                                                                                             
        at xsbt.boot.Locks$GlobalLock.ignoringDeadlockAvoided(Locks.scala:62)                                                                                 
        at xsbt.boot.Locks$GlobalLock.withLock(Locks.scala:52)                                                                                                
        at xsbt.boot.Locks$.apply0(Locks.scala:31)                                                                                                            
        at xsbt.boot.Locks$.apply(Locks.scala:28)                                                                                                             
        at xsbt.boot.Launch.locked(Launch.scala:240)                                                                                                          
        at xsbt.boot.Launch.app(Launch.scala:149)                                                                                                             
        at xsbt.boot.Launch.app(Launch.scala:147)                                                                                                             
        at xsbt.boot.Launch$.run(Launch.scala:102)                                                                                                            
        at xsbt.boot.Launch$$anonfun$apply$1.apply(Launch.scala:36)                                                                                           
        at xsbt.boot.Launch$.launch(Launch.scala:117)                                                                                                         
        at xsbt.boot.Launch$.apply(Launch.scala:19)                                                                                                           
        at xsbt.boot.Boot$.runImpl(Boot.scala:44)                                                                                                             
        at xsbt.boot.Boot$.main(Boot.scala:20)                                                                                                                
        at xsbt.boot.Boot.main(Boot.scala)                                                                                                                    
Caused by: java.lang.RuntimeException: The SHA1 algorithm is not available in your classpath                                                                  
        at org.apache.ivy.core.cache.DefaultRepositoryCacheManager.<clinit>(DefaultRepositoryCacheManager.java:86)                                            
        ... 29 more                                                                                                                                           
Caused by: java.security.NoSuchAlgorithmException: SHA1 MessageDigest not available                                                                           
        at sun.security.jca.GetInstance.getInstance(GetInstance.java:159)                                                                                     
        at java.security.Security.getImpl(Security.java:695)                                                                                                  
        at java.security.MessageDigest.getInstance(MessageDigest.java:159)                                                                                    
        at org.apache.ivy.core.cache.DefaultRepositoryCacheManager.<clinit>(DefaultRepositoryCacheManager.java:84)                                            
        ... 29 more                                                                                                                                           
Error during sbt execution: java.lang.ExceptionInInitializerError 
