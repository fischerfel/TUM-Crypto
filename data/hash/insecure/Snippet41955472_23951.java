import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.EqualsBuilder;

public class MyCustomObject {
    //variables, enums, interface ... here
    ...    
    public MyCustomObject(...){...}

    //mutch code here
    ...

    public String getChecksum() {
        String id = Integer.toString(hashCode());
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(id.getBytes("UTF-8"));
            id = DatatypeConverter.printHexBinary(digest);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            // do nothing here
        }
        return new id;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(-1013166723, 372138085)
        //if needed in extended classes: .appendSuper(super.hashCode())
        .append(...)
        ....
        .toHashCode();
    }

    @Override
    public boolean equals(
            final Object other) {
        if (!(other instanceof MyCustomObject)) {
            return false;
        }
        MyCustomObject castOther = (MyCustomObject) other;
        return new EqualsBuilder()
            // if needed in extended classes: .appendSuper(super.hashCode())
            .append(..., ...)
            ....
            .isEquals();
    }
}
