w_NOMECER = CPLib.LRTrim(pFile)+"KEYS/"+CPLib.LRTrim(w_FOLDER)+"/"+CPLib.LRTrim(w_PIVA)+".p12";
...

clientCertPassword = CPLib.LRTrim(w_AEPWDP12);
KeyStore keyStore = KeyStore.getInstance("PKCS12");
FileInputStream fis = new FileInputStream(w_NOMECER);
keyStore.load(fis, clientCertPassword.toCharArray());

KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
kmf.init(keyStore, clientCertPassword.toCharArray());
KeyManager[] keyManagers = kmf.getKeyManagers();

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(keyManagers, null,new SecureRandom());
