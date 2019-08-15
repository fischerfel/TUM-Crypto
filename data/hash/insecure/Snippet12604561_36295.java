MessageDigest digest = MessageDigest.getInstance("MD5");
digest.reset();
digest.update(output.getBytes());
byte[] outDigest = digest.digest();
BigInteger outBigInt = new BigInteger(1,outDigest);
output = outBigInt.toString(16);
    while (output.length() < 32){
    output = "0"+output;
    }
