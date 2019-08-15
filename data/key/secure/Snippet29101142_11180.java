byte[] data = Files.readAllBytes(Paths.get(fileName.replaceAll("myPath")));
key = new SecretKeySpec(data, "AES");
