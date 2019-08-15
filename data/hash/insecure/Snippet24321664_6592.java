    try {
        Log.d("Checking signs", "Signs");
        PackageInfo info = getPackageManager().getPackageInfo(
                this.getPackageName(), PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            System.out.print(Base64.encodeToString(md.digest(),
                    Base64.DEFAULT));

        }
    } catch (NameNotFoundException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
}
