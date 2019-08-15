PEMReader pr = new PEMReader(new FileReader("cert.pem"));
X509Certificate cert = (X509Certificate) pr.readObject();
PEMReader kr = new PEMReader(new FileReader("privkey.pem"),
        new PasswordFinder() {
    public char[] getPassword() {
        return "passphase".toCharArray();
    }
});
KeyPair key = (KeyPair) kr.readObject();
KeyStore ksKeys = KeyStore.getInstance("JKS");
ksKeys.load(null, "passphase".toCharArray());
ksKeys.setCertificateEntry("MyCert", cert);
ksKeys.setKeyEntry("Mykey", key.getPrivate(),
        "passphase".toCharArray(), new Certificate[]{cert});
KeyManagerFactory kmf = KeyManagerFactory.getInstance(
        KeyManagerFactory.getDefaultAlgorithm());
kmf.init(ksKeys, "passphase".toCharArray());
TrustManagerFactory tmf = TrustManagerFactory.getInstance(
        TrustManagerFactory.getDefaultAlgorithm());
tmf.init(ksKeys);

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

Socket socket = sslContext.getSocketFactory().createSocket(
        "localhost", 4433);
BufferedReader in = new BufferedReader(new InputStreamReader(
        socket.getInputStream()));
PrintWriter out = new PrintWriter(new OutputStreamWriter(
        socket.getOutputStream()));
out.println("Hello World");
System.out.println(in.readLine());
out.close();
in.close();
