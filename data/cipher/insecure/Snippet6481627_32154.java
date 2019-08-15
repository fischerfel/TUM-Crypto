String key = "av45k1pfb024xa3bl359vsb4esortvks74sksr5oy4s5serondry84jsrryuhsr5ys49y5seri5shrdliheuirdygliurguiy5ru";
try {
    Cipher c = Cipher.getInstance("ARCFOUR");

    SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "ARCFOUR");
    c.init(Cipher.DECRYPT_MODE, secretKeySpec);

    return new String(c.doFinal(Hex.decodeHex(data.toCharArray())), "UTF-8");

} catch (InvalidKeyException e) {
    throw new CryptoException(e);
}
