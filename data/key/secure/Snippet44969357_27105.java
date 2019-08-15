public static String generateMac(String key,String data) { 
    try { 
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        sha256_HMAC.init(secret_key);
        return new String(Base64.encodeBase64((sha256_HMAC.doFinal(data.getBytes("UTF-8")))));
    }
    catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                } 
    return null;
}

public static String buildData(){
    return "1499421362\n"+"1499421362:754319219beba85ed2319bd39aa4b82d\n"+"GET\n"+"/user\n"+"ishin-global.aktsk.com\n"+"443\n\n";
}

public static String getSecret(){
    return "EskUsUb4z7+IDTi5hjv8yNQgFsPTYftmgJuee6i8MDjMTy2+lQ7nOK/pBI1eiUFE8pQkUBHfbPmX8BcaKgov7Q==";
}
