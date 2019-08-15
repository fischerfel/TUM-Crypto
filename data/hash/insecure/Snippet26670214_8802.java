String path = "your complete file path";
MessageDigest md = MessageDigest.getInstance("MD5");
md.update(Files.readAllBytes(Paths.get(path)));
byte[] digest = md.digest();
