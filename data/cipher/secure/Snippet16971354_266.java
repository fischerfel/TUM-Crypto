        // reads the public key stored in a file
    AssetManager am = mContext.getAssets();
    InputStream is = am.open("public_key.pem");     
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

    // converts the String to a PublicKey instance
    byte[] keyBytes = Base64.decode(keyString.getBytes("utf-8"), 0);
    X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PublicKey key = keyFactory.generatePublic(spec);

    //byte[] encryptedText = null;
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    String encryptedText = Base64.encodeToString(cipher.doFinal(message), 0);

   return encryptedText;
