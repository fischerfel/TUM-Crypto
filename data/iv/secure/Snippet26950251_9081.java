//note: the initialization vector (IV) must be 16 bytes in this case
//so, if a user password is being used to create it, measures must
//be taken to ensure proper IV length; random iv is best and should be
//stored, possibly alongside the encrypted data
IvParameterSpec ivSpec = new IvParameterSpec(password.getBytes("UTF-8"));
