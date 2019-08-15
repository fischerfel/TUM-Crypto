    @Test
public void test_cipher() {
    final SecureRandom secureRandomWithFixSeed = generateSecureRandomWithFixSeed();

    final String pkcs = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAv7n+/uWHHVC7229QLEObeH0vUcOagavDukf/gkveqgZsszzGkZQaXfsrjdPiCnvjozCy1tbnLu5EInDy4w8B+a9gtK8KqsvlsfuaT9kRSMUS8CfgpWj8JcJwijmeZhjR52k0UBpWLfn3JmRYW8xjZW6dlOSnS0yqwREPU7myyqUzhk1vyyUG7wLpk7uK9Bxmup0tnbnD4MeqDboAXlQYnIFVV+CXywlAQfHHCfQRsGhsEtu4excZVw7FD1rjnro9bcWFY9cm/KdDBxZCYQoT/UW0OBbipoINycrmfMKt1r4mGE9/MdVoIEMBc54aI6sb2g5J2GtNCYfEu+1/gA99xY0+5B3ydH74cbqfHYOZIvu11Q7GnpZ6l8zTLlMuF/pvlSily76I45H0YZ3HcdQnf/GoKC942P6fNsynHEX01zASYM8dzyMxHQpNEx7fcXGi+uiBUD/Xdm2jwzr9ZEP5eEVlrpcAvr8c9S5ylE50lwR+Mp3vaZxPoLdSGZrfyXy4v97UZSnYARQBacmn6KgsIHIOKhYOxNgUG0jwCO/zrPvlbjiYTHQYLOCcldTULvXOdn51enJFGVjscGoZfRj6vZgyHVCUW4iip4iSbSKPcPbf0GMZuniS9qJ3Wybve0/xpppdOv1c40ez0NKQyQkEZRb+V0qfesatJKZd/hUGr+MCAwEAAQ==";
    final byte bytePKCS[] = Base64.base64ToByteArray(pkcs);
    final X509EncodedKeySpec pubKeySpec = new X509EncodedKeySpec(bytePKCS);

    PublicKey pubKey = null;
    try {
        pubKey = KeyFactory.getInstance("RSA").generatePublic(pubKeySpec);
    } catch (InvalidKeySpecException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }

    final String targetResultText = "NZqTzuNli92vuXEQNchGeF6faN/NBHykhfqBFcWzBHZhbgljZaWAcAzasFSm/odZZ6vBD2jK7J4oi5BmDjxNdEjkXyv3OZ2sOTLCfudgPwXcXmmhOwWHDLY02OX0X3RwBHzqWczqAd4dwslo59Gp5CT59GWXenJPL8wvG90WH2XAKOmHg5uEZj55ZvErRQ6StPVzLkiNCMPOhga7FZWK/rSEpT6BHDy3CibDZ0PNRtAW4wiYAr0Cw6brqiqkN301Bz6DzrV5380KDHkw26GjM8URSTFekwvZ7FISQ72UaNHhjnh1WgMIPf/QDbrEh5b+rmdZjzc5bdjyONrQuoj0rzrWLN4z8lsrBnKFVo+zVyUeqr0IxqD2aHDLyz5OE4fb5IZJHEMfYr/R79Zfe8IuQ2tusA02ZlFzGRGBhAkb0VygXxJxPXkjbkPaLbZQZOsKLUoIDkcbNoUTxeS9+4LWVg1j5q3HR9OSvmsF5I/SszvVrnXdNaz1IKCfVYkwpIBQ+Y+xI/K360dWIHR/vn7TU4UsGmWtwVciq0jWLuBN/qRE6MV47TDRQu63GzeV00yAM/xUM33qWNXCV1tbGXNZw8jHpakgflTY0hcjOFHPpq2UfJCyxiSBtZ0b7hw9Rvhi8VwYc243jXO9CvGq+J6JYvchvKHjq2+YKn1UB2+gs20=";
    final String plainText = "a";
    String resultText = "";
    try {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey, secureRandomWithFixSeed);
        final byte[] result = cipher.doFinal(plainText.getBytes());
        resultText = Base64.byteArrayToBase64(result);
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    }
    assertThat(resultText, is(targetResultText));
}
