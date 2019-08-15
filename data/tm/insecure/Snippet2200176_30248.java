...<5 more exceptions above this>
Caused by: sun.security.validator.ValidatorException: No trusted certificate found
        at sun.security.validator.SimpleValidator.buildTrustedChain(SimpleValidator.java:304)
        at sun.security.validator.SimpleValidator.engineValidate(SimpleValidator.java:107)
        at sun.security.validator.Validator.validate(Validator.java:203)
        at com.sun.net.ssl.internal.ssl.X509TrustManagerImpl.checkServerTrusted(X509TrustManagerImpl.java:172)
        at com.sun.net.ssl.internal.ssl.JsseX509TrustManager.checkServerTrusted(SSLContextImpl.java:320)
        at com.sun.net.ssl.internal.ssl.ClientHandshaker.serverCertificate(ClientHandshaker.java:841)
        ... 22 more
