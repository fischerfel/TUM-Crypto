def groovyUtils = new com.eviware.soapui.support.GroovyUtils( context )

// load JKS (need for https)
def keyStorePath = "keystore.jks"
def keyStorePassword = "keystore_pswd"
def trustStorePath = "truststore.jks"
def trustStorePassword = "truststore_pswd"

def keyStoreFactory = javax.net.ssl.KeyManagerFactory.getInstance("SUNX509")
def trustStoreFactory = javax.net.ssl.TrustManagerFactory.getInstance("SUNX509")
def keyStore = java.security.KeyStore.getInstance("JKS")
def trustStore = java.security.KeyStore.getInstance("JKS")

def keyInput = new FileInputStream(keyStorePath)
keyStore.load(keyInput, keyStorePassword.toCharArray())
keyInput.close()

def trustInput = new FileInputStream(trustStorePath)
trustStore.load(trustInput, trustStorePassword.toCharArray())
trustInput.close()

keyStoreFactory.init(keyStore, keyStorePassword.toCharArray())
trustStoreFactory.init(trustStore)

def sslContext = javax.net.ssl.SSLContext.getInstance("TLS")
sslContext.init(keyStoreFactory.getKeyManagers(), trustStoreFactory.getTrustManagers(), new java.security.SecureRandom())

def sslFactory = sslContext.getSocketFactory()

// Send received request to https://example.org/soap/service
def soapRequest = mockRequest.requestContent
def soapUrl = new URL("https://example.org/soap/service")
def connection = soapUrl.openConnection()

connection.setRequestMethod("POST")
connection.setRequestProperty("Content-Type" ,"text/html")
connection.setRequestProperty("SOAPAction", "")
connection.setSSLSocketFactory(sslFactory)
connection.doOutput = true

Writer writer = new OutputStreamWriter(connection.outputStream);
writer.write(soapRequest)
writer.flush()
writer.close()
connection.connect()

def soapResponse = connection.content.text
def responseXPathHelper = groovyUtils.getXmlHolder(soapResponse)

responseXPathHelper.declareNamespace("envelop", "http://www.w3.org/2003/05/soap-envelope")
responseXPathHelper.declareNamespace("msg", "https://example.org/soap/service")

// Here you can modify condition (Where you want to spoofing)
if ("ExampleResponse".equals(responseXPathHelper["/envelop:Envelope/envelop:Body/*/local-name()"]))    // spoofing
{
    def requestXPathHelper = groovyUtils.getXmlHolder(mockRequest.requestContent)
    requestXPathHelper.declareNamespace("envelop", "http://www.w3.org/2003/05/soap-envelope")

    def messageValue = "spoofed response : " + requestXPathHelper[/envelop:Envelope/envelop:Body/msg:ExampleResponse/text()]

    def responseTemplate = """<S:Envelope xmlns:S="http://www.w3.org/2003/05/soap-envelope">
        <S:Body>
            <ExampleResponse xmlns="https://example.org/soap/service">
                ${messageValue}
            </ExampleResponse>
        </S:Body>
    </S:Envelope>"""

    requestContext.responseMessage = responseTemplate
}
// work as proxy, i.e. just return response recieved from example.org/soap/service
else     requestContext.responseMessage = soapResponse
