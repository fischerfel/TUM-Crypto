private static String calculateSignature(String nonce, String data, String path) {
    System.out.println("calculateSignature " + nonce + " " + data + " " + path);
    String signature = "";
    try {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update((nonce + data).getBytes());
        Mac mac = Mac.getInstance("HmacSHA512");
        mac.init(new SecretKeySpec(Base64.decode(API_SECRET.getBytes(), Base64.NO_WRAP), "HmacSHA512"));
        mac.update(path.getBytes());
        byte[] digest = md.digest();
        System.out.println("digest = " + Base64.encodeToString(digest, Base64.NO_WRAP));
        signature = Base64.encodeToString(mac.doFinal(digest), Base64.NO_WRAP);
        System.out.println("signature = " + signature);
    } catch(Exception e) {}
    return signature;
}
