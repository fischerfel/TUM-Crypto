String partitionKey = "YourKnownKey";
byte[] partitionBytes = partitionKey.getBytes("UTF-8");
byte[] hashBytes = MessageDigest.getInstance("MD5").digest(partitionBytes);
BigInteger biPartitionKey = new BigInteger(1, hashBytes);
