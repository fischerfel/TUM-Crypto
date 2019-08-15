def calcSha1 = { file ->
    MessageDigest md = MessageDigest.getInstance("SHA-1");
    file.eachByte 4096, {bytes, size ->
        md.update(bytes, 0, size);
    }
    return md.digest().collect {String.format "%02x", it}.join();
}
