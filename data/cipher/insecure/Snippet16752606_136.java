jbyteArray Java_MainActivity_decrypt(JNIEnv* env, jobject context,jbyteArray key, jbyteArray iv, jbyteArray enc) {


   //Cipher c3des = Cipher.getInstance("DESede/CBC/PKCS5Padding");
   jclass cl = (*env)->FindClass(env,"javax/crypto/Cipher");
   jmethodID MID = (*env)->GetStaticMethodID(env,cl, "getInstance", "(Ljava/lang/String;)Ljavax/crypto/Cipher;");
   jstring s = (*env)->NewStringUTF(env,"DESede/CBC/PKCS5Padding");
   jobject c3des = (*env)->CallStaticObjectMethod(env,cl, MID, s);

   //SecretKeySpec    myKey = new SecretKeySpec(key, "DESede");
   jclass cl1 = (*env)->FindClass(env, "javax/crypto/spec/SecretKeySpec");
   jclass constructor1 = (*env)->GetMethodID(env, cl1, "<init>", "([BLjava/lang/String;)V");
   jstring s1 = (*env)->NewStringUTF(env,"DESede");
   jobject myKey = (*env)->NewObject(env, cl1, constructor1, key, s1);

   //IvParameterSpec ivspec = new IvParameterSpec(initializationVector);
   jclass cl2 = (*env)->FindClass(env, "javax/crypto/spec/IvParameterSpec");
   jclass constructor2 = (*env)->GetMethodID(env, cl2, "<init>", "([B)V");
   jobject ivspec = (*env)->NewObject(env, cl2, constructor2, iv);

   //c3des.init(Cipher.DECRYPT_MODE, myKey, ivspec);
   jmethodID mid_int = (*env)->GetMethodID(env, cl, "init","(ILjava/security/Key;Ljava/security/AlgorithmParameters;)V");
   jfieldID field_dec_id = (*env)->GetStaticFieldID(env, cl, "DECRYPT_MODE","I");
   jint field_dec = (*env)->GetStaticIntField(env, cl, field_dec_id);
   (*env)->CallVoidMethod(env,c3des,mid_int,field_dec,myKey,ivspec); //<--app crash at this line

   return;
}
