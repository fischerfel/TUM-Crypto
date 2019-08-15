from(ftp_endpoint)
    .idempotentConsumer(simple("${in.header.CamelFileName}"), redisIdempotentRepository)
    .setHeader(S3Constants.KEY, simple("${file:name}"))
    .process(md5HeadersProcessor)
    .to(s3_endpoint)


public class Md5HeadersProcessor implements Processor {
    private java.util.Base64.Encoder encoder = java.util.Base64.getEncoder();

    @Override
    public void process(Exchange exchange) throws NoSuchAlgorithmException {
        byte[] bytes = exchange.getIn().getBody(byte[].class);
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(bytes);
        String md5= encoder.encodeToString(md.digest());
        exchange.getIn().setHeader(S3Constants.CONTENT_MD5, md5);
    }
}
