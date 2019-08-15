        final MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(str.getBytes("UTF-8"), 0, str.length());
        sha1hash = md.digest();
