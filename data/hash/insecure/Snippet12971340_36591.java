public class UnhashingDataSource extends DelegatingDataSource {

    private static final Logger LOGGER = Logger.getLogger(UnhashingDataSource.class);
    private static final int HEX_RADIX = 16;
    private static final String DB_PASS = "a_sample_password";

    @Override
    public Connection getConnection() throws SQLException {
        DriverManagerDataSource dataSource = (DriverManagerDataSource) getTargetDataSource();
        return getConnection(dataSource.getUsername(), dataSource.getPassword());
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        try {
            DataSource datasource = getTargetDataSource();
            if (datasource == null) {
                throw new RuntimeException("targetDataSource is null");
            }
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.reset();
            md.update(DB_PASS.getBytes());
            if (password.equals(getHexString(md.digest()))) {
                return datasource.getConnection(username, DB_PASS);
            } else {
                throw new RuntimeException("Unable to connect to DB");
            }
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("Unknown algorithm");
        }
        return null;
    }

    private String getHexString(final byte[] messageDigest) {
        BigInteger bigInt = new BigInteger(1, messageDigest);
        return bigInt.toString(HEX_RADIX);
    }
}
