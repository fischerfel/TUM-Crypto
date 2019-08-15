String mykey = "fb6a1271f98099ac96cc0002d5e8022b";
String test = "json6b17f33e25e2d8197462d1c6bcb0b1302156641988";
try {
    Mac mac = Mac.getInstance("HmacSHA1");
    SecretKeySpec secret = new SecretKeySpec(mykey.getBytes(),
            "HmacSHA1");
    mac.init(secret);
    byte[] digest = mac.doFinal(test.getBytes());
    for (byte b : digest) {
        System.out.format("%02x", b);        
    }

    System.out.println();
} catch (Exception e) {
    System.out.println(e.getMessage());
}
