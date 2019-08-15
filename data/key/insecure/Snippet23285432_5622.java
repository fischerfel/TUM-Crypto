            String mykey = "Private Key";
    String test = "pnrjson6b17f33e25e2d8197462d1c6bcb0b130";
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
