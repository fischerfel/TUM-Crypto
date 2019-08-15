SSLContext sc = SSLContext.getInstance("SSL");
sc.init(null, new TrustManager[]{new TrustDummy()}, new SecureRandom());
HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
servicePort = new MyPort(new URL(wsdlLocation), new QName(nameSpaceURI, localPart)).getMyPortSoap();
