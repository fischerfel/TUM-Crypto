FacebookHashKey(){PackageInfo info;
     try{ 
        info =getPackageManager().getPackageInfo("com.facebook.android",PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA"); md.update(signature.toByteArray());
        String androidhashkey = new String(Base64.encode(md.digest(), 0));// 
        String androidhashkey = new String(Base64.encodeBytes(md.digest()));//PRINT THE KEY IN THE LOGCAT 
        Log.e("GPS Citygames Activity FB HASH KEY:", androidhashkey);
        }
    }catch (NameNotFoundException e1) {    
      Log.e("GPS Citygames Activity name not found exception", e1.toString());
    }catch (NoSuchAlgorithmException e) 
    {
     Log.e("GPS Citygames Activity no such an algorithm exception", e.toString());

    }catch (Exception e){

    Log.e("exception", e.toString());

    }
    }
