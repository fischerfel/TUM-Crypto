        val md5 = MessageDigest.getInstance("MD5")
        val hash = BigInteger(1, md5.digest(queryToSign.toByteArray(Charset.defaultCharset()))).toString(16)
