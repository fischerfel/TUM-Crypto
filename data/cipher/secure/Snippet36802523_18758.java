 Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidKeyStoreBCWorkaround");
                output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());
