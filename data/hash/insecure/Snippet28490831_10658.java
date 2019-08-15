return new BigInteger(1, MessageDigest.getInstance("MD5").digest( s.getBytes()) ).mod(BigInteger.valueOf(100));
