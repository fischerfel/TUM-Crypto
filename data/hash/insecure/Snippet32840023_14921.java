import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.security.MessageDigest;

public class SampleDigesterFactory implements FactoryBean<MessageDigest>, InitializingBean {

    MessageDigest messageDigest;

    String algorithmName = "MD5";

    public MessageDigest getObject() throws Exception {
        return messageDigest;
    }

    public Class<?> getObjectType() {
        return MessageDigest.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public void afterPropertiesSet() throws Exception {
        messageDigest = MessageDigest.getInstance(algorithmName);
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }
}


import java.security.MessageDigest;

public class SampleDigester {

    private MessageDigest messageDigest;

    public void digestMessage(String message) {
        System.out.println("digest message:" + message);
        System.out.println("result: " + messageDigest.digest(message.getBytes()));
    }

    public MessageDigest getMessageDigest() {
        return messageDigest;
    }

    public void setMessageDigest(MessageDigest messageDigest) {
        this.messageDigest = messageDigest;
    }
}
