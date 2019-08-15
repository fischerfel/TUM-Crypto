try {
                        KeyStore keyStore=null;
                        keyStore= KeyStore.getInstance(KeyStore.getDefaultType());
                        char[] passwordKS="network".toCharArray();

                             SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
                             sr.setSeed("any data used as random seed".getBytes());
                             KeyGenerator kg = KeyGenerator.getInstance("AES");
                             kg.init(128, sr);
                             key= kg.generateKey();
                             keyToSave=key.getEncoded();
                             sks = new SecretKeySpec(keyToSave, "AES");

                             try
                             {
                             keyStore.load(null,null);
                             keyStore.setKeyEntry("aliasKey",key,passwordKS, null);

                             }
                             catch(Exception ex)
                             {

                             }
                             FileOutputStream ksout=openFileOutput("keyStoreName", Context.MODE_PRIVATE);
                             keyStore.store(ksout, passwordKS);
                             ksout.close();
                         }


                     } catch (Exception e) {
                     }
                byte[] userLongENC = null;
                byte[] userLatENC=null;
                try {
                    Cipher c = Cipher.getInstance("AES");
                    c.init(Cipher.ENCRYPT_MODE,sks ); 
                    userLatENC = c.doFinal(userLat.getBytes());
                    userLongENC = c.doFinal(userLong.getBytes());
                } catch (Exception e) {
                 }
