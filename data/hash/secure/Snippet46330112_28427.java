scala> String.format("%032x", new BigInteger(1, MessageDigest.getInstance("SHA-256").digest("some string".getBytes("UTF-8"))))
res4: String = 61d034473102d7dac305902770471fd50f4c5b26f6831a56dd90b5184b3c30fc
