 private static PublicKey getPublicKeyFromPemFormat(String PEMString) throws IOException, NoSuchAlgorithmException,
            InvalidKeySpecException {

        BufferedReader pemReader = null;

        pemReader = new BufferedReader(new InputStreamReader(
                new ByteArrayInputStream(PEMString.getBytes("UTF-8"))));

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

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(new X509EncodedKeySpec(Base64.decode(content.toString(), Base64.DEFAULT)));
        // return keyFactory.generatePublic(new X509EncodedKeySpec(content.toString().getBytes()));


    }


 public static String getContentWithPublicKeyFromPemFormat(String PEMString,
                                                              String content, boolean ispublic) throws NoSuchAlgorithmException,
            InvalidKeySpecException, IOException, NoSuchProviderException,
            NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {


        PublicKey publicKey = getPublicKeyFromPemFormat(PEMString);
        if (publicKey != null)
            Log.i("PUBLIC KEY: ", "FORMAT : " + publicKey.getFormat()
                    + " \ntoString : " + publicKey.toString());

        byte[] contentBytes = Base64.encode(content.getBytes(), Base64.DEFAULT);
        // byte[] contentBytes = content.getBytes();

        byte[] decoded = null;

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");//BC=BouncyCastle Provider
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        decoded = cipher.doFinal(contentBytes);
        return new String(decoded, "UTF-8");

    }
