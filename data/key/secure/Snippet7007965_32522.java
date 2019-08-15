private String getSignatureToken(String consumerSecret, String baseString, String algotithm) {
    byte[] keyBytes = (consumerSecret+"&").getBytes();
    SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, algotithm);
    Mac mac;
    String res = null;
    try {
        mac = Mac.getInstance(algotithm);
        mac.init(secretKeySpec);
        byte[] rawHmac = mac.doFinal((baseString).getBytes());
        res = android.util.Base64.encodeToString(rawHmac, android.util.Base64.DEFAULT);
        res = res.substring(0, res.length() - 1);
        System.out.println("MAC : " + res);

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    }
    return res;
}
