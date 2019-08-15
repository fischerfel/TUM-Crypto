byte[] b = Files.readAllBytes(Paths.get("/path/to/file"));
byte[] hash = MessageDigest.getInstance("MD5").digest(b);
