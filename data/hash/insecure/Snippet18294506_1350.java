/**
 * Computes the default serial version UID value for the given class.
 */
private static long computeDefaultSUID(Class<?> cl) {
    if (!Serializable.class.isAssignableFrom(cl) || Proxy.isProxyClass(cl))
    {
        return 0L;
    }

    try {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        DataOutputStream dout = new DataOutputStream(bout);

        dout.writeUTF(cl.getName());

        int classMods = cl.getModifiers() &
            (Modifier.PUBLIC | Modifier.FINAL |
             Modifier.INTERFACE | Modifier.ABSTRACT);

        /*
         * compensate for javac bug in which ABSTRACT bit was set for an
         * interface only if the interface declared methods
         */
        Method[] methods = cl.getDeclaredMethods();
        if ((classMods & Modifier.INTERFACE) != 0) {
            classMods = (methods.length > 0) ?
                (classMods | Modifier.ABSTRACT) :
                (classMods & ~Modifier.ABSTRACT);
        }
        dout.writeInt(classMods);

        if (!cl.isArray()) {
            /*
             * compensate for change in 1.2FCS in which
             * Class.getInterfaces() was modified to return Cloneable and
             * Serializable for array classes.
             */
            Class<?>[] interfaces = cl.getInterfaces();
            String[] ifaceNames = new String[interfaces.length];
            for (int i = 0; i < interfaces.length; i++) {
                ifaceNames[i] = interfaces[i].getName();
            }
            Arrays.sort(ifaceNames);
            for (int i = 0; i < ifaceNames.length; i++) {
                dout.writeUTF(ifaceNames[i]);
            }
        }

        Field[] fields = cl.getDeclaredFields();
        MemberSignature[] fieldSigs = new MemberSignature[fields.length];
        for (int i = 0; i < fields.length; i++) {
            fieldSigs[i] = new MemberSignature(fields[i]);
        }
        Arrays.sort(fieldSigs, new Comparator<MemberSignature>() {
            public int compare(MemberSignature ms1, MemberSignature ms2) {
                return ms1.name.compareTo(ms2.name);
            }
        });
        for (int i = 0; i < fieldSigs.length; i++) {
            MemberSignature sig = fieldSigs[i];
            int mods = sig.member.getModifiers() &
                (Modifier.PUBLIC | Modifier.PRIVATE | Modifier.PROTECTED |
                 Modifier.STATIC | Modifier.FINAL | Modifier.VOLATILE |
                 Modifier.TRANSIENT);
            if (((mods & Modifier.PRIVATE) == 0) ||
                ((mods & (Modifier.STATIC | Modifier.TRANSIENT)) == 0))
            {
                dout.writeUTF(sig.name);
                dout.writeInt(mods);
                dout.writeUTF(sig.signature);
            }
        }

        if (hasStaticInitializer(cl)) {
            dout.writeUTF("<clinit>");
            dout.writeInt(Modifier.STATIC);
            dout.writeUTF("()V");
        }

        Constructor[] cons = cl.getDeclaredConstructors();
        MemberSignature[] consSigs = new MemberSignature[cons.length];
        for (int i = 0; i < cons.length; i++) {
            consSigs[i] = new MemberSignature(cons[i]);
        }
        Arrays.sort(consSigs, new Comparator<MemberSignature>() {
            public int compare(MemberSignature ms1, MemberSignature ms2) {
                return ms1.signature.compareTo(ms2.signature);
            }
        });
        for (int i = 0; i < consSigs.length; i++) {
            MemberSignature sig = consSigs[i];
            int mods = sig.member.getModifiers() &
                (Modifier.PUBLIC | Modifier.PRIVATE | Modifier.PROTECTED |
                 Modifier.STATIC | Modifier.FINAL |
                 Modifier.SYNCHRONIZED | Modifier.NATIVE |
                 Modifier.ABSTRACT | Modifier.STRICT);
            if ((mods & Modifier.PRIVATE) == 0) {
                dout.writeUTF("<init>");
                dout.writeInt(mods);
                dout.writeUTF(sig.signature.replace('/', '.'));
            }
        }

        MemberSignature[] methSigs = new MemberSignature[methods.length];
        for (int i = 0; i < methods.length; i++) {
            methSigs[i] = new MemberSignature(methods[i]);
        }
        Arrays.sort(methSigs, new Comparator<MemberSignature>() {
            public int compare(MemberSignature ms1, MemberSignature ms2) {
                int comp = ms1.name.compareTo(ms2.name);
                if (comp == 0) {
                    comp = ms1.signature.compareTo(ms2.signature);
                }
                return comp;
            }
        });
        for (int i = 0; i < methSigs.length; i++) {
            MemberSignature sig = methSigs[i];
            int mods = sig.member.getModifiers() &
                (Modifier.PUBLIC | Modifier.PRIVATE | Modifier.PROTECTED |
                 Modifier.STATIC | Modifier.FINAL |
                 Modifier.SYNCHRONIZED | Modifier.NATIVE |
                 Modifier.ABSTRACT | Modifier.STRICT);
            if ((mods & Modifier.PRIVATE) == 0) {
                dout.writeUTF(sig.name);
                dout.writeInt(mods);
                dout.writeUTF(sig.signature.replace('/', '.'));
            }
        }

        dout.flush();

        MessageDigest md = MessageDigest.getInstance("SHA");
        byte[] hashBytes = md.digest(bout.toByteArray());
        long hash = 0;
        for (int i = Math.min(hashBytes.length, 8) - 1; i >= 0; i--) {
            hash = (hash << 8) | (hashBytes[i] & 0xFF);
        }
        return hash;
    } catch (IOException ex) {
        throw new InternalError();
    } catch (NoSuchAlgorithmException ex) {
        throw new SecurityException(ex.getMessage());
    }
}
