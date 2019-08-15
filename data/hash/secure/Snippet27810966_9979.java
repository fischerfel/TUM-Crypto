static encode = { String s ->
        print(s)
        MessageDigest md = MessageDigest.getInstance('SHA')
        print(md)
        md.update s.getBytes('UTF-8')
        def result = Base64.encodeBase64 md.digest()
        new String(result, "UTF-8");
    }
