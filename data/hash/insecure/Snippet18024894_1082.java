class Book {

  String id

  static constraints = {
    id maxSize: 18
  }

  static mapping = {
    id generator:'assigned'
  }

def beforeValidate() {
    if (!id) {
        String uuid = UUID.randomUUID().toString()
        MessageDigest sha1 = MessageDigest.getInstance("SHA1")
        byte[] digest  = sha1.digest(uuid.getBytes())
        def tmpId = new  BigInteger(1, digest).toString(16)
        id = tmpId[0..n]    // size of the id
    }
}

}
