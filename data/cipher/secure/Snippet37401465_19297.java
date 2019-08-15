        String pub = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEA4IJZLsjlx+o4RSvafaAcReoNnzrI0UXu7kZyXPe31ql32X9AvhC6QQIUmLkr1Evm0zP/SgVG9YX3DSqBUgPo04iv1I1/wNKwAf1/uH9EiiqdpczefyxxnzJiKUTcx2/4mA4E4QxCIL5JsZb78WoYZrd2kToW/WD01MnSFiCgSyjGdd812GY2EVzfvlv8kYuti3icMUyitEfHhtw8cAWI6/nVrRPNs0e5NsvtZJ0nfrXsfQDR0C7+ivQK+fQabi8oRGsbTZceAvVlqVE669zoIwIFLcB+eYXTxbka4E7veUMpaF9w//HdwVS2y/2jJiI+16qPStQQPIKQ4Cucoif7/UHfIBuVGVJ5MIVyK7NC7TV/lyoXmyo7ZcnVZnI7rZcw5/qZcqaZ0VCrzvHijwTK7100hOOjiarvRa2OJGXHLIeAUlbrHOXEXS6ah2glPhLDEg6Qzp/lKVSISolal7q73qyhF483P9jXn3hefSLA9J1/1LgeajWvuVkxuw+dy2Tlv7oUpNBkX47/TOho5qttr1y9K3hD5Q87RAJPdBtFdDbY8qUPxoiBsTbUWjVoEjJf2YAsLTJIIi2ZISkbD/VdrtZnS73QSJkJReOMNT9XYNGDJvwNIrRcNGFKlJcX6qq+ozGNsDkrt0ObxAD7YCTjAYQVTlbQOaTu5DbGxGDNCoMCAwEAAQ==";

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        byte[] keyBytes = Base64.getDecoder().decode(pub.getBytes("UTF-8"));  
        PKCS1EncodedKeySpec KeySpec = new PKCS1EncodedKeySpec(keyBytes);
        RSAPublicKey publicKey = (RSAPublicKey)keyFactory.generatePublic((java.security.spec.KeySpec) KeySpec);

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] cipherData = cipher.doFinal(text.getBytes("UTF-8"));


        return cipherData;
