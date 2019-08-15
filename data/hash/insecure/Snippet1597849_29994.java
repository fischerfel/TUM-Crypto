    MessageDigest md5 = MessageDigest.getInstance("MD5");

    if (md5 != null) {
        md5.reset();
        newHashByte = md5.digest(msg.getBytes());
    }

    newHash = convertToString(newHashByte);
