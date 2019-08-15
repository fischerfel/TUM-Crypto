enter code here
Private static byte[] encrypt(String text, PublicKey key) {
        byte[] cipherText = null;
        try {
          final Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");
          cipher.init(Cipher.ENCRYPT_MODE, key);
          cipherText = cipher.doFinal(text.getBytes());
        } catch (Exception e) {
          e.printStackTrace();
        }
        return cipherText;
      }
