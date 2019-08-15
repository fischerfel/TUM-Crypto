private String calculateMAC(final String macAlgorithm, final String normalizedString, final String keyString) {
    try {
        final SecretKeySpec keySpec = new SecretKeySpec((keyString).getBytes("UTF-8"), macAlgorithm);
        final Mac mac = Mac.getInstance(macAlgorithm);
        mac.init(keySpec);

        return Base64.encodeToString(mac.doFinal(normalizedString.getBytes("UTF-8")), Base64.NO_WRAP);
    } catch (final Exception e) {
        Log.e("Error", e.toString());
        return null;
    }
}
