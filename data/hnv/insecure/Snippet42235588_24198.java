    FileInputStream instream1=null;
    FileInputStream instream2=null;
    KeyStore trustStore=null;
    KeyStore keyStore=null;


    instream1 = new FileInputStream(new File(keystore)); 
    keyStore = KeyStore.getInstance("PKCS12"); 
    keyStore.load(instream1, keystorepwd.toCharArray()); 

    instream2 = new FileInputStream (new File(truststore)); 
    trustStore = KeyStore.getInstance("jks"); 
    trustStore.load(instream2, truststorepwd.toCharArray()); 

    X509HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
    org.apache.http.conn.ssl.SSLSocketFactory lSchemeSocketFactory=null;
    lSchemeSocketFactory = new org.apache.http.conn.ssl.SSLSocketFactory(keyStore, keystorepwd);
    lSchemeSocketFactory.setHostnameVerifier(hostnameVerifier);
    RestAssured.config = RestAssured.config().sslConfig(new SSLConfig().with().sslSocketFactory(lSchemeSocketFactory).and().allowAllHostnames());

    response = RestAssured.given()
            .relaxedHTTPSValidation()
            .contentType("application/json")
            .header("Accept-Encoding","gzip,deflate")
            .body(\\body)
  .post()
    .then().log().all()
    .assertThat().statusCode(201)
    .assertThat().extract().response();
