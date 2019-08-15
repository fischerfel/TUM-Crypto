        MessageDigest md = MessageDigest.getInstance("SHA1");
        md.update(text.getBytes("utf-16le"));
        byte digest[] = md.digest();
        // Convert to hex or Base64
