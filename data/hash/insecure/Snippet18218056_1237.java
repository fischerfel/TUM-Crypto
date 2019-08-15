public static String sha1(String s) {
    MessageDigest digest = null;
    try {
        digest = MessageDigest.getInstance("SHA-1");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    digest.reset();
    byte[] data = digest.digest(s.getBytes());
    return String.format("%0" + (data.length * 2) + "X", new BigInteger(1, data));
}


-(NSString*) sha1:(NSString*)input
{
    const char *cstr = [input cStringUsingEncoding:NSUTF8StringEncoding];
    NSData *data = [NSData dataWithBytes:cstr length:input.length];
    uint8_t digest[CC_SHA1_DIGEST_LENGTH];
    CC_SHA1(data.bytes, data.length, digest);
    NSMutableString* output = [NSMutableString stringWithCapacity:CC_SHA1_DIGEST_LENGTH * 2];
    for(int i = 0; i < CC_SHA1_DIGEST_LENGTH; i++)
        [output appendFormat:@"%02X", digest[i]];
    return output;

}
