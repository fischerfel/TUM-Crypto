String result = null;
Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
SecretKeySpec ks = new SecretKeySpec(key, "Blowfish");
cipher.init(Cipher.DECRYPT_MODE, ks);
if (reader != null)
{
    InputStream is = getInputStream(reader);
    StringBuilder builder = new StringBuilder();

    byte[] bytes = new byte[8];
    int off = 0;

    while (is.read(bytes, 0, 8) != -1)
    {
        builder.append(new String(cipher.update(bytes, off, 8), "UTF-8"));
    }
    result = builder.toString();
}
