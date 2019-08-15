         KeyStore ks = KeyStore.getInstance("JKS");
         ks.load(new FileInputStream(keyStoreFile), "password".toCharArray());
         SSLContext sslCtx = SSLContext.getInstance("TLS");
         CustomKeyManager ck = new CustomKeyManager(ks, "mykey");
         KeyManager[] kms = new KeyManager[1];
         kms[0] = ck;
         System.out.println(ck.getPrivateKey("mykey")); //returns a non null value
         sslCtx.init(kms , null, null); //throws an exception
