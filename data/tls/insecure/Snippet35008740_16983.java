def keyStorePath = "E:\\ws.jks";
def keyStorePassword = "cgpwd";
def trustStorePath = "E:\\ws.jks";
def trustStorePassword = "cgpwd";

def keyStoreFactory = javax.net.ssl.KeyManagerFactory.getInstance("SUNX509");
def trustStoreFactory = javax.net.ssl.TrustManagerFactory.getInstance("SUNX509");
def keyStore = java.security.KeyStore.getInstance("JKS");
def trustStore = java.security.KeyStore.getInstance("JKS");

def keyInput = new FileInputStream(keyStorePath);
keyStore.load(keyInput, keyStorePassword.toCharArray());
keyInput.close();

def trustInput = new FileInputStream(trustStorePath);
trustStore.load(trustInput, trustStorePassword.toCharArray());
trustInput.close();

keyStoreFactory.init(keyStore, keyStorePassword.toCharArray());
trustStoreFactory.init(trustStore);

def sslContext = javax.net.ssl.SSLContext.getInstance("TLS");
sslContext.init(keyStoreFactory.getKeyManagers(), trustStoreFactory.getTrustManagers(), new java.security.SecureRandom());

def sslFactory = sslContext.getSocketFactory();

def soapRequest = mockRequest.requestContent;
def soapUrl = new URL("https://test.webservice.dummyservice.com");
def connection = soapUrl.openConnection();

connection.setRequestMethod("POST");
connection.setRequestProperty("Accept-Encoding" ,"gzip,deflate");
connection.setRequestProperty("Content-Type" ,"text/xml;charset=UTF-8");
connection.setRequestProperty("SOAPAction", mockRequest.getSoapAction());
//connection.setRequestProperty("Content-Length", "13986");
//connection.setRequestProperty("Connection", "Keep-Alive");
//connection.setRequestProperty("User-Agent", "Apache-HttpClient/4.1.1 (java 1.5)");
//connection.setRequestProperty("Host", "test.webservice.dummyservice.com");
connection.setSSLSocketFactory(sslFactory);
connection.doOutput = true;

Writer writer = new OutputStreamWriter(connection.outputStream);
writer.write(soapRequest);
writer.flush();
writer.close();

connection.connect();
def soapResponse = connection.content.text;

requestContext.responseMessage = soapResponse;
