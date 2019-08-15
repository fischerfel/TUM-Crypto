public class PooledCipherImpl implements PooledCipher {
    private final ObjectPool<PooledCipher> pool;
    private final Cipher cipher;
    private final String secretKeySpecAlgorithm;
    private boolean destroyOnClose = false;

    public PooledCipherImpl(ObjectPool<PooledCipher> pool, String transformation, int opmode, Key key, AlgorithmParameters params, String secretKeySpecAlgorithm) {
        this.pool = pool;
        this.cipher = Cipher.getInstance(transformation);
        this.cipher.init(opmode, key, params);
        this.secretKeySpecAlgorithm = secretKeySpecAlgorithm;
    }

    @Override
    public byte[] doFinal(byte[] bytes) throws Exception {
        try {
            return cipher.doFinal(bytes);
        } catch (Exception e) {
           destroyOnClose = true;
           throw e;
        }
    }

    @Override
    public SecretKeySpec getSecretKeySpec(byte[] bytes) {
        return new SecretKeySpec(doFinal(bytes), secretKeySpecAlgorithm);
    }

    @Override
    public void close() throws IOException {
        try {
            if (destroyOnClose) {
                pool.destroyObject(this);
            } else {
                pool.returnObject(this);
            }
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
