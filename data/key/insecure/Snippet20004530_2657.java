try {
    String secret = "secret";
    String message = "Message";

    Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
    SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
    sha256_HMAC.init(secret_key);
    byte[] s53 = sha256_HMAC.doFinal(message.getBytes());
    String hash = Base64.encodeToString(s53, Base64.DEFAULT);
    Log.e("beadict", hash);
} catch (Exception e){
    System.out.println("Error");
}
