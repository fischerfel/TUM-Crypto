SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(), HMAC_SHA1_ALGORITHM);

Mac mac = null;
mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
mac.init(signingKey);

byte[] bytes = mac.doFinal(signatureString.getBytes());

String form = "";
for (int i = 0; i < bytes.length; i++)
{
    String str = Integer.toHexString(((int)bytes[i]) & 0xff);
    if (str.length() == 1)
    {
        str = "0" + str;
    }

    form = form + str;
}
return form;
