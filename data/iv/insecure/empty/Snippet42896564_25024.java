   protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    generateHMAC();
}

String K0 = "4f28d5901b4b7b80d33fda76ca372c2a20bd1a6c2aad7fa215dc79d507330678";
String generatedString = "com.myapp.com|355004059196637|911111111111|11341e5e-9643-4559-bbb7-34d40555e96c";

private void generateHMAC() {
    Log.d("Message of Hash", generatedString);
    byte[] var14 = new byte[0];
    try {
        var14 = SHA256(generatedString);
        byte[] var15 = new byte[0];
        var15 = encrypt(var14, hexStringToByteArray(K0));
        String var4 = Base64.encodeToString(var15, 2);
        Log.d("Existing K0", K0);
        Log.d("HMAC", var4);
    } catch (Exception e) {
        e.printStackTrace();
    }
}


public byte[] SHA256(String paramString) throws Exception {
    MessageDigest md = MessageDigest.getInstance("SHA-256");
    md.update(paramString.getBytes("UTF-8"));
    byte[] digest = md.digest();
    return digest;
}

public byte[] encrypt(byte[] var1, byte[] var2) throws Exception {
    SecretKeySpec var3 = new SecretKeySpec(var2, "AES");
    byte[] var4 = new byte[16];
    IvParameterSpec var5 = new IvParameterSpec(var4);
    Cipher var6 = Cipher.getInstance("AES/CBC/PKCS5Padding");
    var6.init(1, var3, var5);
    byte[] var7 = var6.doFinal(var1);
    return var7;
}

public byte[] hexStringToByteArray(String var1) {
    byte[] var2 = new byte[var1.length() / 2];

    for (int var3 = 0; var3 < var2.length; ++var3) {
        int var4 = var3 * 2;
        int var5 = Integer.parseInt(var1.substring(var4, var4 + 2), 16);
        var2[var3] = (byte) var5;
    }

    return var2;
}
