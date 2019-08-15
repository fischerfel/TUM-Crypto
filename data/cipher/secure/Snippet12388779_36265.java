public static String getDecryptedValue(int keyId,String encryptedCCNumber ,String passPhrase){
              String result="";

              String privateKeyFileName="key_8.private";
              String privateKeyLocation= PropertiesUtil.getProperty("PUBLIC_PRIVATE_KEY_LOCATION");
             String privateKeyFileNameLocation=privateKeyLocation+privateKeyFileName;
              String decryptedValue= getDecryptedMessage(privateKeyFileNameLocation,encryptedCCNumber,passPhrase);
              return result;

       }


       public static String getDecryptedMessage(String privateKeyFileNameLocation, String encryptedCCNumber,String passPhrase) 
                { 
              byte[] decodedBytesCCNumber= Base64.decodeBase64(encryptedCCNumber.getBytes());
           byte[] decryptedMessage=null; 
           try { 
               Cipher cipher = Cipher.getInstance("RSA"); 

                PrivateKey privateKey = getPrivateKey(privateKeyFileNameLocation,passPhrase);
               cipher.init(Cipher.DECRYPT_MODE, privateKey); 
               decryptedMessage = cipher.doFinal(decodedBytesCCNumber); 

           } catch (Throwable t) { 
              t.printStackTrace();
           }

           System.out.println("new String(decryptedMessage)"+new String(decryptedMessage));
           return new String(decryptedMessage); 

       } 

       private static PrivateKey getPrivateKey(String privateKeyFileNameLocation,String passPhrase) throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableEntryException {
               KeyStore ks = KeyStore.getInstance("PKCS12");
               ks.load(new FileInputStream(privateKeyFileNameLocation), passPhrase.toCharArray());
               String alias = (String) ks.aliases().nextElement();
               KeyStore.PrivateKeyEntry keyEntry = (KeyStore.PrivateKeyEntry) ks.getEntry(alias, new KeyStore.PasswordProtection(passPhrase.toCharArray()));
               return keyEntry.getPrivateKey();
           }
