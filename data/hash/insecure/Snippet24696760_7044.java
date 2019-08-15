def getMd5(content: Array[Byte]) =
    try {
        val md = MessageDigest.getInstance("MD5")
        val bytes = md.digest(content)
        bytes.map(b => Integer.toHexString((b + 0x100) % 0x100)).mkString
    } catch {
        case ex: Throwable => null
    }
