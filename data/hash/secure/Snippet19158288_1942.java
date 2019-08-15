       while (true){
            long l = System.currentTimeMillis();
            MessageDigest md = MessageDigest.getInstance(hashAlgorithm);
            try (InputStream is = new BufferedInputStream(Files.newInputStream(Paths.get("bigFile.txt")))) {
                DigestInputStream dis = new DigestInputStream(is, md);
                int b;
                while ((b = dis.read()) != -1){
                }
            }
            byte[] digest = md.digest();
            System.out.println(System.currentTimeMillis() - l);
        }
