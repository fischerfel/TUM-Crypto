private String generateRSAEncryptedText(String publicKey) {
        String baseCredentials = email + "---" + password;
        try {
            return encryptRSA(context, baseCredentials, publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getPublicKeyStringFromPemFormat(String PEMString, boolean isFilePath) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        BufferedReader pemReader = null;
        if (isFilePath) {
            pemReader = new BufferedReader(new InputStreamReader(new FileInputStream(PEMString)));
        } else {
            pemReader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(PEMString.getBytes("UTF-8"))));
        }
        StringBuffer content = new StringBuffer();
        String line = null;
        while ((line = pemReader.readLine()) != null) {
            if (line.indexOf("-----BEGIN PUBLIC KEY-----") != -1) {
                while ((line = pemReader.readLine()) != null) {
                    if (line.indexOf("-----END PUBLIC KEY") != -1) {
                        break;
                    }
                    content.append(line.trim());
                }
                break;
            }
        }
        if (line == null) {
            throw new IOException("PUBLIC KEY" + " not found");
        }
        Log.i("PUBLIC KEY: ", "PEM content = : " + content.toString());
        return content.toString();
    }

    public String encryptRSA(Context mContext, String message, String publicKeyString) throws Exception {

        String keyString = getPublicKeyStringFromPemFormat(publicKeyString, false);

        // converts the String to a PublicKey instance
        byte[] keyBytes = Base64.decode(keyString.getBytes("utf-8"), Base64.NO_WRAP);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey key = keyFactory.generatePublic(spec);

        // decrypts the message
        byte[] dectyptedText = null;
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        dectyptedText = cipher.doFinal(Base64.decode(message.getBytes("utf-8"), Base64.NO_WRAP));
        return Base64.encodeToString(dectyptedText, Base64.NO_WRAP);
    }
