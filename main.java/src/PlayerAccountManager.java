import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerAccountManager {
    private static final String FILE_NAME = "players.dat";

    public static void savePlayers(List<Player> players) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(players);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Player> loadPlayers() {
        List<Player> players = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            players = (List<Player>) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("Player file not found. Starting with an empty list.");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return players;
    }
}
