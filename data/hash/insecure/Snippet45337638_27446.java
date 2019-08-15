val to_encode = "Hello World"
val md5hash = MessageDigest.getInstance("MD5")
 .digest(to_encode.trim().getBytes())
val md5string = md5hash.map("%02x-".format(_)).mkString
val uuid_bytes = UUID.nameUUIDFromBytes(to_encode.trim().getBytes())
printf("String to encode: %s\n", to_encode)
printf("MD5: %s\n", md5string)
printf("UUID: %s\n", uuid_bytes.toString)
