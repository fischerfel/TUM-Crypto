String key = "dGhlIHNhbXBsZSBub25jZQ==258EAFA5-E914-47DA-95CA-C5AB0DC85B11";

MessageDigest md = MessageDigest.getInstance("SHA-1");

//s3pPLMBiTxaQ9kYGzzhZRbK+xOo=
System.out.println(Base64.encodeBase64String(md.digest(key.getBytes())));
