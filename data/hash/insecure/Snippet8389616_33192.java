   val md = MessageDigest.getInstance("MD5")
   val messageDigest = md.digest(fileName.getBytes)
   val md5 = (new BigInteger(1, messageDigest)).toString(16)

   val hash1 = md5.substring(0, 1)
   val hash2 = md5.substring(0, 2);

   val urlPart = hash1 + "/" + hash2 + "/" + fileName
