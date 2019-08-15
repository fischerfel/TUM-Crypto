1. add certificate to truststore.
2.install Java Cryptography Extension (JCE) Unlimited Strength Jurisdiction Policy Files 7.
3.set start Arguments:
  -Dweblogic.security.SSL.protocolVersion=TLS1
  -Dweblogic.security.SSL.minimumProtocolVersion=TLSv1.2
  -Djavax.net.debug=ssl
(and I have enable JSSE SSL)
4. set TLS1.2 in code where have use axis

  context = SSLContext.getInstance("TLSv1.2");
  context.init(null, null, null);
HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
