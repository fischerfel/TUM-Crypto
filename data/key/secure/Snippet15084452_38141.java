public static String signPolicyDocument(String policyDocument, String secret) {     
try {
        Mac mac = Mac.getInstance("HmacSHA256");
        byte[] secretBytes = secret.getBytes();
        SecretKeySpec signingKey = new SecretKeySpec(secretBytes, "HmacSHA256");
        mac.init(signingKey);
        byte[] signedSecretBytes = mac.doFinal(policyDocument.getBytes());          
        return new String(Base64.encode(signedSecretBytes));
    } catch (InvalidKeyException e) {
        throw new RuntimeException(e);
    } catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
    }
