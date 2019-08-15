public String hash(String email) {
    MessageDigest digester = MessageDigest.getInstance("SHA-256");
    digester.update(email.getBytes(StandardCharsets.UTF_8)));
    digester.update("RanDOmComplCatEdSalt647826583745".getBytes(StandardCharsets.UTF_8));
    return Base64.getEncoder().encodeToString(digester.digest());
}

String email = "dude@example.com";
sendEmailWithLink("/verify?email=" + email + "&hash=" + hash(email));
