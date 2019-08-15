@Test
    public void testGenerateKeyCredentials(){

    String certFile = "/etc/abc/server2.crt";
    System.out.printf("Generating keyCredentials entry from %s\n", certFile);


    try {
        FileInputStream certFileIn = new FileInputStream(certFile);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate cert = cf.generateCertificate(certFileIn);

        // Generate base64-encoded version of the cert's data
        // for the "value" property of the "keyCredentials" entry
        byte[] certData = cert.getEncoded();
        String certValue = Base64.getEncoder().encodeToString(certData);
        System.out.println("Cert value: " + certValue);

        // Generate the SHA1-hash of the cert for the "customKeyIdentifier"
        // property of the "keyCredentials" entry
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        md.update(certData);
        String certCustomKeyId = Base64.getEncoder().encodeToString(md.digest());
        System.out.println("Cert custom key ID: " + certCustomKeyId);

        FileWriter fw = new FileWriter("keycredentials.txt", false);
        PrintWriter pw = new PrintWriter(fw);

        pw.println("\"keyCredentials\": [");
        pw.println("  {");
        pw.println("    \"customKeyIdentifier\": \"" + certCustomKeyId + "\",");
        pw.println("    \"keyId\": \"" + UUID.randomUUID().toString() + "\",");
        pw.println("    \"type\": \"AsymmetricX509Cert\",");
        pw.println("    \"usage\": \"Verify\",");
        pw.println("    \"value\": \"" + certValue + "\"");
        pw.println("  }");
        pw.println("],");

        pw.close();

        System.out.println("Key credentials written to keycredentials.txt");
    } catch (FileNotFoundException e) {
        System.out.printf("ERROR: Cannot find %s\n", certFile);
    } catch (CertificateException e) {
        System.out.println("ERROR: Cannot instantiate X.509 certificate");
    } catch (NoSuchAlgorithmException e) {
        System.out.println("ERROR: Cannot instantiate SHA-1 algorithm");
    } catch (IOException e) {
        System.out.println("ERROR: Cannot write to keycredentials.txt");
    }
}
