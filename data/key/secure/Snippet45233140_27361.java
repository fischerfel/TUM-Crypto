public String generateSignature(String requestPath, String method, String body, String timestamp) {
        try {
            String prehash = timestamp + method.toUpperCase() + requestPath + body;
            byte[] secretDecoded = Base64.getDecoder().decode(secretKey);
            SecretKeySpec keyspec = new SecretKeySpec(secretDecoded, "HmacSHA256");
            Mac sha256 = (Mac) Mac.getInstance("HmacSHA256").clone();
            sha256.init(keyspec);
            return Base64.getEncoder().encodeToString(sha256.doFinal(prehash.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
