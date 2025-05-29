import java.util.ArrayList;
import java.util.Scanner;

public class Game {
    private Player player;
    private ArrayList<Integer> cards = new ArrayList<Integer>();
    private ArrayList<Integer> cardChoices = new ArrayList<Integer>();
    private ArrayList<Integer> playerCards = new ArrayList<Integer>();
    private ArrayList<Integer> dealerCards = new ArrayList<Integer>();
    private Scanner input = new Scanner(System.in);
    private double currentWager;
    private double basePrice;
    private boolean cardCounter;
    
    public Game(Player player){
        this.player = player;
        System.out.println("The game has started! " + player.getName() + ", you have $" + player.getCash());
        basePrice = player.getCash();
        cardCounter = false;
        //cardCounter = true; // <--COMMENT OUT
        scramble();
        outer:
        while(true){
            System.out.println("You have $" + player.getCash() + ", how much money will you wager?");
            while(true){
                String wager = input.nextLine();
                double actualWager = checkWager(wager);
                if(actualWager < 1){
                    System.out.println("You can't wager that amount! Try again!");
                } else {
                    currentWager = actualWager;
                    System.out.println("You wager $" + currentWager);
                    break;
                }
            }
            cardGame();
            if(player.getCash() < 1){
                break;
            }
            while(true){
                System.out.println("Would you like to continue your gambling career? You have $" + player.getCash() + ", by the way\n[1]: Yes\n[2]: No");
                String careerChoice = input.nextLine();
                int actualCareerChoice = checkCareerChoice(careerChoice);
                if(actualCareerChoice == 1){
                    System.out.println("Right! Good luck!");
                    break;
                } else if(actualCareerChoice == 2){
                    System.out.println("Ok! Hopefully you made some cash!");
                    break outer;
                } else if(actualCareerChoice == 3){
                    System.out.println("of gamblers quit before they hit big? Ok pal, pack it up. Seriously. Leave. Nobody thinks you're funny. You should just quit now. In fact, I'm going to take all of your money now. How do you like that? Now you have to retire! HAHAHAHAHAHAHAHAHA!");
                    player.setCash(0);
                    break outer;
                } else {
                    System.out.println("That's not a choice, try again!");
                }
            }
        }
        System.out.println("");
        System.out.println("Wins - Ties- Losses");
        System.out.println(player.getWins() + " - " + player.getTies() + " - " + player.getLosses());
        System.out.println("You've retired from your career of gambling with $" + player.getCash());
        printEnd();
    }
    
    public int checkCareerChoice(String choice){
        if(choice.indexOf('1') != -1){
            return 1;
        } else if(choice.indexOf('2') != -1){
            return 2;
        } else if(choice.indexOf("99%") != -1){
            return 3;
        } else {
            return -1;
        }
    }
    
    public double checkWager(String wager){
        String numbers = "";
        int once = 0;
        if(wager.length() >= 10){
            wager = wager.substring(0, 9);
        }
        for(int i = 0; i < wager.length(); i ++){
            if(wager.substring(i, i + 1).matches(".*\\d.*")){
                numbers += wager.substring(i, i + 1);
            } else if(wager.substring(i, i + 1).equals(".") && once != 1){
                numbers += ".";
                once ++;
            }
        }
        if(numbers.equals("")){
            return 0;
        }
        double returnWager = Double.valueOf(Double.parseDouble(numbers));
        if(returnWager > player.getCash()){
            return player.getCash();
        } else {
            return returnWager;
        }
    }
    
