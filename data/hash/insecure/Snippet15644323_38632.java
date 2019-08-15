        byte[] bytesOfMessage = "google.com".getBytes("UTF-8");

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] md5 = md.digest(bytesOfMessage);
        ByteBuffer bb = ByteBuffer.wrap(md5);
        LongBuffer ig = bb.asLongBuffer();
        return new UUID(ig.get(0), ig.get(1));
