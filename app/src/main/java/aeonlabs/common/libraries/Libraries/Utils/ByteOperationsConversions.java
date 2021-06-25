package aeonlabs.common.libraries.Libraries.Utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ByteOperationsConversions {
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();


/***********************************  CONVERSION FROM AND TO byte <> Byte *****************************/
    public static Byte[] frombyte2Byte(byte[] bytes){
        Byte[] B = new Byte[bytes.length];
        for (int i = 0; i < bytes.length; i++)
        {
            B[i] = Byte.valueOf(bytes[i]);
        }
        return B;
    }

    public static byte[] fromByte2byte(Byte[] bytes){
        byte[] b2 = new byte[bytes.length];
        for (int i = 0; i < bytes.length; i++) {
            b2[i] = bytes[i];
        }
        return b2;
    }
    /*****************************************************************/
    public static byte[] convertIntegerTo2byteArray(int number){
        ArrayList<Byte> data= new ArrayList<>();

        data.add( (byte) (number & 0xFF));
        data.add( (byte) ((number >> 8) & 0xFF));

        Byte[] dataArr = new Byte[data.size()];
        dataArr = data.toArray(dataArr);
        return fromByte2byte(dataArr);
    }
    /************************ add 2 byte arrays *************************************/
    public static byte[] concatenateByteArrays(byte[] a, byte[] b) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );
        outputStream.write( a );
        outputStream.write( b );

        return outputStream.toByteArray( );
    }

    //*********************** conversion methods    ************************************************//
    /** length should be less than 4 (for int) **/
    public static int byteToInt(byte[] bytes, int length) {
        int val = 0;
        if(length>4) throw new RuntimeException("Too big to fit in int");
        for (int i = 0; i < length; i++) {
            val=val<<8;
            val=val|(bytes[i] & 0xFF);
        }
        return val;
    }

    public static String bytesToHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    // one byte integer to hex string
    public static String int2hexString(int byteNumberString){
        byte[] byteVal= new byte[1];
        //TODO
        byteVal[0] = (byte) byteNumberString;
        return ByteOperationsConversions.bytesToHexString(byteVal);
    }

    public static String hexToString(String hex) {
        StringBuilder sb = new StringBuilder();
        char[] hexData = hex.toCharArray();
        for (int count = 0; count < hexData.length - 1; count += 2) {
            int firstDigit = Character.digit(hexData[count], 16);
            int lastDigit = Character.digit(hexData[count + 1], 16);
            int decimal = firstDigit * 16 + lastDigit;
            sb.append((char)decimal);
        }
        return sb.toString();
    }

    /* s must be an even-length string. */
    public static byte[] hexStringToByteArray(String hexString) {
        if(hexString.length() % 2 != 0)
            hexString+=" ";

        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i+1), 16));
        }
        return data;
    }

    public static String string2HexString(String bin) {
        StringBuffer sb = new StringBuffer();
        byte[] bs = bin.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(hexArray[bit]);
            bit = bs[i] & 0x0f;
            sb.append(hexArray[bit]);
        }
        return sb.toString();
    }

    public static byte[] removeBytes(byte[] arr, int startOffSet, int endOffSet) {
        byte[] result;
        int pos=0;

        result = new byte[arr.length-(startOffSet+endOffSet)];

        for (int i = startOffSet; i < (arr.length-endOffSet); i++) {
            result[pos]=arr[i];
            pos++;
        }
        return result;
    }

    public static byte[] hexIntToByteArray(String hex) {
        hex = hex.length()%2 != 0?"0"+hex:hex;

        byte[] b = new byte[hex.length() / 2];

        for (int i = 0; i < b.length; i++) {
            int index = i * 2;
            int v = Integer.parseInt(hex.substring(index, index + 2), 16);
            b[i] = (byte) v;
        }
        return b;
    }

}
