@WebService(
  targetNamespace = "http://com.redsigil/soapserver",
  name = "com.redsigil.soapserver",
  portName = "soap",
  serviceName = "SoapServerSample")
@BindingType(javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true, required = false)
class SoapServerImpl() {
  @WebMethod(action = "hashing")
  def test(@WebParam(name = "mystr", mode = WebParam.Mode.IN) value: String): String = sha256Hash(value)

  def sha256Hash(text: String): String =
    String.format("%064x",
      new java.math.BigInteger(1, java.security.MessageDigest.getInstance("SHA-256").digest(text.getBytes("UTF-8"))))
}
