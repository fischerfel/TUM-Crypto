try
    {
        PackageInfo info = getPackageManager().getPackageInfo(getApplication().getPackageName(), PackageManager.GET_SIGNATURES);
        for(Signature signature : info.signatures)
        {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(signature.toByteArray());
            String myHashCode = Base64.encodeBytes(md.digest());
            Log.e("-------------FACEBOOK HASH KEY-----------", myHashCode);
        }
    }
    catch(NameNotFoundException e)
    {
        e.printStackTrace();
    }
    catch(NoSuchAlgorithmException e)
    {
        e.printStackTrace();
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
    catch(Error e)
    {
        e.printStackTrace();
    }
