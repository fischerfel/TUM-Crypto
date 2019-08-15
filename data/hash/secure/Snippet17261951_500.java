Exception in thread "main" org.bouncycastle.cms.CMSException: can't create digest calculator: exception on setup: java.security.NoSuchAlgorithmException: no such algorithm: SHA1WITHRSA for provider BC
    at org.bouncycastle.cms.SignerInformation.doVerify(Unknown Source)
    at org.bouncycastle.cms.SignerInformation.verify(Unknown Source)
    at PKCS7Signer.verifypkcs7_4(PKCS7Signer.java:281)
    at PKCS7Signer.main(PKCS7Signer.java:170)
Caused by: org.bouncycastle.operator.OperatorCreationException: exception on setup: java.security.NoSuchAlgorithmException: no such algorithm: SHA1WITHRSA for provider BC
    at org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder$1.get(Unknown Source)
    at org.bouncycastle.cms.SignerInformationVerifier.getDigestCalculator(Unknown Source)
    ... 4 more
Caused by: java.security.NoSuchAlgorithmException: no such algorithm: SHA1WITHRSA for provider BC
    at sun.security.jca.GetInstance.getService(Unknown Source)
    at sun.security.jca.GetInstance.getInstance(Unknown Source)
    at java.security.Security.getImpl(Unknown Source)
    at java.security.MessageDigest.getInstance(Unknown Source)
    at org.bouncycastle.jcajce.NamedJcaJceHelper.createDigest(Unknown Source)
    at org.bouncycastle.operator.jcajce.OperatorHelper.createDigest(Unknown Source)
    ... 6 more
