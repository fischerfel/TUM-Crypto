String getSHA(byte[] sf,String sn,byte[] mac) throws NoSuchAlgorithmException 
    {

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(sf);
        md.update(sn.getBytes());
        byte[] hash = md.digest(mac);
        StringBuilder sb = new StringBuilder();
        for(byte b : hash) {
            sb.append(String.format("%02x", b));
        }


       return sb.toString();
    }
