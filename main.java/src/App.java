import static java.lang.System.out;
import static java.lang.System.in;
import java.util.List;
import java.util.Scanner;
// import java.util.ArrayList;
import java.util.Random;

public class App {

    public static boolean checkRSVP(List<Player> players, String target, String authtype, Scanner option) {
        boolean found = false;
    
        while (!found) {
            for (Player player : players) {
                if ((authtype.equals("user") && player.getUsername().equals(target)) ||
                    (authtype.equals("pass") && player.getPassword().equals(target))) {
                    found = true;
                    break;
                }
            }
    
            if (!found) {
                if (authtype.equals("user")) {
                    out.println("Sorry, that username doesn't exist. Please exit the program and try again.");
                } else {
                    out.println("Sorry, that password doesn't match. Please exit the program and try again.");
                }

                return false;
            }
        }
        return true;
    }
    
    public static boolean login(List<Player> players, Scanner player) {
        out.println("       --Login--");
    
        // Prompt for username and check its validity
        out.print("     User: ");
        String userName = player.nextLine();
        if (!checkRSVP(players, userName, "user", player)) {
            return false;
        }
    
        // Prompt for password and check its validity
        out.print(" Password: ");
        String pass = player.nextLine();
        if (!checkRSVP(players, pass, "pass", player)) {
            return false;
        }
    
        // Check the combination of username and password
        for (Player p : players) {
            if (p.getUsername().equals(userName) && p.getPassword().equals(pass)) {
                return true; // Successful authentication
            }
        }
    
        // If the combination doesn't match, handle it here
        out.println("Authentication failed. The username and password do not match.");
        return false;
    }    

    public static void play() {
        out.println("Welcome to Guess That Number! The game where you guess a positive integer in a range you choose.");
        out.println("You will be given a number of guesses calculated from the length of your guessing range.\n");

        Scanner integerScanner = new Scanner(in);
        out.print("You will guess between 1 and (Enter a positive integer): ");
        int maxInteger = integerScanner.nextInt();
        integerScanner.nextLine(); 

        int start = 1;
        int rangeSize = maxInteger - start + 1;
        int maxGuesses = (int) Math.ceil(Math.log(rangeSize) / Math.log(2));
        out.println("Your maximum amount of guesses is " + maxGuesses + ".\n");

        Random random = new Random();
        int target = start + random.nextInt(rangeSize);

        Integer yourGuess;
        Integer count = 0;
        Integer countDown = maxGuesses;

        do {
            out.println("You have " + countDown + " guesses left.");
            out.print("Enter your guess: ");
            yourGuess = integerScanner.nextInt();
            integerScanner.nextLine(); 
            if (yourGuess < target) {
                out.println("Higher.");
            } else if (yourGuess > target) {
                out.println("Lower.");
            }
            count++;
            countDown--;
            out.println("");

        } while (yourGuess != target && countDown > 0);

        if (yourGuess == target) {
            out.println("Congratulations! You guessed the right number in " + count + " attempts.");
        } else {
            out.println("Sorry, the number was " + target + ".");
        }

        integerScanner.close();
    }

    public static void main(String[] args) throws Exception {
        out.println("Welcome to Guess That Number!");
        Scanner option = new Scanner(in);

        List<Player> players = PlayerAccountManager.loadPlayers();

        out.println("1. New Player or 2. Returning Player (1/2): ");
        int newOld = option.nextInt();
        option.nextLine(); 

        switch (newOld) {
            case 1:
                out.print("Please enter a username: ");
                String newUser = option.nextLine();

                boolean userExists = false;
                for (Player player : players) {
                    if (player.getUsername().equals(newUser)) {
                        userExists = true;
                        break;
                    }
                }

                while (userExists) {
                    out.println("Sorry, that username is already in use, please try again.");
                    out.print("Please enter a username: ");
                    newUser = option.nextLine();

                    userExists = false;
                    for (Player player : players) {
                        if (player.getUsername().equals(newUser)) {
                            userExists = true;
                            break;
                        }
                    }
                }

                out.print("Please enter a password: ");
                String newPass = option.nextLine();
                players.add(new Player(newUser, newPass));
                PlayerAccountManager.savePlayers(players);

                out.println("Please log in with your credentials.");
                if (login(players, option)) {
                    play();
                }
                break;

            case 2:
                if (login(players, option)) {
                    play();
                }
                break;

            default:
                out.println("Invalid option. Please restart the application.");
        }

        option.close();
    }
}

