PackageManager pm = getPackageManager();
try {
    PackageInfo a = pm.getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
    byte[] raw = a.signatures[0].toByteArray();

    String sig = MessageDigest.getInstance("MD5").digest(raw).toString();

    _sourceTextEditor.setText(sig);
    }
    catch (Exception e) {

        return false;
    }
