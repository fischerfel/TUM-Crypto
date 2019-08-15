            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(motsDePasse.getBytes(StandardCharsets.UTF_8));
            String fileString = Base64.getEncoder().encodeToString(hash);
