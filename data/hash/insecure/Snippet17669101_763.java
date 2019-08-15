PackageInfo packageInfo;
        try {
        packageInfo = getPackageManager().getPackageInfo("com.yourapp", 
PackageManager.GET_SIGNATURES);
        for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String key = new String(Base64.encode(md.digest(), 0));
                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Hash key", key);
        } 
        }
        catch (NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        }

        catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        }
        catch (Exception e){
            Log.e("Exception", e.toString());
        }
