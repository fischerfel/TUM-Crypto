public String Decrypt(String input) {
    try {
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        String modulusString = "mmGn1IXB+/NEm1ecLiUzgz7g2L6L5EE5DUcptppTNwZSqxeYKn0AuAccupL0iyX3LMPw6Dl9pjPXDjk93TQwYwyGgZaXOSRDQd/W2Y93g8erpGBRm/Olt7QN2GYhxP8Vn+cWUbNuikdD4yMfYX9NeD9UNt5WJGFf+jRkLk0zRK0A7ZIS+q0NvGJ/CgaRuoe3x4Mh1qYP9ZWNRw8rsDbZ6N2zyUa3Hk/WJkptRa6jrzc937r3QYF3eDTurVJZHwC7c3TJ474/8up3YNREnpK1p7hqwQ78fn35Tw4ZyTNxCevVJfYtc7pKHHiwfk36OxtOIesfKlMnHMs4vMWJm79ctixqAe3i9aFbbRj710dKAfZZ0FnwSnTpsoKO5g7N8mKY8nVpZej7tcLdTL44JqWEqnQkocRqgO/p3R8V/6To/OjQGf0r6ut9y/LnlM5qalnKJ1gFg1D7gCzZJ150TX4AO5kGSAFRyjkwGxnR0WLKf+BDZ8T/syOrFOrzg6b05OxiECwCvLWk0AaQiJkdu2uHbsFUj3J2BcwDYm/kZiD0Ri886xHqZMNExZshlIqiecqCskQhaMVC1+aCm+IFf16Qg/+eMYCd+3jm/deezT4rcMBOV/M+muownGYQ9WOdjEK53h9oVheahD3LqCW8MizABFimvXR3wAgkIUvhocVhSN0=";
        String exponentString = "AQAB";

        byte[] modulusBytes = Base64.decode(modulusString.getBytes("UTF-8"), Base64.DEFAULT);
        byte[] dBytes = Base64.decode(exponentString.getBytes("UTF-8"), Base64.DEFAULT);

        BigInteger modulus = new BigInteger(1, modulusBytes);
        BigInteger d = new BigInteger(1, dBytes);

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, d);
        PublicKey key = keyFactory.generatePublic(keySpec);

        //at one point I read somewhere that .net reverses the byte array so that it needs to be reversed for java, but who knows any more
        /*byte[] inputArrayReversed = Base64.decode(input.getBytes("UTF-8"), Base64.DEFAULT);
        for (int i = 0; i < inputArrayReversed.length / 2; i++) {
            byte temp = inputArrayReversed[i];
            inputArrayReversed[i] = inputArrayReversed[inputArrayReversed.length - 1];
            inputArrayReversed[inputArrayReversed.length - 1] = temp;
        }*/

        byte[] decryptedText = null;
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, key);
        decryptedText = cipher.doFinal(Base64.decode(input.getBytes("UTF-8"), Base64.DEFAULT));
        return Base64.encodeToString(decryptedText, Base64.NO_WRAP);
        //return new String(decryptedText, "UTF-8");
    } catch (Exception e) {
        e.printStackTrace();
    }
    return "";
}
