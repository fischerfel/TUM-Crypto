import org.apache.commons.codec.binary.Base64;
import org.apache.cxf.headers.Header;
import org.apache.cxf.jaxb.JAXBDataBinding;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;



public class CheckoutHeaderBuilder {

    public static Header buildHeader(String merchantId, String passKey, String timestamp) throws JAXBException, NoSuchAlgorithmException, UnsupportedEncodingException {
    String hashedEncodedPass = Base64.encodeBase64String(MessageDigest.getInstance("SHA-256").digest((merchantId + passKey + timestamp).getBytes("utf-8")));

    List<Header> headers = new ArrayList<>();
    CheckOutHeader checkOutHeader = new CheckOutHeader();
    checkOutHeader.setTIMESTAMP(timestamp);
    checkOutHeader.setMERCHANTID(merchantId);
    checkOutHeader.setPASSWORD(hashedEncodedPass);
    return new Header(new QName("tns:ns", "CheckOutHeader"), checkOutHeader, new JAXBDataBinding(CheckOutHeader.class));

    }

}
