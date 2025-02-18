package players;

import paintings.Painting;

import java.util.concurrent.ThreadLocalRandom;

public class ComputerPlayer extends Player {
    private int [][] scoreboard ;
    private int cpuBid=0;
    public ComputerPlayer(int money, int[][] scoreboard) {
        super(money);
        this.scoreboard = scoreboard;

    }
    @Override
    public Painting playPainting(){
        Painting painting = handPaintings.get(ThreadLocalRandom.current().nextInt(0,handPaintings.size()));
        handPaintings.remove(painting);
        return painting;
    }

    @Override
    public int bid(int currentBid, Painting p) {
        int maxBid = doValuation(p);
        int availableFunds = this.getMoney();
        if(p.getType().equals("Open Auction")){
            if(availableFunds > currentBid){
                cpuBid = ThreadLocalRandom.current().nextInt(0,maxBid+1);
                if(cpuBid>currentBid){
                    System.out.println(this.getName()+" bids " + cpuBid);
                }
                return cpuBid;
            }else{
                return 0;
            }
        } else if (p.getType().equals("Hidden Auction")) {
            cpuBid = ThreadLocalRandom.current().nextInt(0,maxBid+1);
            System.out.println(this.getName()+" bids " + cpuBid);
            return cpuBid;
        } else if (p.getType().equals("Fixed Price Auction")) {
            if (p.getOwner()==this){
                cpuBid = ThreadLocalRandom.current().nextInt(0,availableFunds+1);
                return cpuBid;
            }
            cpuBid = ThreadLocalRandom.current().nextInt(0,maxBid+1);
            if ((cpuBid>currentBid||cpuBid==currentBid) && p.getOwner()!= this){
                cpuBid = currentBid;
                return cpuBid;
            }else{
                System.out.println(this.getName()+" pass.");
                return 0;
            }


        } else if (p.getType().equals("One Offer Auction")) {
            cpuBid = ThreadLocalRandom.current().nextInt(0,maxBid+1);
            System.out.println(this.getName()+ " bids " + cpuBid);
            return cpuBid;
        }else {
            return 0;
        }

    }


    private int doValuation(Painting p){
        int index = p.getArtistId();
        int value = 30;
        for(int i = 0; i < scoreboard.length; i++){
            for(int j = 0; j < scoreboard[i].length; j++){
                if(j==index){
                    value += scoreboard[i][j];
                    if(i == scoreboard.length-1 && scoreboard[i][j]== 0){
                        value = 30;
                    }
                }
            }
        }
        if(p.getOwner()==this){
            value = value/2;
            if(value>getMoney()){
                return getMoney();
            }else {
                return value;
            }
        }
        if(value>getMoney()){
            return getMoney();
        }
        return value;
    }



//    @Override
//    public String toString() {
//        return this.getName()+" bids "+cpuBid;
//    }
}
