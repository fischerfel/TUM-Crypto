String strEncrypted = null;
Cipher cipher = null;
IvParameterSpec ivSpec = null;
byte[] btEncrypted = null;

try
{
    cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    ivSpec = new IvParameterSpec(m_btIV);

    cipher.init(Cipher.ENCRYPT_MODE, m_KeySpec, ivSpec);
    btEncrypted = cipher.doFinal(strData.getBytes(m_strCharSet));
    strEncrypted = Base64.encodeToString(btEncrypted, Base64.NO_PADDING | Base64.NO_WRAP);
}
catch(Exception e)
{
    e.printStackTrace();
}

return strEncrypted;
