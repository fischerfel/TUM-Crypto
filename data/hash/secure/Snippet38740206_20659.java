no such algorithm: 1.2.840.113549.1.1.5 for provider BC
java.security.NoSuchAlgorithmException: no such algorithm: 1.2.840.113549.1.1.5 for provider BC
    at sun.security.jca.GetInstance.getService(GetInstance.java:87)
    at sun.security.jca.GetInstance.getInstance(GetInstance.java:206)
    at java.security.Security.getImpl(Security.java:698)
    at java.security.MessageDigest.getInstance(MessageDigest.java:227)
    at com.company.Main.testAddDigest(Main.java:95)
    at com.company.Main.main(Main.java:32)
    at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
    at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
    at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
    at java.lang.reflect.Method.invoke(Method.java:498)
    at com.intellij.rt.execution.application.AppMain.main(AppMain.java:147)
