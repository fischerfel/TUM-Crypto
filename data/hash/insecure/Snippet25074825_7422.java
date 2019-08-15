 String secAccept = sha1base64(secKey + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11");

 private String sha1base64(String str) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return Base64.encode(md.digest(str.getBytes()));
    }