    public void cardGame(){
        System.out.println("Current stats:\nWins: " + player.getWins() + "\nLosses: " + player.getLosses() + "\nTies: " + player.getTies());
        clearBoard();
        addPlayerCard();
        addDealerCard();
        addPlayerCard();
        addDealerCard();
        printDealerCards(true);
        printPlayerCards();
        if(cardCounter){
            System.out.println("\nRemaining cards in deck:");
            printCountedCards();
        }
        int[] checkPlayerBlackjack = calculateTotal(true);
        int[] checkDealerBlackjack = calculateTotal(false);
        if(checkPlayerBlackjack[0] == 21 && checkDealerBlackjack[0] != 21){
            playerBlackjack();
            return;
        } else if(checkDealerBlackjack[0] == 21 && checkPlayerBlackjack[0] != 21){
            dealerBlackjack();
            return;
        }
        while(true){
            int[] total = calculateTotal(true);
            if(total[0] > 21 && total[1] > 21){
                System.out.println("Bust!");
                break;
            }
            System.out.println("It is your turn! You may either: \n[1]: Hit\n[2]: Stand\n[3]: Shop");
            String hitOrStand = input.nextLine();
            int actualHitOrStand = checkHitOrStand(hitOrStand);
            if(actualHitOrStand == -1){
                System.out.println("This is not a valid choice! Try again!");
            } else if(actualHitOrStand == 1){
                addPlayerCard();
                printDealerCards(true);
                printPlayerCards();
                if(cardCounter){
                    System.out.println("\nRemaining cards in deck:");
                    printCountedCards();
                }
            } else if(actualHitOrStand == 3){
                int shopping = shop();
                if(shopping > -1){
                    printDealerCards(true);
                    printPlayerCards();
                    if(cardCounter){
                        System.out.println("\nRemaining cards in deck:");
                        printCountedCards();
                    }
                } else if(shopping == -1){
                    printPlayerCards();
                    if(cardCounter){
                        System.out.println("\nRemaining cards in deck:");
                        printCountedCards();
                    }
                }
            } else {
                break;
            }
        }
        while(true){
            System.out.println("It is now the dealer's turn!");
            printDealerCards(true);
            printPlayerCards();
            int[] playerTotal = calculateTotal(true);
            if(playerTotal[0] > 21 && playerTotal[1] > 21){
                System.out.println("The dealer stands!");
                printDealerCards(false);
                printPlayerCards();
                dealerWin();
                break;
            } else {
                int[] dealerTotal = calculateTotal(false);
                if(dealerTotal[0] > 21 && dealerTotal[1] > 21){
                    System.out.println("The dealer busts!");
                    printDealerCards(false);
                    printPlayerCards();
                    playerWin();
                    break;
                }
                System.out.print("The AI is thinking.");
                try {
                    Thread.sleep(750);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
                System.out.print(".");
                try {
                    Thread.sleep(750);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
                System.out.print(".");
                try {
                    Thread.sleep(750);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                }
                System.out.println(".");
                if(dealerTotal[0] > 16 && dealerTotal[0] < 22 || (dealerTotal[0] > 21 && dealerTotal[1] > 16 && dealerTotal[1] < 22)){
                    System.out.println("The dealer stands!");
                    printDealerCards(false);
                    printPlayerCards();
                    compareScores();
                    break;
                } else {
                    addDealerCard();
                    System.out.println("The dealer hits!");
                }
            }
        }
    }
    
    public int shop(){
        System.out.println();
        System.out.println("Purchase options:\n[1]: Exit\n[2]: Reveal dealer's cards - $" + (basePrice * .25) +"\n[3]: Purchase card counter - $" + (basePrice * 1.5) + "\nAvailable funds: $" + (player.getCash() - currentWager));
        while(true){
            String purchaseChoice = input.nextLine();
            int actualChoice = checkPurchaseChoice(purchaseChoice);
            if(actualChoice == 1){
                System.out.println();
                return 0;
            } else if(actualChoice == 2){
                System.out.println();
                if(player.getCash() < (basePrice * 0.25)){
                    System.out.println("You can't purchase that!");
                } else {
                    System.out.println("You purchased access to the deaer's cards!\nDealer's Cards:");
                    printDealerCards(false);
                    player.setCash(player.getCash() - (basePrice * 0.25 + currentWager));
                    return -1;
                }
            } else if(actualChoice == 3){
                System.out.println();
                if(player.getCash() < (basePrice * 1.5 + currentWager) || cardCounter == true){
                    System.out.println("You can't purchase that!");
                } else {
                    System.out.println("You purchased a card counter!");
                    cardCounter = true;
                    player.setCash(player.getCash() - (basePrice * 1.5 ));
                    return 0;
                }
            } else {
                System.out.println("That's not a valid choice! Try again!");
            }
        }
    }
    
    public int checkPurchaseChoice(String choice){
        if(choice.indexOf('1') != -1){
            return 1;
        } else if(choice.indexOf('2') != -1){
            return 2;
        } else if(choice.indexOf('3') != -1){
            return 3;
        } else {
            return -1;
        }
    }
    
    public void compareScores(){
        int[] playerTotal = calculateTotal(true);
        int[] dealerTotal = calculateTotal(false);
        int greatestPlayer;
        int greatestDealer;
        if(playerTotal[0] < 21 && playerTotal[0] > playerTotal[1]){
            greatestPlayer = playerTotal[0];
        } else {
            greatestPlayer = playerTotal[1];
        }
        if(dealerTotal[0] < 21 && dealerTotal[0] > dealerTotal[1]){
            greatestDealer = dealerTotal[0];
        } else {
            greatestDealer = dealerTotal[1];
        }
        if(greatestPlayer == greatestDealer){
            tie();
        } else if(greatestPlayer > greatestDealer){
            playerWin();
        } else {
            dealerWin();
        }
    }
    
    public void dealerWin(){
        System.out.println("The dealer has won!");
        player.setCash(player.getCash() - currentWager);
        player.addLoss();
    }
    
    public void playerWin(){
        System.out.println("You have won!");
        player.setCash(player.getCash() + currentWager);
        player.addWin();
    }
    
    public void tie(){
        System.out.println("The game is a tie!");
        player.addTie();
    }
    
    public void playerBlackjack(){
        System.out.println("Blackjack!");
        player.setCash(player.getCash() + (1.5 * currentWager));
        player.addWin();
    }
    
    public void dealerBlackjack(){
        System.out.println("The dealer has gotten blackjack!");
        player.setCash(player.getCash() - (currentWager));
        player.addLoss();
    }
    
    public void clearBoard(){
        playerCards.clear();
        dealerCards.clear();
    }
    
    public int[] calculateTotal(boolean player){
        int[] total = new int[2];
        if(player){
            for(int i = 0; i < playerCards.size(); i ++){
                if(playerCards.get(i) == 1){
                   total[0] += 11;
                   total[1] += 1;
                } else if(playerCards.get(i) > 10){
                    total[0] += 10;
                    total[1] += 10;
                } else {
                   total[0] += playerCards.get(i);
                    total[1] += playerCards.get(i);
                }
            }
        } else {
            for(int i = 0; i < dealerCards.size(); i ++){
                if(dealerCards.get(i) == 1){
                    total[0] += 11;
                    total[1] += 1;
                } else if(dealerCards.get(i) > 10){
                    total[0] += 10;
                    total[1] += 10;
                } else {
                    total[0] += dealerCards.get(i);
                    total[1] += dealerCards.get(i);
                }
            }
        }
        return total;
    }
    
    public int checkHitOrStand(String choice){
        if(choice.indexOf('1') != -1){
            return 1;
        } else if(choice.indexOf('2') != -1){
            return 2;
        } else if(choice.indexOf('3') != -1){
            return 3;
        } else {
            return -1;
        }
    }
    
    public void addPlayerCard(){
        if(cards.size() == 0){
            scramble();
        }
        playerCards.add(this.cards.get(0));
        this.cards.remove(0);
    }
    
    public void addDealerCard(){
        if(cards.size() == 0){
            scramble();
        }
        dealerCards.add(this.cards.get(0));
        this.cards.remove(0);
    }
    
    public void scramble(){
        resetCardChoices();
        cards = new ArrayList<Integer>();
        for(int i=0; i < 52; i ++){
            int randomIndex = (int)(Math.random() * cardChoices.size());
            cards.add(cardChoices.get(randomIndex));
            cardChoices.remove(randomIndex);
        }
    }
    
    public void resetCardChoices(){
        cardChoices.clear();
        for(int i = 1; i < 14; i ++){
            for(int j = 0; j < 4; j ++){
                cardChoices.add(i);
            }
        }
    }
    
    public void printPlayerCards(){
        System.out.println();
        System.out.println("Your Cards:");
        for(int i = 0; i < playerCards.size(); i++){
            System.out.print("--------------------\t");
        }
        System.out.println();
        for(int i = 0; i < playerCards.size(); i++){
            if(playerCards.get(i) == 1){
                System.out.print("|A");
            } else if(playerCards.get(i) == 11){
                System.out.print("|J");
            } else if(playerCards.get(i) == 12){
                System.out.print("|Q");
            } else if(playerCards.get(i) == 13){
                System.out.print("|K");
            } else {
                System.out.print("|" + playerCards.get(i));
            }
            System.out.print("\t\t   |\t");
        }
        System.out.println();
        for(int i = 0; i < 3; i ++){
            for(int k = 0; k < playerCards.size(); k++){
                System.out.print("|\t\t   |\t");
            }
            System.out.println();
        }
        for(int i = 0; i < playerCards.size(); i++){
            if(playerCards.get(i) == 1){
                System.out.print("|\t  A");
            } else if(playerCards.get(i) == 11){
                System.out.print("|\t  J");
            } else if(playerCards.get(i) == 12){
                System.out.print("|\t  Q");
            } else if(playerCards.get(i) == 13){
                System.out.print("|\t  K");
            } else if(playerCards.get(i) == 10){
                System.out.print("|\t 10");
            } else {
                System.out.print("|\t  " + playerCards.get(i));
            }
            System.out.print(" \t   |\t");
        }
        System.out.println();
        for(int i = 0; i < 3; i ++){
            for(int k = 0; k < playerCards.size(); k++){
                System.out.print("|\t\t   |\t");
            }
            System.out.println();
        }
        for(int i = 0; i < playerCards.size(); i++){
            if(playerCards.get(i) == 1){
                System.out.print("|\t\t  A");
            } else if(playerCards.get(i) == 11){
                System.out.print("|\t\t  J");
            } else if(playerCards.get(i) == 12){
                System.out.print("|\t\t  Q");
            } else if(playerCards.get(i) == 13){
                System.out.print("|\t\t  K");
            } else if(playerCards.get(i) == 10){
                System.out.print("|\t\t 10");
            } else {
                System.out.print("|\t\t  " + playerCards.get(i));
            }
            System.out.print("|\t");
        }
        System.out.println();
        for(int i = 0; i < playerCards.size(); i++){
            System.out.print("--------------------\t");
        }
        System.out.println();
    }
    
    public void printDealerCards(boolean hidden){
        System.out.println();
        System.out.println("Dealer's Cards:");
        for(int i = 0; i < dealerCards.size(); i++){
            System.out.print("--------------------\t");
        }
        System.out.println();
        for(int i = 0; i < dealerCards.size(); i++){
            if(i == 1 && hidden){
                System.out.print("|?");
            } else if(dealerCards.get(i) == 1){
                System.out.print("|A");
            } else if(dealerCards.get(i) == 11){
                System.out.print("|J");
            } else if(dealerCards.get(i) == 12){
                System.out.print("|Q");
            } else if(dealerCards.get(i) == 13){
                System.out.print("|K");
            } else {
                System.out.print("|" + dealerCards.get(i));
            }
            System.out.print("\t\t   |\t");
        }
        System.out.println();
        for(int i = 0; i < 3; i ++){
            for(int k = 0; k < dealerCards.size(); k++){
                System.out.print("|\t\t   |\t");
            }
            System.out.println();
        }
        for(int i = 0; i < dealerCards.size(); i++){
            if(i == 1){
                System.out.print("|\t  ?");
            } else if(dealerCards.get(i) == 1){
                System.out.print("|\t  A");
            } else if(dealerCards.get(i) == 11){
                System.out.print("|\t  J");
            } else if(dealerCards.get(i) == 12){
                System.out.print("|\t  Q");
            } else if(dealerCards.get(i) == 13){
                System.out.print("|\t  K");
            } else if(dealerCards.get(i) == 10){
                System.out.print("\t  10");
            } else {
                System.out.print("|\t  " + dealerCards.get(i));
            }
            System.out.print(" \t   |\t");
        }
        System.out.println();
        for(int i = 0; i < 3; i ++){
            for(int k = 0; k < dealerCards.size(); k++){
                System.out.print("|\t\t   |\t");
            }
            System.out.println();
        }
        for(int i = 0; i < dealerCards.size(); i++){
            if(i == 1){
                System.out.print("|\t\t  ?");
            } else if(dealerCards.get(i) == 1){
                System.out.print("|\t\t  A");
            } else if(dealerCards.get(i) == 11){
                System.out.print("|\t\t  J");
            } else if(dealerCards.get(i) == 12){
                System.out.print("|\t\t  Q");
            } else if(dealerCards.get(i) == 13){
                System.out.print("|\t\t  K");
            } else if(dealerCards.get(i) == 10){
                System.out.print("|\t\t 10");
            } else {
                System.out.print("|\t\t  " + dealerCards.get(i));
            }
            System.out.print("|\t");
        }
        System.out.println();
        for(int i = 0; i < dealerCards.size(); i++){
            System.out.print("--------------------\t");
        }
        System.out.println();
    }
    
    public void printCards(){
        for(int i=0; i < cards.size(); i++){
            if(cards.get(i) == 1){
                System.out.print("A, ");
            } else if(cards.get(i) == 11){
                System.out.print("J, ");
            } else if(cards.get(i) == 12){
                System.out.print("Q, ");
            } else if(cards.get(i) == 13){
                System.out.print("K, ");
            } else {
                System.out.print(cards.get(i) + ", ");
            }
        }
        System.out.println();
    }
    
    public void printCountedCards(){
        int hiLo = 0;
        int rCount = 0;
        int currentCard = 1;
        System.out.println(cards.size());
        for(int i = 1; i < 14; i ++){
            rCount = 0;
            for(int r = 0; r < cards.size(); r ++){
                if(cards.get(r) == i){
                    rCount ++;
                    if(cards.get(r) == 1){
                        System.out.print("A ");
                    } else if(cards.get(r) == 11){
                        System.out.print("J ");
                    } else if(cards.get(r) == 12){
                        System.out.print("Q ");
                    } else if(cards.get(r) == 13){
                        System.out.print("K ");
                    } else {
                        System.out.print(cards.get(r) + " ");
                    }
                }
            }
            for(int z = 4; z > rCount; z --){
                if(i == 1 || i > 9){
                    hiLo --;
                } else if(i < 7 && i > 1){
                    hiLo ++;
                }
            }
        }
        System.out.println("\nHi-Lo Score: " + hiLo);
        System.out.println("Hint: Higher scores correlate with more 10's, J's, Q's, K's, A's");
        System.out.println("");
    }
    
    public void printEnd(){
        System.out.println("");
        System.out.println("  _______ _                 _           __                   _             _             _ ");
        System.out.println(" |__   __| |               | |         / _|                 | |           (_)           | |");
        System.out.println("    | |  | |__   __ _ _ __ | | _____  | |_ ___  _ __   _ __ | | __ _ _   _ _ _ __   __ _| |");
        System.out.println("    | |  | '_ \\ / _` | '_ \\| |/ / __| |  _/ _ \\| '__| | '_ \\| |/ _` | | | | | '_ \\ / _` | |");
        System.out.println("    | |  | | | | (_| | | | |   <\\__ \\ | || (_) | |    | |_) | | (_| | |_| | | | | | (_| |_|");
        System.out.println("    |_|  |_| |_|\\__,_|_| |_|_|\\_\\___/ |_| \\___/|_|    | .__/|_|\\__,_|\\__, |_|_| |_|\\__, (_)");
        System.out.println("                                                      | |             __/ |         __/ |  ");
        System.out.println("                                                      |_|            |___/         |___/   ");
    }
}
