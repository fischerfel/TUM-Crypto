$ /var/lib/jenkins/jobs/android-native-dev/workspace/gradlew assembleRelease
Exception in thread "main" java.lang.RuntimeException: Could not hash input string.
    at org.gradle.wrapper.PathAssembler.getMd5Hash(PathAssembler.java:63)
    at org.gradle.wrapper.PathAssembler.rootDirName(PathAssembler.java:52)
    at org.gradle.wrapper.PathAssembler.getDistribution(PathAssembler.java:45)
    at org.gradle.wrapper.Install.createDist(Install.java:43)
    at org.gradle.wrapper.WrapperExecutor.execute(WrapperExecutor.java:129)
    at org.gradle.wrapper.GradleWrapperMain.main(GradleWrapperMain.java:48)
Caused by: java.security.NoSuchAlgorithmException: MD5 MessageDigest not available
    at sun.security.jca.GetInstance.getInstance(GetInstance.java:159)
    at java.security.Security.getImpl(Security.java:695)
    at java.security.MessageDigest.getInstance(MessageDigest.java:159)
    at org.gradle.wrapper.PathAssembler.getMd5Hash(PathAssembler.java:58)
    ... 5 more
Build step 'Invoke Gradle script' changed build result to FAILURE
