package example;

public class Main {

    private static void printVerify(String pass1, String pass2, Boolean test)
    {
      String baseStr = String.format("%s, %s", pass1, pass2);

      System.out.println(String.format("%s Match: %b", baseStr, test));
    }

    public static void main(String[] args) {

      Encode encode = new Encode();

      String hash = encode.hashpw("pass123");

      Boolean test1 = encode.verify("pass123", hash);

      Boolean test2 = encode.verify("123pass", hash);

      printVerify("pass123", "pass123", test1);

      printVerify("pass123", "123pass", test2);
    }
}
