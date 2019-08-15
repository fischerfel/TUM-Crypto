public static String generateSignature(String secrete, String pathAndQuery){
        String encoded = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(secrete.getBytes("UTF-8"));
            md.update(pathAndQuery.getBytes("UTF-8"));
            byte[] digest = md.digest();
            encoded = Base64.getEncoder().encodeToString(digest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encoded;
    }
