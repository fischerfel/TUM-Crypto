RSAPublicKeySpec spec = new RSAPublicKeySpec(new BigInteger(1,hexToByte(rsaJSON.publickey_exp)),new BigInteger(1,hexToByte(rsaJSON.publickey_mod)));
KeyFactory factory = KeyFactory.getInstance("RSA");
PublicKey pub = factory.generatePublic(spec); <---
Cipher cipher = cipher = Cipher.getInstance("RSA/None/OAEPWithSHA1AndMGF1Padding", "BC");
cipher.init(Cipher.ENCRYPT_MODE, pub);
.....
String HEXES = "0123456789ABCDEF";
public static String byteToHex( byte [] raw ) {
    if ( raw == null ) {
      return null;
    }
    final StringBuilder hex = new StringBuilder( 2 * raw.length );
    for ( final byte b : raw ) {
      hex.append(HEXES.charAt((b & 0xF0) >> 4))
         .append(HEXES.charAt((b & 0x0F)));
    }
    return hex.toString();
}

public static byte[] hexToByte( String hexString){
    int len = hexString.length();
    byte[] ba = new byte[len / 2];
    for (int i = 0; i < len; i += 2) {
        ba[i/2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i+1), 16));
    }
    return ba;
}
