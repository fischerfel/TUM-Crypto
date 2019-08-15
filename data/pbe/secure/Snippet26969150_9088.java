public String encrypt(String texte) {
   byte[] bytePassword = Base64.decode(PASSWORD, Base64.DEFAULT);
   byte[] byteSalt = Base64.decode(SALT, Base64.DEFAULT);
   byte[] bytesIv = Base64.decode(IV, Base64.DEFAULT);
   SecretKeyFactory factory = null;
   factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
   KeySpec spec = new PBEKeySpec(PASSWORD.toCharArray(), byteSalt, NB_ITER_RFC, 128);
   SecretKey temp = null;
   temp = factory.generateSecret(spec);
   byte[] clef = temp.getEncoded();
   Cipher c = Cipher.getInstance("DESede/CBC/PKCS5Padding");
   IvParameterSpec ivParam = new IvParameterSpec(bytesIv);
   c.init(Cipher.ENCRYPT_MODE, temp, ivParam);     
   byte[] encrypted = c.doFinal(texte.getBytes("UTF-8"));
   mdp = Base64.encodeToString(encrypted, Base64.DEFAULT);
   Log.i("MDP CHIFFRE", " = " + mdp);
}
