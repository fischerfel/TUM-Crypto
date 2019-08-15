public class MessageDigestFactoryBean implements FactoryBean<MessageDigest>{

    private String algorithmName = "MD5";
    private MessageDigest messageDigest = null;

    @Override
    public MessageDigest getObject() throws Exception {
        System.out.println("<> MessageDigestFactoryBean.getObject()");
        return messageDigest;
    }

    @Override
    public Class<?> getObjectType() {
        System.out.println("<> MessageDigestFactoryBean.getObjectType()");
        return MessageDigest.class;
    }

    @Override
    public boolean isSingleton() {
        System.out.println("<> MessageDigestFactoryBean.isSingleton()");
        return true;
    }

    @PostConstruct
    public void postConstructHandler() throws NoSuchAlgorithmException {
        System.out.println("<> MessageDigestFactoryBean.postConstructHandler()");
        messageDigest = MessageDigest.getInstance(algorithmName);
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }
}
