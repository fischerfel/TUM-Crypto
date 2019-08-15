try {
    String secret = "secret";
    String message = "Message";

    Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
    SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
    sha256_HMAC.init(secret_key);

    String hash = Base64.encodeBase64String(sha256_HMAC.doFinal(message.getBytes()));
    System.out.println(hash);
} catch (Exception e){
    System.out.println("Error");
}
