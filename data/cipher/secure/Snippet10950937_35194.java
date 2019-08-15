class Crypt
{
    Key KEY;
    String TD;
    Cipher aes = Cipher.getInstance("AES/CBC/PKCS5Padding");

    KeyGenerator keyGen = KeyGenerator.getInstance("AES");

public Crypt()
{
    int keyLength = 192;
    keyGen.init(keyLength);
    KEY = keyGen.generateKey();
