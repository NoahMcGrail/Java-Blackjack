public class Player {
    private String name;
    private double cash;
    private int wins;
    private int losses;
    private int ties;
    
    public Player(String name, double startingCash){
        this.name = name;
        cash = startingCash;
    }
    
    public double getCash(){
        return cash;
    }
    
    public void setCash(double amount){
        cash = amount;
    }
    
    public String getName(){
        return name;
    }
    
    public void addWin(){
        wins ++;
    }
    
    public int getWins(){
        return wins;
    }
    
    public void addLoss(){
        losses ++;
    }
    
    public int getLosses(){
        return losses;
    }
    
    public void addTie(){
        ties ++;
    }
    
    public int getTies(){
        return ties;
    }
    
    
}
