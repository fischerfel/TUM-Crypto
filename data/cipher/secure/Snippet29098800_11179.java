public void encrypt(Context context) {

    try {

        //This example uses the Bouncy Castle library
        Security.addProvider(new BouncyCastleProvider());

        String plainText = "This needs to be encrypted";
        String public_key_file = "PublicKey.pem";

        //Load public key
        InputStream inputStream = context.getAssets().open(public_key_file);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        // read from the input stream reader
        PEMParser parser = new PEMParser(inputStreamReader);

        Object key = parser.readObject();
        parser.close();
        PublicKey pubKey = null;

        if (key instanceof SubjectPublicKeyInfo) {
            SubjectPublicKeyInfo spki = (SubjectPublicKeyInfo) key;
            pubKey = KeyFactory.getInstance("RSA").generatePublic
                    (new X509EncodedKeySpec(spki.getEncoded()));
        }

        //Encrypt the plain text
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] encrypted_data = cipher.doFinal(plainText.getBytes());
        String encoded_data =
                new String(Base64.encode(encrypted_data));

        Log.d("MyApp", "Encrypted Value:");
        Log.d("MyApp", encoded_data);

    } catch (Exception ex) {
        Log.d("MyApp", ex.getMessage());
    }

}
