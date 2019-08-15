String initialVectorString = Helper.randomString(CHARSET_AZ_09, 16);
// ....
IvParameterSpec initialVector = new IvParameterSpec(
    (new org.apache.commons.codec.binary.Base64()).decode(
    initialVectorString.getBytes()));
