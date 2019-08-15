private static final String KEY = "AAAAAAAA000000000000000000000000";
private static final String IV = "AAAAAAAA00000000";
private static final String FCN = "Blowfish/CBC/PKCS5Padding";
private static final String CN = "Blowfish";

final byte[] encoded = Base64.decode("eAIUXziwB8QbBexkiIDR3A==");
final SecretKeySpec key =
new SecretKeySpec(Hex.decodeHex(KEY.toCharArray()), CN);
final Cipher cipher = Cipher.getInstance(FCN, JCE_PROVIDER);
cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(Hex.decodeHex(IV.toCharArray())));
final byte[] decrypted = cipher.doFinal(encoded);
return new String(decrypted);
