FileInputStream fis = new FileInputStream("res/myjksstore.jks");
KeyStore trusted = KeyStore.getInstance("JKS");
trusted.load(fis, "ez24get".toCharArray());           

TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
trustManagerFactory.init(trusted);

SSLContext context = SSLContext.getInstance("SSL");
context.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

Socket socket = context.getSocketFactory().createSocket("localhost", 443);

String str = "abc123";

socket.getOutputStream().write(GeneralUtil.toByta(str.length()));
socket.getOutputStream().write(str.getBytes());

socket.setKeepAlive(true);
