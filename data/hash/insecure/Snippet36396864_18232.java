@Component
public class IlinqChecksumCalculator {

    private static Logger DATA_LOADER_CHECKSUM_CALCULATOR_LOGGER = Logger.getLogger(IlinqChecksumCalculator.class);

    public String calculateCheckSum(String rfsdata) throws IOException {

        System.out.println(rfsdata);
        String checkSumValue = null;
        if (StringUtils.isNotBlank(rfsdata)) {
            try {
                // Create MessageDigest object for MD5
                MessageDigest digest = MessageDigest.getInstance("MD5");

                // Update input string in message digest
                digest.update(rfsdata.getBytes(), 0, rfsdata.getBytes().length);

                // Converts message digest value in base 16 (hex)
                checkSumValue = new BigInteger(1, digest.digest()).toString(16);

            } catch (NoSuchAlgorithmException exception) {
                DATA_LOADER_CHECKSUM_CALCULATOR_LOGGER.error(
                        "Error in determineInputCheckSum() method during calculation of checksum for Input JSON String for ",
                        exception);
            }
        }
        System.out.println("Final checksum value is:" + checkSumValue);
        return checkSumValue;
    }

}
