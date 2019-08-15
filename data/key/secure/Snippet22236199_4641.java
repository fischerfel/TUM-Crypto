   public static void main() {
     ecnryptedData = "f5EBWYipPKG1FpyTEP7pyPLLJNpqrvwYJFs8iMw9mOY$";
     ecnryptedData = ecnryptedData.replace('-', '+')
         .replace('_', '/').replace('$', '=');

     while (ecnryptedData.length() % 4 != 0) {
         ecnryptedData = StringUtils.rightPad(ecnryptedData, ecnryptedData.length() + (4 - ecnryptedData.length() % 4),
             "=");
     }
     System.out.println(Decrypt(ecnryptedData, "ThisIsMyKey"));
 }

 public static String Decrypt(String text, String key) throws Exception {
     Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
     byte[] keyBytes = new byte[16];
     byte[] b = key.getBytes("UTF-8");
     int len = b.length;
     if (len > keyBytes.length) len = keyBytes.length;
     System.arraycopy(b, 0, keyBytes, 0, len);
     SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
     AlgorithmParameterSpec spec = new IvParameterSpec(keyBytes);
     cipher.init(Cipher.DECRYPT_MODE, keySpec, spec);

     BASE64Decoder decoder = new BASE64Decoder();
     byte[] results = cipher.doFinal(decoder.decodeBuffer(text));
     return new String(results, "UTF-8");
 }
