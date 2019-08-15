/**
 * 
 * @param PEMString  -A file/string in .pem format with a generated RSA key (with "des3", using "openssl genrsa".)
 * @param content
 * @return String value of content Decoded
 * @throws NoSuchAlgorithmException
 * @throws InvalidKeySpecException
 * @throws IOException
 * @throws NoSuchProviderException
 * @throws NoSuchPaddingException
 * @throws InvalidKeyException
 * @throws IllegalBlockSizeException
 * @throws BadPaddingException
 * 
 * @author hsigmond
 */


    public static String getContentWithPublicKeyFromPemFormat(String PEMString,
        String content,boolean isFilePath) throws NoSuchAlgorithmException,
        InvalidKeySpecException, IOException, NoSuchProviderException,
        NoSuchPaddingException, InvalidKeyException,
        IllegalBlockSizeException, BadPaddingException {

    PublicKey publicKey = getPublicKeyFromPemFormat(PEMString,isFilePath);
    if (publicKey != null)
        Log.i("PUBLIC KEY: ", "FORMAT : " + publicKey.getFormat()
                + " \ntoString : " + publicKey.toString());

    byte[] contentBytes = Base64.decode(content, Base64.DEFAULT);
    byte[] decoded = null;

    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "BC");//BC=BouncyCastle Provider
    cipher.init(Cipher.DECRYPT_MODE, publicKey);
    decoded = cipher.doFinal(contentBytes);
    return new String(decoded, "UTF-8");
}
