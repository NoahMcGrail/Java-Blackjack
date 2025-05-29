import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {
        Scanner input = new Scanner(System.in);
        
        printStart();
        System.out.println("Welcome! Please enter your name:");
        String name = input.nextLine();
        System.out.println();
        System.out.println("How much money would you like to start with?");
        if(isDev(name) == false){
            System.out.println("[1]: $500 \n[2]: $1000 \n[3]: $2000 \n[4]: $5000");
            int checkedFirstChoice;
            while(true){
                String startingCashChoice = input.nextLine();
                checkedFirstChoice = checkFirstChoice(startingCashChoice);
                if(checkedFirstChoice != -1){
                    break;
                } else {
                    System.out.println("Not a valid option! Try again!");
                }
                System.out.println();
            }
            if(checkedFirstChoice == 1){
                    Player player = new Player(name, 500.0);
                    Game newGame = new Game(player);
                } else if(checkedFirstChoice == 2){
                    Player player = new Player(name, 1000.0);
                    Game newGame = new Game(player);
                } else if(checkedFirstChoice == 3){
                    Player player = new Player(name, 2000.0);
                    Game newGame = new Game(player);
                } else if(checkedFirstChoice == 4){
                    Player player = new Player(name, 5000.0);
                    Game newGame = new Game(player);
                } else if(checkedFirstChoice == 0){
                    Player player = new Player("Smartass", 0.01);
                    Game newGame = new Game(player);
                }
        } else {
            String customCash = input.nextLine();
            double startingCash = startingCash(customCash);
            Player player = new Player(name, startingCash);
            Game newGame = new Game(player);
        }
        
        
    }
    
    public static void printStart(){
        System.out.println("         .______    __          ___       ______  __  ___           __       ___       ______  __  ___         ");
        System.out.println("         |   _  \\  |  |        /   \\     /      ||  |/  /          |  |     /   \\     /      ||  |/  /         ");
        System.out.println("         |  |_)  | |  |       /  ^  \\   |  ,----'|  '  /           |  |    /  ^  \\   |  ,----'|  '  /          ");
        System.out.println("         |   _  <  |  |      /  /_\\  \\  |  |     |    <      .--.  |  |   /  /_\\  \\  |  |     |    <           ");
        System.out.println("         |  |_)  | |  `----./  _____  \\ |  `----.|  .  \\     |  `--'  |  /  _____  \\ |  `----.|  .  \\          ");
        System.out.println("         |______/  |_______/__/     \\__\\ \\______||__|\\__\\     \\______/  /__/     \\__\\ \\______||__|\\__\\         ");
        System.out.println("");
        System.out.println("       __       ___   ____    ____  ___          _______  _______   __  .___________. __    ______   .__   __. ");
        System.out.println("      |  |     /   \\  \\   \\  /   / /   \\        |   ____||       \\ |  | |           ||  |  /  __  \\  |  \\ |  | ");
        System.out.println("      |  |    /  ^  \\  \\   \\/   / /  ^  \\       |  |__   |  .--.  ||  | `---|  |----`|  | |  |  |  | |   \\|  | ");
        System.out.println(".--.  |  |   /  /_\\  \\  \\      / /  /_\\  \\      |   __|  |  |  |  ||  |     |  |     |  | |  |  |  | |  . `  | ");
        System.out.println("|  `--'  |  /  _____  \\  \\    / /  _____  \\     |  |____ |  '--'  ||  |     |  |     |  | |  `--'  | |  |\\   | ");
        System.out.println(" \\______/  /__/     \\__\\  \\__/ /__/     \\__\\    |_______||_______/ |__|     |__|     |__|  \\______/  |__| \\__| ");
        System.out.println("______________________________________________________________________________________________________________");
        System.out.println();
    }
    
    public static boolean isDev(String name){
        return name.indexOf("Dealer") != -1;
    }
    
    public static int checkFirstChoice(String choice){
        if(choice.indexOf('1') != -1){
            return 1;
        } else if(choice.indexOf('2') != -1){
            return 2;
        } else if(choice.indexOf('3') != -1){
            return 3;
        } else if(choice.indexOf('4') != -1){
            return 4;
        } else if(choice.indexOf("again") != -1){
            return 0;
        } else {
            return -1;
        }
    }
    
    public static double startingCash(String choice){
        if(choice.length() >= 10){
            choice = choice.substring(0, 9);
        }
        String justNumbers = "";
        boolean lastNum = false;
        for(int i = 0; i < choice.length(); i ++){
            if(choice.substring(i, i + 1).matches(".*\\d.*")){
                justNumbers += choice.substring(i, i + 1);
                lastNum = true;
            } else {
                if(lastNum){
                    break;
                }
            }
        }
        if(justNumbers.equals("")){
            return 1;
        }
        return Integer.valueOf(Integer.parseInt(justNumbers));
    }
}
