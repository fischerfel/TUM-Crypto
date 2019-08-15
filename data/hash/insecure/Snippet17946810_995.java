 try {
        byte[] bytesOfMessage = id.getBytes("UTF-8");
        log.error "bytesOfMessage length: " + bytesOfMessage.length
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(bytesOfMessage);

        md5Value = new String(thedigest);
        log.error "md5Value length: " + md5Value.length()
        log.error "md5Value bytes length: " + md5Value.getBytes().length
    } catch (UnsupportedEncodingException e) {
        log.error "[getMD5EncryptionKey]UnsupportedEncodingException: " + e;
    } catch (NoSuchAlgorithmException e) {
        log.error "[getMD5EncryptionKey]NoSuchAlgorithmException: " + e;
    }
