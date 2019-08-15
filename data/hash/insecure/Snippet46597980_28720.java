        val md5 = MessageDigest.getInstance("MD5")
        val hash = md5.digest(queryToSign.toByteArray(Charset.defaultCharset())).toString()
