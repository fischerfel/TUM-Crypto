public static void destroy(Key key) throws DestroyFailedException {
    if(Destroyable.class.isInstance(key)) {
        ((Destroyable)key).destroy();
    }
}

@Test
public void destroySecretKeySpec() {
    byte[] rawKey = new byte[32];
    new SecureRandom().nextBytes(rawKey);
    try {
        destroy(new SecretKeySpec(rawKey , "AES"));
    } catch(DestroyFailedException e) {
        Assert.fail();
    }
}
