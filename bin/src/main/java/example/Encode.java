package example;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import java.io.IOException;
import javax.xml.bind.DatatypeConverter;

/*
 * This is the Main class.
 */
public class Encode {

  private final Argon2 argon2 = Argon2Factory.create();

  final Encryption encrypt = new Encryption();

  public String hashpw(String plainText) {
      char[] passwordChars = plainText.toCharArray();

      String hash = argon2.hash(22, 65536, 1, passwordChars);

      argon2.wipeArray(passwordChars);

      try {

        return encrypt.encryptPasswordBased(hash);

      } catch (Exception e) {
        return null;
      }
  }

  public boolean verify(String plainText, String hashedStr) {
    try{

      hashedStr = encrypt.decryptPasswordBased(hashedStr);

      return argon2.verify(hashedStr, plainText.toCharArray());

    } catch (Exception e) {

      System.out.println("Encode verify error");

      e.printStackTrace();

      return false;
    }
  }
}
