 public PublicKey getFromString(String keystr) throws Exception
    {
        // Remove the first and last lines

        String pubKeyPEM = keystr.replace("-----BEGIN PUBLIC KEY-----\n", "");
        pubKeyPEM = pubKeyPEM.replace("-----END PUBLIC KEY-----", "");

        // Base64 decode the data

        byte [] encoded = Base64.decode(pubKeyPEM);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        PublicKey pubkey = kf.generatePublic(keySpec);

        return pubkey;
    }

    public String RSAEncrypt(final String plain) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {

        if (pubKey!=null) {
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            encryptedBytes = cipher.doFinal(plain.getBytes());
            Log.d("BYTES", new String(encryptedBytes));
            return Hex.encodeHexString(encryptedBytes);
        }
        else
            return null;
    }
