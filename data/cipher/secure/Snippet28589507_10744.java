public String encryptSessionKeyWithPublicKey(String pemString, byte[] sessionKey) {
    try {
        PublicKey publicKey = getPublicKeyFromPemFormat(pemString);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1PADDING");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cipherData = cipher.doFinal(sessionKey);
        return Base64.encodeToString(cipherData, Base64.DEFAULT);
    } catch (IOException ioException) {
        Log.e(TAG, "ioException");
    } catch (NoSuchAlgorithmException exNoSuchAlg) {
        Log.e(TAG, "NoSuchAlgorithmException");
    } catch (InvalidKeySpecException exInvalidKeySpec) {
        Log.e(TAG, "InvalidKeySpecException");
    } catch (NoSuchPaddingException exNoSuchPadding) {
        Log.e(TAG, "NoSuchPaddingException");
    } catch (InvalidKeyException exInvalidKey) {
        Log.e(TAG, "InvalidKeyException");
    } catch (IllegalBlockSizeException exIllBlockSize) {
        Log.e(TAG, "IllegalBlockSizeException");
    } catch (BadPaddingException exBadPadding) {
        Log.e(TAG, "BadPaddingException");
    }

    return null;
}

private PublicKey getPublicKeyFromPemFormat(String PEMString)
        throws IOException, NoSuchAlgorithmException, InvalidKeySpecException
{
    AssetManager assetManager = context.getAssets();
    InputStream inputStream = assetManager.open(PEMString);
    BufferedReader pemReader = new BufferedReader(new InputStreamReader(inputStream));

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
        throw new IOException("PUBLIC KEY not found");
    }

    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    return keyFactory.generatePublic(new X509EncodedKeySpec(Base64.decode(content.toString(), Base64.DEFAULT)));

}
