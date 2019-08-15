public static byte[] decryptRSA(Context mContext, byte[] message) throws Exception { 

    // reads the public key stored in a file
    InputStream is = mContext.getResources().openRawResource(R.raw.sm_public);
    BufferedReader br = new BufferedReader(new InputStreamReader(is));
    List<String> lines = new ArrayList<String>();
    String line = null;
    while ((line = br.readLine()) != null)
        lines.add(line);

    // removes the first and last lines of the file (comments)
    if (lines.size() > 1 && lines.get(0).startsWith("-----") && lines.get(lines.size()-1).startsWith("-----")) {
        lines.remove(0);
        lines.remove(lines.size()-1);
    }

    // concats the remaining lines to a single String
    StringBuilder sb = new StringBuilder();
    for (String aLine: lines)
        sb.append(aLine);
    String keyString = sb.toString();
    Log.d("log", "keyString:"+keyString);

    // converts the String to a PublicKey instance
    byte[] keyBytes = Base64.decodeBase64(keyString.getBytes("utf-8"));
    X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PublicKey key = keyFactory.generatePublic(spec);

    // decrypts the message
    byte[] dectyptedText = null;
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, key);
    dectyptedText = cipher.doFinal(Base64.decodeBase64(message));
    return dectyptedText;
}
