package example;

import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.BadPaddingException;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKeyFactory;
import javax.crypto.SealedObject;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

class Encryption {

    private final String password = "abcd1234";
    final int key_size = 256;
    IvParameterSpec ivParameterSpec = generateIv();

    String salt = null;

    SecretKey key = null;


    public Encryption() {
      try {
        salt = generateSalt();
        key = getKeyFromPassword();
      } catch(Exception e) {

      }
    }

    private SecretKey generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(key_size);
        return keyGenerator.generateKey();
    }

    private String generateSalt() throws Exception {
       SecretKey key = generateKey();
       return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    private SecretKey getKeyFromPassword() throws Exception
    {
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
        return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
    }

    private IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    private Cipher setupCipher(int optMode) throws Exception {
      Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      cipher.init(optMode, key, ivParameterSpec);
      return cipher;
    }

    public String encryptPasswordBased(String plainText) throws Exception
    {
        Cipher cipher = setupCipher(Cipher.ENCRYPT_MODE);
        return Base64.getEncoder().encodeToString(cipher.doFinal(plainText.getBytes()));
    }

    public String decryptPasswordBased(String cipherText) throws Exception{
      Cipher cipher = setupCipher(Cipher.DECRYPT_MODE);
      return new String(cipher.doFinal(Base64.getDecoder().decode(cipherText)));
    }

}
