md = MessageDigest.getInstance("SHA-256");
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        sha1hash = md.digest();
