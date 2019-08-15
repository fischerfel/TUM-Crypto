public static String aesDecrypt(byte[] strBytes, String keyStr) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/ECB/NOPadding");
    KeyGenerator kgen = KeyGenerator.getInstance("AES");
    kgen.init(128, new SecureRandom(keyStr.getBytes()));
    SecretKeySpec key = new SecretKeySpec(keyStr.getBytes(), "AES");

    cipher.init(Cipher.DECRYPT_MODE, key);
    byte[] bytes = cipher.doFinal(strBytes);
    return new String(bytes, "gb2312");
}

public static byte[] convertStrArrayToByteArray(String s){
    String[] ss = s.split(";");
    byte[] bs = new byte[ss.length];
    int index = 0;
    for (String byteStr : ss) {
        bs[index ++] = (byte) (Short.parseShort(byteStr));
    }
    return bs;
}

public static void main(String[] args) throws Exception {
    byte b = (byte) 158;
    System.out.println(b);
    String enStr = "158;244;75;86;184;135;189;50;161;55;60;169;144;186;65;76;37;241;197;21;71;105;113;29;114;92;200;99;102;119;240;124;228;195;12;115;162;186;197;27;40;23;48;24;30;0;98;28;6;113;40;252;191;223;59;138;207;70;31;244;1;9;1;95;66;209;189;115;113;241;122;175;246;155;6;114;221;161;149;246;167;137;27;61;180;122;145;251;52;202;126;242;25;214;129;66;182;176;9;155;36;224;49;158;94;93;53;194;184;46;194;82;203;79;68;185;154;6;182;121;132;233;166;138;209;159;191;126;3;36;113;5;38;84;58;145;78;118;177;222;216;160;217;204;169;153;3;40;198;4;144;137;0;60;96;69;96;4;47;60;69;202;131;250;137;162;192;216;0;95;75;47;3;72;219;85;13;33;88;68;135;239;221;114;171;190;114;128;168;156;230;180;120;251;70;48;151;23;254;221;73;90;111;159;150;22;50;108;133;233;226;157;165;254;14;242;59;176;100;81;27;156;110;194;6;113;40;252;191;223;59;138;207;70;31;244;1;9;1;95;66;209;189;115;113;241;122;175;246;155;6;114;221;161;149;246;145;77;98;181;148;212;44;112;175;96;184;222;128;172;98;31;147;59;158;66;238;255;8;6;100;215;35;228;28;197;52;168;252;239;80;176;80;195;177;197;42;252;47;184;235;64;237;246";
    String key = "^_^b@_@b*_*b-_-b^_^b@_@b*_*b-_-b";
    System.out.println(aesDecrypt(convertStrArrayToByteArray(enStr), key));
}
