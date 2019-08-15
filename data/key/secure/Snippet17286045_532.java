private static final String ALGO = "AES";


@RequestMapping(value = "/util/encrypt/", method = RequestMethod.GET)
@ResponseBody
public String encrypt(HttpServletResponse httpResponse,
        @RequestParam(value = "token", required=true) String token) throws Exception 
{
    Key key = generateKey();
    Cipher c = Cipher.getInstance(ALGO);
    c.init(Cipher.ENCRYPT_MODE, key);
    byte[] encVal = c.doFinal(token.getBytes());
    String encryptedValue = Base64.encodeBase64String(encVal);
    return encryptedValue.trim();
}



@RequestMapping(value = "/util/decrypt/", method = RequestMethod.GET)
@ResponseBody
public String decrypt(HttpServletResponse httpResponse,
        @RequestParam(value = "token", required=true) String token) throws Exception 
{
    token = URLDecoder.decode(token, "UTF-8");
    Key key = generateKey();
    Cipher c = Cipher.getInstance(ALGO);
    c.init(Cipher.DECRYPT_MODE, key);
    byte[] decordedValue = Base64.decodeBase64(token);
    byte[] decValue = c.doFinal(decordedValue);
    String decryptedValue = new String(decValue);
    return decryptedValue.trim();
}



private Key generateKey() throws Exception 
{
    Key key = new SecretKeySpec(getAesKey().getBytes(), ALGO);
    return key;
}
