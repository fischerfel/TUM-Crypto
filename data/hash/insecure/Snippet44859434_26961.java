 /**
 * 
 * @param id
 * @return
 */
public static final String getGeneratedId(final String id) {

    try {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(id);
        oos.close();
        MessageDigest m = MessageDigest.getInstance("SHA1");
        m.update(baos.toByteArray());
        return new BigInteger(1, m.digest()).toString(16);
    } catch (Exception e) {
        ICAMPPLogger.logException(CLASSNAME, "error in creating uuid" + e);
        ICAMPPUtils.getStackTraceToString(e);
        return BigInteger.ZERO.toString();
    }
}
public static void main(String[] args) {
    String token = getGeneratedId("testing");
    System.out.println(token);
}
ouput is : 1305340859400297508806692338645894167742475232778
But i would like to limit length to less than 10 . is it posssible
