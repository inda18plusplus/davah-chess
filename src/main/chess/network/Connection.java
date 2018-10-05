package chess.network;

import chess.Game;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import org.json.JSONObject;

public class Connection {

  private Socket socket;
  private DataInputStream inputStream;
  private DataOutputStream outputStream;

  /**
   * Listens to a connection at a specified port and creates the connection.
   *
   * @param port The port to listen to.
   */
  public Connection(int port) {
    try {
      ServerSocket serverSocket = new ServerSocket(port);
      socket = serverSocket.accept();
      inputStream = new DataInputStream(socket.getInputStream());
      outputStream = new DataOutputStream(socket.getOutputStream());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Connects to a specified host and port.
   *
   * @param host The address to connect to (?)
   * @param port The port to connect to.
   */
  public Connection(String host, int port) {
    try {
      socket = new Socket(host, port);
      inputStream = new DataInputStream(socket.getInputStream());
      outputStream = new DataOutputStream(socket.getOutputStream());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void write(String string) {
    try {
      outputStream.writeUTF(string);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String read() {
    try {
      return inputStream.readUTF();
    } catch (Exception e) {
      e.printStackTrace();
      return "Somtign vent rong, en I kant handl eccepcions.";
    }
  }

  /**
   * Decides who starts when playing as host.
   *
   * @return Which player (black or white) the user is.
   */
  public Game.Player initAsHost() {
    InitHash initHash = new InitHash();
    final String seed = initHash.getSeed();
    final int choice = initHash.getChoice();
    JSONObject jsonObject = new JSONObject();

    jsonObject.put("type", "init");
    try {
      jsonObject.put("hash", InitHash.hash(seed));
    } catch (Exception e) {
      e.printStackTrace();
    }
    write(jsonObject.toString());

    jsonObject = new JSONObject(this.read());
    final int receivedChoice = jsonObject.getInt("choice");
    if (receivedChoice != 0 && receivedChoice != 1) {
      this.write("Opponent cheated! Disconnecting.");
      this.disconnect();
      return null;
    }

    jsonObject = new JSONObject();
    jsonObject.put("type", "init");
    jsonObject.put("seed", seed);
    jsonObject.put("choice", choice);
    write(jsonObject.toString());

    return choice == receivedChoice ? Game.Player.WHITE : Game.Player.BLACK;
  }

  /**
   * Decides who starts when not playing as host.
   *
   * @return Which player (black or white) the user is.
   */
  public Game.Player initAsJoin() {
    JSONObject jsonObject = new JSONObject(this.read());
    final String receivedHash = jsonObject.getString("hash");

    final int choice = InitHash.getRandomChoice();
    jsonObject = new JSONObject();
    jsonObject.put("choice", choice);
    write(jsonObject.toString());

    jsonObject = new JSONObject(this.read());
    final String receivedSeed = jsonObject.getString("seed");
    final int receivedChoice = jsonObject.getInt("choice");

    try {
      boolean sameHash = InitHash.hash(receivedSeed).equals(receivedHash);
      boolean correctChoiceInSeed = (receivedChoice == receivedSeed.charAt(0) - '0');
      boolean choiceInRange = (receivedChoice == 0 || receivedChoice == 1);
      if (!sameHash || !correctChoiceInSeed || !choiceInRange) {
        this.write("Opponent cheated! Disconnecting.");
        this.disconnect();
        return null;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return choice == receivedChoice ? Game.Player.BLACK : Game.Player.WHITE;
  }

  /**
   * Sends a move to the other player through the mysterious net.
   *
   * @param move The move. Format: [a-h][1-8][a-z][1-8]
   * @param promoteTo Empty string if not promoting, otherwise an uppercase letter.
   */
  public void sendMove(String move, String promoteTo) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("type", "move");
    jsonObject.put("from", move.substring(0, 2));
    jsonObject.put("to", move.substring(2, 4));
    jsonObject.put("promotion", promoteTo);
    this.write(jsonObject.toString());
  }

  /**
   * Reads and parses a move.
   *
   * @return The move in chess protocol format.
   */
  public String readMove() {
    JSONObject jsonObject = new JSONObject(this.read());
    return jsonObject.getString("from")
            + jsonObject.getString("to")
            + jsonObject.getString("promotion");
  }

  /**
   * Sends a response to a move.
   *
   * @param success Whether the received move was valid.
   */
  public void sendResponse(boolean success) {
    JSONObject jsonObject = new JSONObject();
    jsonObject.put("type", "response");
    jsonObject.put("response", success ? "ok" : "invalid");
    this.write(jsonObject.toString());
  }

  /**
   * Reads a response by the opponent.
   *
   * @return True if our move was accepted, false otherwise.
   */
  public boolean readResponseOk() {
    JSONObject jsonObject = new JSONObject(this.read());
    return jsonObject.getString("response").equals("ok");
  }

  /**
   * Disconnects.
   */
  public void disconnect() {
    try {
      socket.close();
      inputStream = null;
      outputStream = null;
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
