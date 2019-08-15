private byte[] getRC4Key(BigInteger paramBigInteger)
   {
    try
{
  MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
  localMessageDigest.reset();
  localMessageDigest.update(ServerModel.getInstance().getConnection().getPassword().getBytes("ISO-8859-1"));
  byte[] arrayOfByte = localMessageDigest.digest();
  localMessageDigest.reset();
  localMessageDigest.update(arrayOfByte);
  localMessageDigest.update(ByteArraysUtils.BigInteger2ByteArray(paramBigInteger));
  return localMessageDigest.digest();
}
catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
{
  LOGGER.severe(localNoSuchAlgorithmException.getMessage());
}
catch (PbxOperationException localPbxOperationException)
{
  LOGGER.severe(localPbxOperationException.getMessage());
}
catch (UnsupportedEncodingException localUnsupportedEncodingException)
{
  LOGGER.severe(localUnsupportedEncodingException.getMessage());
}
return null;
}
