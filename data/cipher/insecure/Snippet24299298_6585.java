try
{
    if (key == null)
    {
        key = SecretKeyFactory.getInstance("DESede").generateSecret(new DESedeKeySpec(keyBytes));
    }

    Cipher cipher = Cipher.getInstance("DESede");
    cipher.init(2, key);
}
