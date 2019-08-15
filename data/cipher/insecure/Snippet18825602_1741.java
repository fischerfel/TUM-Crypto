    public static String encryptParameters(byte plainText[], String sKey)
        throws RCPCipherException
    {
      Cipher cipher;
      DESedeKeySpec dks = new DESedeKeySpec(_getKeyBytes(sKey));
      java.security.Key secretKey = SecretKeyFactory.getInstance("DESede").generateSecret(dks);
      cipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
      cipher.init(1, secretKey);
      return Base64.encodeBase64URLSafeString(cipher.doFinal(plainText));
    }
