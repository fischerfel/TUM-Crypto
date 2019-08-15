byte[] ivBytes = new byte[16];
byte[] ivBytesShort = fromHex(initVector);
System.arraycopy(ivBytesShort, 0, ivBytes, 0, ivBytesShort.length);
IvParameterSpec iv = new IvParameterSpec(ivBytes);
SecretKeySpec skeySpec = new SecretKeySpec(fromHex(key), "AES");
