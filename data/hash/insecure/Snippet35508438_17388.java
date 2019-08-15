public static byte []computeKey(byte[] bytes) throws NoSuchAlgorithmException 
{
     byte[] salt = { 82,122, 43, 30,-47, 97, 4,-124,-31,-63,-108, 69,-83,-86,-125, 88,-98,-77,111, 79,-71,-73,100,106,  8,-20,-95,-27, 38,-32,-61, 88};
     MessageDigest digester = MessageDigest.getInstance("SHA1");
     digester.update(bytes, 0, bytes.length);
     digester.update(salt, 0, salt.length);
     byte[] digest = digester.digest();
     return digest;
}
