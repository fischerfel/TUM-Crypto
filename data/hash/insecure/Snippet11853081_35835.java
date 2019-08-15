public class MD5OutInterceptor extends AbstractPhaseInterceptor<Message> {

    public MD5OutInterceptor () {
        super(Phase.MARSHAL);
    }

    public final void handleMessage(Message message) {


        OutputStream os = message.getContent(OutputStream.class);
        if (os == null) {
            return;
        }

        final CachedOutputStream cos = new CachedOutputStream(os);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        cos.writeCacheTo(baos);

        // calculate MD5
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] mdBytes = md.digest(baos);

        MultivaluedMap<String, Object> headers = (MetadataMap<String, Object>) message.get(Message.PROTOCOL_HEADERS);

        if (headers == null) {
            headers = new MetadataMap<String, Object>();
        }             

        headers.add("MD5-Header", new String(mdBytes));
        message.put(Message.PROTOCOL_HEADERS, headers);
    }
}
