import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;

import com.sun.jersey.api.client.AbstractClientRequestAdapter;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientRequestAdapter;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.filter.ClientFilter;

public class ContentMD5Filter extends ClientFilter {

    @Override
    public ClientResponse handle(final ClientRequest cr) throws ClientHandlerException {
        cr.setAdapter(new ContentMD5Adapter(cr.getAdapter()));
        return getNext().handle(cr);
    }

    private static final class ContentMD5Adapter extends AbstractClientRequestAdapter {
        ContentMD5Adapter(final ClientRequestAdapter cra) {
            super(cra);
        }

        @Override
        public OutputStream adapt(final ClientRequest request, final OutputStream out) throws IOException {
            return new ContentMD5OutputStream(request, getAdapter().adapt(request, out));
        }
    }

    private static final class ContentMD5OutputStream extends OutputStream {

        private final ByteArrayOutputStream byteArrayOutputStream;
        private final OutputStream jerseyStream;
        private final ClientRequest request;

        public ContentMD5OutputStream(final ClientRequest request, final OutputStream jerseyStream) {
            this.jerseyStream = jerseyStream;
            this.request = request;

            byteArrayOutputStream = new ByteArrayOutputStream();
        }

        @Override
        public void write(final int b) throws IOException {
            byteArrayOutputStream.write(b);
        }

        @Override
        public void close() throws IOException {
            byteArrayOutputStream.close();
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            setContentMD5Header(byteArray);
            IOUtils.write(byteArray, jerseyStream);
            jerseyStream.close();
        }

        private void setContentMD5Header(final byte[] data) {
            try {
                MessageDigest instance = MessageDigest.getInstance("MD5");
                String contentMD5 = new String(Base64.encodeBase64(instance.digest(data)));
                request.getHeaders().add("Content-MD5", contentMD5);
            } catch (NoSuchAlgorithmException e) {
                // Errorhandling. Should not happen though!
            }
        }
    }
}
