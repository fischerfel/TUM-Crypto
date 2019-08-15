    byte[] raw = key.getEncoded();
    System.out.println(key.getEncoded().length);
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

    try {
        ecipher = Cipher.getInstance("AES");
        dcipher = Cipher.getInstance("AES");
        ecipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        dcipher.init(Cipher.DECRYPT_MODE, skeySpec);

    } catch (javax.crypto.NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (java.security.NoSuchAlgorithmException e) {
        e.printStackTrace();
    **} catch (java.security.InvalidKeyException e) {
        e.printStackTrace();**
    }



read:17
17
java.security.InvalidKeyException: Invalid AES key length: 17 bytes
at com.ibm.crypto.provider.AESCipher.engineGetKeySize(Unknown Source)
at javax.crypto.Cipher.b(Unknown Source)
at javax.crypto.Cipher.a(Unknown Source)
at javax.crypto.Cipher.a(Unknown Source)
at javax.crypto.Cipher.a(Unknown Source)
at javax.crypto.Cipher.init(Unknown Source)
at javax.crypto.Cipher.init(Unknown Source)
at au.edu.uts.itd.encryption.manager.AESEncrypter.<init>(AESEncrypter.java:21)
at au.edu.uts.itd.encryption.util.Encrypt.main(Encrypt.java:26)
Exception in thread "main" java.lang.IllegalStateException: Cipher not initialized
at javax.crypto.Cipher.c(Unknown Source)
at javax.crypto.Cipher.doFinal(Unknown Source)
at au.edu.uts.itd.encryption.manager.AESEncrypter.encrypt(AESEncrypter.java:39)
at au.edu.uts.itd.encryption.util.Encrypt.main(Encrypt.java:27)
