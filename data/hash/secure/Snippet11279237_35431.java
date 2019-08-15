abstract class DigestComputor{


    String compute(DigestAlgorithm algorithm){
        MessageDigest instance;
        try {
            instance = MessageDigest.getInstance(algorithm.toString());
            updateMessageDigest(instance);

            return hex(instance.digest());
        } catch (NoSuchAlgorithmException e) {
            LOG.error(e.getMessage(), e);
            throw new UnsupportedOperationException(e.getMessage(), e);
        }
    }

    abstract void updateMessageDigest(MessageDigest instance);

}

class ByteBufferDigestComputor extends DigestComputor{

    private final ByteBuffer byteBuffer;

    public ByteBufferDigestComputor(ByteBuffer byteBuffer) {
        super();
        this.byteBuffer = byteBuffer;
    }

    @Override
    void updateMessageDigest(MessageDigest instance) {
        instance.update(byteBuffer);

    }

}

class InputStreamDigestComputor extends DigestComputor{


               // this place has error. due to exception. if I change the overrided method to throw it. evey caller will handle the exception. but 
    @Override
    void updateMessageDigest(MessageDigest instance) {
        throw new IOException();

    }

}
