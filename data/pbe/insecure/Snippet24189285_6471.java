@Override
protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(R.layout.amain);

    try {
        viewKey();
    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        Log.d("checkingthevalue",e.toString());
    }
}

 private void viewKey() {
        // TODO Auto-generated method stub
         String pass = "password";
         String testStr = "TheSecretString";
         final byte[] SALT = {
                (byte) 0xaa, (byte) 0xaa, (byte) 0xce, (byte) 0xce,
                (byte) 0xaa, (byte) 0xaa, (byte) 0xce, (byte) 0xce,
            };
         try{
             SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
             SecretKey key = keyFactory.generateSecret(new PBEKeySpec(pass.toCharArray()));
             Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
             pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT, 20));
             String strEnc = base64Encode(pbeCipher.doFinal(testStr.getBytes("UTF-8")));
             Log.d("ViewKey", "strEnc :: " + strEnc);
         }catch (Exception e)
         {
             Log.d("ViewKey","ERROR");
         }
    }
 private static String base64Encode(byte[] bytes) {
final int asd = Base64.URL_SAFE | Base64.NO_PADDING | Base64.NO_WRAP ;
            return Base64.encodeToString(bytes,asd);
        }
