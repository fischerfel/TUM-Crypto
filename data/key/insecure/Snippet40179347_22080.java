public static String hmac(String parameters, String key) {
    String result = "";
    parameters = "PBX_SITE=1999888&PBX_RANG=32&PBX_IDENTIFIANT=2&PBX_TOTAL=1000&PBX_DEVISE=978&PBX_CMD=TEST TEST&PBX_PORTEUR=test@test.com&PBX_RETOUR=Mt:M;Ref:R;Auto:A;Erreur:E;Id:U&PBX_HASH=SHA512&PBX_TIME=2011-02-28T11:01:50+01:00&PBX_TYPEPAIEMENT=CARTE&PBX_TYPECARTE=CB&PBX_AUTOSEULE=O";
    key = "0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF";

    try{
        final String HMAC_SHA512 = "HmacSHA512";
        Mac sha512_HMAC = Mac.getInstance(HMAC_SHA512); 

        //pack
        String input = key.length() % 2 == 0 ? key : key  + "0";
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < input.length(); i+=2) {
            String str = input.substring(i, i+2);
            output.append((char)Integer.parseInt(str, 16));
        }
        key = output.toString();

        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), HMAC_SHA512);
        sha512_HMAC.init(keySpec);

        byte [] mac_data = sha512_HMAC.doFinal(parameters.getBytes());

        //toHex
        result = String.format("%040x", new BigInteger(1, mac_data)).toUpperCase();
        System.out.println(result);

    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } finally{
        System.out.println("Done");
    }

    return result;
}
