public String getSignature(String _plainTextMessage,PrivateKey privateKey){
try {
    Signature signer = Signature.getInstance("SHA1withRSA");
    signer.initSign(privateKey);
    signer.update(_plainTextMessage.getBytes());
    byte[] signature = signer.sign();

    MessageDigest sha1 = MessageDigest.getInstance("SHA1");
    byte[] digest = sha1.digest(signature);

    return new BASE64Encoder().encode(digest);

} catch (NoSuchAlgorithmException e) {
    e.printStackTrace();
} catch (InvalidKeyException e) {
    e.printStackTrace();
} catch (SignatureException e) {
    e.printStackTrace();
} catch (Exception e) {
    e.printStackTrace();
}
return null;
