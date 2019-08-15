Exception in thread "main" java.lang.ExceptionInInitializerError
Caused by: java.lang.RuntimeException: java.security.NoSuchAlgorithmException: MD8 MessageDigest not available
    at BloomFilter.<clinit>(BloomFilter.java:86)
Caused by: java.security.NoSuchAlgorithmException: MD8 MessageDigest not available
    at sun.security.jca.GetInstance.getInstance(GetInstance.java:142)
    at java.security.Security.getImpl(Security.java:659)
    at java.security.MessageDigest.getInstance(MessageDigest.java:129)
    at BloomFilter.<clinit>(BloomFilter.java:84)
