 String hashfirst="12345678901234567890123456789077";
           MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hash = digest.digest(s.getBytes(StandardCharsets.UTF_8));
        System.out.println(bytesToHex(hash));
    }
    private static String bytesToHex(byte[] bytes) {
        StringBuffer result = new StringBuffer();
        for (byte b : bytes) result.append(Integer.toString((b & 0xFF) + 0x100, 16).substring(1));
        return result.toString();
    }
