package paintings;
import players.Player;
public class OneOfferAuctionPainting extends Painting{
    public OneOfferAuctionPainting(int artist_id){
        super(artist_id);
    }

    @Override
    public String getType() {
        return "One Offer Auction";
    }

    @Override
    public void auction(Player[] players) {
        int ownerIndex = owner(players);

        currentBid =0;
        for(int i =1;i<= players.length;i++){
            int currentPlayerIndex = (ownerIndex+i)%players.length;
            int playerBid = players[currentPlayerIndex].bid(currentBid,this);
            if(playerBid > currentBid){
                currentBid = playerBid;
                currentBidder = players[currentPlayerIndex];
            }
        }
        sold();
    }
    private int owner(Player[] players) {
        for (int i = 0; i < players.length; i++) {
            if (players[i] == this.owner) {
                return i;
            }
        }
        return -1;
    }
}
