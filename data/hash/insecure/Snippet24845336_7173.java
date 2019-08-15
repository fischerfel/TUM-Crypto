val sha1 = MessageDigest.getInstance("SHA1") 

calculateHash(sha1, "Hello", 5).get             

//> res0: Array[Byte] = Array(-9, -1, -98, -117, 123, -78, -32, -101, 112, -109, 90, 93, 120, 94, 12, -59, -39, -48, -85, -16)

calculateHash(sha1, "Too long!!!", 5)           

//> res1: Option[Array[Byte]] = None
