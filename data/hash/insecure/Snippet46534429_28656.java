    byte[] a = ...some byte array...;
    byte[] b = ...some byte array...;

    MessageDigest m_Hash = MessageDigest.getInstance("SHA-1");
    m_Hash.update(a);
    m_Hash.update(b);
    byte[] ub = m_Hash.digest();
