package aeonlabs.common.libraries.Encryption;

import android.util.Base64;

import androidx.annotation.Nullable;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AesCipher
 * <p>Encode/Decode text by password using AES-128-CBC algorithm</p>
 */

public class AESCipher {
    public static final int INIT_VECTOR_LENGTH = 16;
    /**
     * @see <a href="https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java">how-to-convert-a-byte-array-to-a-hex-string</a>
     */

    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
    /**
     * Encoded/Decoded data
     */

    protected String data;
    /**
     * Initialization vector value
     */

    protected String initVector;
    /**
     * Error message if operation failed
     */

    protected String errorMessage;

    private AESCipher() {
        super();
    }

    /**
     * AesCipher constructor.
     *
     * @param initVector   Initialization vector value
     * @param data         Encoded/Decoded data
     * @param errorMessage Error message if operation failed
     */

    private AESCipher(@Nullable String initVector, @Nullable String data, @Nullable String errorMessage) {
        super();
        this.initVector = initVector;
        this.data = data;
        this.errorMessage = errorMessage;
    }

    /**
     * Encrypt input text by AES-128-CBC algorithm
     *
     * @param secretKey 16/24/32 -characters secret password
     * @param plainText Text for encryption
     * @return Encoded string or NULL if error
     */

    public static AESCipher encrypt(String secretKey, String plainText) {
        String initVector = null;
        try {
            // Check secret length
            if (!isKeyLengthValid(secretKey)) {
                throw new Exception("Secret key's length must be 128, 192 or 256 bits");
            }
            // Get random initialization vector
            SecureRandom secureRandom = new SecureRandom();
            byte[] initVectorBytes = new byte[INIT_VECTOR_LENGTH / 2];
            secureRandom.nextBytes(initVectorBytes);
            initVector = bytesToHex(initVectorBytes);
            initVectorBytes = initVector.getBytes(StandardCharsets.UTF_8);
            IvParameterSpec ivParameterSpec = new IvParameterSpec(initVectorBytes);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
            // Encrypt input text
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            ByteBuffer byteBuffer = ByteBuffer.allocate(initVectorBytes.length + encrypted.length);
            byteBuffer.put(initVectorBytes);
            byteBuffer.put(encrypted);
            // Result is base64-encoded string: initVector + encrypted result
            String result = (Base64.encodeToString(byteBuffer.array(), Base64.DEFAULT));
            // Return successful encoded object
            return new AESCipher(initVector, result, null);
        } catch (Exception e){
            return new AESCipher(initVector, null, e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
            // Operation failed
            return new AESCipher(initVector, null, t.getMessage());
        }
    }

    /**
     * Decrypt encoded text by AES-128-CBC algorithm
     *
     * @param secretKey  16/24/32 -characters secret password
     * @param cipherText Encrypted text
     * @return Self object instance with data or error message
     */

    public static AESCipher decrypt(String secretKey, String cipherText) {
        try {
            // Check secret length
            if (!isKeyLengthValid(secretKey)) {
                throw new Exception("Secret key's length must be 128, 192 or 256 bits");
            }

            // Get raw encoded data
            byte[] encrypted = Base64.decode(cipherText, Base64.DEFAULT);
            // Slice initialization vector
            IvParameterSpec ivParameterSpec = new IvParameterSpec(encrypted, 0, INIT_VECTOR_LENGTH);
            // Set secret password
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
            // Trying to get decrypted text
            String result = new String(cipher.doFinal(encrypted, INIT_VECTOR_LENGTH, encrypted.length - INIT_VECTOR_LENGTH));
            // Return successful decoded object
            return new AESCipher(hexToString(bytesToHex(ivParameterSpec.getIV())), result, null);
        } catch (Exception e){
            return new AESCipher(null, null, e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
            // Operation failed
            return new AESCipher(null, null, t.getMessage());
        }
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

    private static String padString(String source) {
        char paddingChar = 0;
        int size = 16;
        int x = source.length() % size;
        int padLength = size - x;

        for (int i = 0; i < padLength; i++)
        {
            source += paddingChar;
        }
        return source;
    }


    /**
     * Check that secret password length is valid
     *
     * @param key 16/24/32 -characters secret password
     * @return TRUE if valid, FALSE otherwise
     */
    public static boolean isKeyLengthValid(String key) {
        return key.length() == 16 || key.length() == 24 || key.length() == 32;
    }

    /**
     * Convert Bytes to HEX
     *
     * @param bytes Bytes array
     * @return String with bytes values
     */

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * Get encoded/decoded data
     */
    public String getData() {
        return data;
    }

    /**
     * Get initialization vector value
     */

    public String getInitVector() {
        return initVector;
    }

    /**
     * Get error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Check that operation failed
     *
     * @return TRUE if failed, FALSE otherwise
     */

    public boolean hasError() {
        return this.errorMessage != null;
    }

    /**
     * To string return resulting data
     *
     * @return Encoded/decoded data
     */

    public String toString() {
        return getData();
    }
}
