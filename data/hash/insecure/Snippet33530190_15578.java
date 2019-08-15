    private void getAppKeyHash() {
    try {
        PackageInfo info = getPackageManager().getPackageInfo(
                getPackageName(), PackageManager.GET_SIGNATURES);
        for (Signature signature : info.signatures) {
            MessageDigest md;

            md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            String something = new String(Base64.encode(md.digest(), 0));
            System.out.println("HASH  " + something);
            showSignedHashKey(something);

        }
    } catch (NameNotFoundException e1) {
        // TODO Auto-generated catch block
        Log.e("name not found", e1.toString());
    } catch (NoSuchAlgorithmException e) {

        Log.e("no such an algorithm", e.toString());
    } catch (Exception e) {
        Log.e("exception", e.toString());
    }
}
public void showSignedHashKey(String hashKey) {

    AlertDialog.Builder adb = new AlertDialog.Builder(this);
    adb.setTitle("Note Signed Hash Key");
    adb.setMessage(hashKey);
    adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {

        }
    });

    adb.show();
}
