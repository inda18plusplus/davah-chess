package chess.network;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

class InitHash {

  int choice;
  String seed;

  static int getRandomChoice() {
    Random random = new Random();
    return random.nextInt(2);
  }

  // From https://gits-15.sys.kth.se/inda18plusplus/chess-protocol
  static String hash(String input) throws NoSuchAlgorithmException {
    MessageDigest digest = MessageDigest.getInstance("SHA-256");
    byte[] hashedBytes = digest.digest(input.getBytes());

    StringBuilder builder = new StringBuilder();

    for (byte hashedByte : hashedBytes) {
      builder.append(String.format("%02X", hashedByte));
    }

    return builder.toString();
  }

  InitHash() {
    choice = InitHash.getRandomChoice();

    Random random = new Random();
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append((char)('0' + choice));
    for (int i = 0; i < 10; i++) {
      char temp = (char)('a' + random.nextInt('z' - 'a' + 1));
      stringBuilder.append(temp);
    }
    seed = stringBuilder.toString();
  }

  int getChoice() {
    return choice;
  }

  String getSeed() {
    return seed;
  }
}
