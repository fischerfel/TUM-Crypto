private static Promise<String> toSHA1(final byte[] bytes) {       
    return Promise.promise(new Function0<String>() {
        @Override
        public String apply() throws Throwable {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] digestResult = digest.digest(bytes);
            String hexResult = "";
            for (int i = 0; i < digestResult.length; i++) {
                hexResult += Integer.toString(( bytes[i] & 0xff ) + 0x100, 16).substring(1);
            }
            return hexResult;
        }
    });
}
