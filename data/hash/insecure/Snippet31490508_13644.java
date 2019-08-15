import java.security.MessageDigest

class City {

  String id
  Date dateCreated
  Date lastUpdated
  String name


  static constraints = {
    id maxSize: 16, unique: true
    name nullable: false, blank: false
  }


  static mapping = {
    id generator:'assigned'
  }


  def setIdIfMissing() {
    if (!id) {
      String uuid = UUID.randomUUID().toString()
      MessageDigest sha1 = MessageDigest.getInstance("SHA1")
      byte[] digest  = sha1.digest(uuid.getBytes())
      def tmpId = new  BigInteger(1, digest).toString(16)
      id = tmpId[0..15] // size of the id
    }
  }

  /**
   * Constructor
   */
  City() {
    setIdIfMissing()
  }

}
