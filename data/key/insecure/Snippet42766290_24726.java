byte[] value = characteristic.getValue();
byte[] tmpValue = Arrays.copyOfRange(value, 3, 19);
Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");

// here we use key like in our firt requst
SecretKeySpec key = new SecretKeySpec(new byte[] {0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37, 0x38, 0x39, 0x40, 0x41, 0x42, 0x43, 0x44, 0x45}, "AES");

cipher.init(Cipher.ENCRYPT_MODE, key);
byte[] bytes = cipher.doFinal(tmpValue);

byte[] rq = ArrayUtils.addAll(new byte[]{0x03, 0x8}, bytes);
characteristic.setValue(rq);
gatt.writeCharacteristic(characteristic);
