ByteArrayOutputStream bos = new ByteArrayOutputStream();
bos.write("password".getBytes(StandardCharsets.US_ASCII));
bos.write("JUQLSPOYGFURMGSDRYWIWBIWP".getBytes(StandardCharsets.US_ASCII));

MessageDigest md = MessageDigest.getInstance("SHA-256");
byte[] out = md.digest(bos.toByteArray());

System.out.println("hex = " + new HexBinaryAdapter().marshal(out).toLowerCase());
