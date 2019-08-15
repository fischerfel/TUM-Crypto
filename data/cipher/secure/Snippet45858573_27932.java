private static final int MAX_DECRYPT_BLOCK = 256;


private static RSAPrivateKey loadPrivateKey(InputStream in) throws Exception {
    RSAPrivateKey priKey;
    try {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String readLine = null;
        StringBuilder sb = new StringBuilder();
        while ((readLine = br.readLine()) != null) {
            if (readLine.charAt(0) == '-') {
                continue;
            } else {
                sb.append(readLine);
                sb.append('\r');
            }
        }
        byte[] priKeyData = Base64.decode(new String(sb), Base64.NO_WRAP);



        PKCS8EncodedKeySpec keySpec= new PKCS8EncodedKeySpec(priKeyData);
        KeyFactory keyFactory= KeyFactory.getInstance("RSA",new BouncyCastleProvider());
        priKey= (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    } catch (IOException e) {
        throw new Exception("error reading the key");
    } catch (NullPointerException e) {
        throw new Exception("inputstream is null");
    }
    return priKey;
}


/**
 * decrypt with a private key
 *
 * @param privateKey
 * @param cipherData
 * @return
 * @throws Exception
 */
private static byte[] decrypt(RSAPrivateKey privateKey, byte[] cipherData) throws Exception {
    if (privateKey == null) {
        throw new Exception("key is null");
    }
    Cipher cipher = null;
    try {
        cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        int inputLen = cipherData.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
                cache = cipher.doFinal(cipherData, offSet, MAX_DECRYPT_BLOCK);
            } else {
                cache = cipher.doFinal(cipherData, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * MAX_DECRYPT_BLOCK;
        }
        byte[] decryptedData = out.toByteArray();
        out.close();
        return decryptedData;

    } catch (NoSuchAlgorithmException e) {
        throw new Exception("no such algorithm");
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
        return null;
    } catch (InvalidKeyException e) {
        throw new Exception("InvalidKeyException");
    } catch (IllegalBlockSizeException e) {
        throw new Exception("IllegalBlockSizeException");
    } catch (BadPaddingException e) {
        throw new Exception("BadPaddingException");
    }
}

public static String RSADecrypt(Context context, String KeyFileNameInAssetFolder, byte[] content) {
    try {
        InputStream inputStream = context.getResources().getAssets().open(KeyFileNameInAssetFolder);
        RSAPrivateKey privateKey = loadPrivateKey(inputStream);
        byte[] b = decrypt(privateKey, content);
        return new String(b,"utf-8");
    } catch (Exception e) {
        e.printStackTrace();
    }
    return "error";
}
