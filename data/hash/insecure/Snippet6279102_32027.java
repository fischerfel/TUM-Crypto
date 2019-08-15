public class ContentMD5Filter extends ClientFilter {

    private static final class ContentMD5Adapter extends AbstractClientRequestAdapter {
        ContentMD5Adapter(final ClientRequestAdapter cra) {
            super(cra);
        }

        @Override
        public OutputStream adapt(final ClientRequest request, final OutputStream out) throws IOException {
            try {
                MessageDigest instance = MessageDigest.getInstance("MD5");
                request.getHeaders().add("Content-MD5", instance);
                return new DigestOutputStream(out, instance);
            } catch (NoSuchAlgorithmException e) {
                throw new WebApplicationException();
            }
        }
    }

    @Override
    public ClientResponse handle(final ClientRequest cr) throws ClientHandlerException {
        cr.setAdapter(new ContentMD5Adapter(cr.getAdapter()));
        return getNext().handle(cr);
    }
}
