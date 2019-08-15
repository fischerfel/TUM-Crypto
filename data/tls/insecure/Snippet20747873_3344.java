StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        InputStream caInput = new BufferedInputStream(new FileInputStream("/sdcard/Certificate.pfx"));
        KeyHelper kh = new KeyHelper();
        Certificate ca = kh.GetKey("Password");
        String keyStoreType = KeyStore.getDefaultType();
        keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, "Password".toCharArray());
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(kmf.getKeyManagers(),null,new SecureRandom());
        HttpsURLConnection urlConnection =
                (HttpsURLConnection)url.openConnection();
        urlConnection.setRequestProperty("x-ms-version",AZURE_REST_VERSION);
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("GET");
        urlConnection.setSSLSocketFactory(context.getSocketFactory());
        urlConnection.connect();

        InputStream in = new BufferedInputStream(urlConnection.getInputStream()); //<-----Blows up here

    } catch (KeyStoreException e) {
        throw new KeyStoreException("Keystore Exception",e);
    } catch (NoSuchAlgorithmException e) {
        throw new NoSuchAlgorithmException("Algorithm exception",e);
    } catch (KeyManagementException e) {
        throw new KeyManagementException("Key Exception", e);
    }
