String haha = sPassPhrase + sSaltValue;
byte[] abKey = haha.getBytes("US-ASCII");
MessageDigest oSHA1 = MessageDigest.getInstance("SHA-1");

for (int i = 1; i <= iIterations; i++) 
{
    abKey = oSHA1.digest(abKey);
}
