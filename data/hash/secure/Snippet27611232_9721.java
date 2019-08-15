package jweb

import java.security.MessageDigest

class Clients {

    int id
    String name
    String password
    String cart_id
    boolean admin
    static constraints = {
        id()
        name()
        password()
        cart_id nullable:true
        admin()
    }
    def beforeInsert(){
        encodePassword()
    }
    def beforeUpdate(){
        encodePassword()
    }
    def encodePassword(){
       MessageDigest md5Digest;
       byte[] digest;

       md5Digest = MessageDigest.getInstance("SHA-512");
       md5Digest.reset();
       md5Digest.update(password.getBytes());
       digest = md5Digest.digest();
       password = new BigInteger(1,digest).toString(16)
    }
}
