        KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");

        kmf.init(keyStore, password.toCharArray());

        KeyManager[] keyManagers = kmf.getKeyManagers();


        // creating an SSLSocketFactory that uses our TrustManager
        sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagers, null, null);

    } catch (IOException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (CertificateException e) {
        e.printStackTrace();
    } catch (KeyStoreException e) {
        e.printStackTrace();
    } catch (KeyManagementException e) {
        e.printStackTrace();
    } catch (UnrecoverableKeyException e) {
        e.printStackTrace();
    }

    return sslContext.getSocketFactory();

}
