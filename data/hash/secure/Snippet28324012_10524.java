            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest((matcher.group(1)).getBytes("UTF-8"));
            String a = Base64.encode(hash);
