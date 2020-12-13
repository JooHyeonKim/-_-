package rsa;

import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class CipherUtil {
	 /**

     * 1024��Ʈ RSA Ű���� �����մϴ�.

     */

    public static KeyPair genRSAKeyPair() throws NoSuchAlgorithmException {

        SecureRandom secureRandom = new SecureRandom();

        KeyPairGenerator gen;



        gen = KeyPairGenerator.getInstance("RSA");

        gen.initialize(1024, secureRandom);



        KeyPair keyPair = gen.genKeyPair();



        return keyPair;

    }



    /**

     * Public Key�� RSA ��ȣȭ�� �����մϴ�.

     * @param plainText ��ȣȭ�� ���Դϴ�.

     * @param publicKey ����Ű �Դϴ�.

     * @return

     */

    public static String encryptRSA(String plainText, PublicKey publicKey)

            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,

                      BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance("RSA");



        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        byte[] bytePlain = cipher.doFinal(plainText.getBytes());

        String encrypted = Base64.getEncoder().encodeToString(bytePlain);



    	return encrypted;

    }



    /**

     * Private Key�� RAS ��ȣȭ�� �����մϴ�.

     *

     * @param encrypted ��ȣȭ�� ���������͸� base64 ���ڵ��� ���ڿ� �Դϴ�.

     * @param privateKey ��ȣȭ�� ���� ����Ű �Դϴ�.

     * @return

     * @throws Exception

     */

    public static String decryptRSA(String encrypted, PrivateKey privateKey)

    		throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,


 
    		         BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {



        Cipher cipher = Cipher.getInstance("RSA");



        byte[] byteEncrypted = Base64.getDecoder().decode(encrypted.getBytes());

        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] bytePlain = cipher.doFinal(byteEncrypted);

        String decrypted = new String(bytePlain, "utf-8");



        return decrypted;

    }

}


}
