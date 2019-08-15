private SSLSocketFactory getSSLSocketFactory() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
    KeyStore trusted = KeyStore.getInstance("PKCS12");
    InputStream in = activity.getResources().openRawResource(R.raw.client_keystore);
    try {
        trusted.load(in, "blablabla".toCharArray());
    } catch (CertificateException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } finally {
        try {
            in.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
    tmf.init(trusted);
    SSLContext context = SSLContext.getInstance("SSLv3");
    context.init(null, tmf.getTrustManagers(), null);
    return context.getSocketFactory();
}

public String SendRecieveMessage(String xmlData, String nameXML, String methodName, String methodAction) {

    HttpsTransportSE httpTransport = new KeepAliveHttpsTransportSE("hostname", 8443, "/blablabla/blablabla?wsdl", 1000);
    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
    SoapObject request = new SoapObject(activity.getResources().getString(R.string.SOAP_NAMESPACE), methodName); // set
    // request
    Log.e("Sending SOAP", xmlData);
    String base64 = base64Coder.encodeString(xmlData);
    request.addProperty(nameXML, base64); 
    envelope.setOutputSoapObject(request); // prepare request
    try {
        ((HttpsServiceConnectionSE) httpTransport.getServiceConnection()).setSSLSocketFactory(getSSLSocketFactory());
    } catch (KeyManagementException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    } catch (KeyStoreException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    } catch (NoSuchAlgorithmException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    } catch (IOException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    }
    SoapPrimitive result = null;
    try {
        httpTransport.call(methodAction, envelope);
        result = (SoapPrimitive) envelope.getResponse(); // get
        if (result != null) {
            base64 = base64Coder.decodeString(result.toString());
        } else {
            base64 = null;
        }
    } catch (IOException e) {
        // TODO Auto-generated catch block
        Log.e("ERROR", "SOAPSendRecieve: " + e.getMessage());
        base64 = null;
    } catch (XmlPullParserException e) {
        // TODO Auto-generated catch block
        Log.e("ERROR", "SOAPSendRecieve: " + e.getMessage());
        base64 = null;
    } catch (IllegalArgumentException e) {
        Log.e("ERROR", "SOAPSendRecieve: " + e.getMessage());
        base64 = null;
        }
    } finally {
        request = null;
        result = null;
    }
    return base64;
}
