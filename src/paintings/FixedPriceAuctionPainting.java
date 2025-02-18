package paintings;
import players.Player;
public class FixedPriceAuctionPainting extends Painting{
    public FixedPriceAuctionPainting(int artist_id){
        super(artist_id);
    }

    @Override
    public String getType() {
        return "Fixed Price Auction";
    }

    @Override
    public void auction(Player[] players) {
        int fixedBid;
        while (true) {
            System.out.println(this.owner.getName() + ", please fix a price for the auction");
            fixedBid = this.owner.bid(currentBid, this);
            if (fixedBid >= 0) {
                break;
            }
        }
        currentBid = fixedBid;
        System.out.println("The fixed price is " + fixedBid);
        int ownerIndex = -1;
        for (int i = 0; i < players.length; i++) {
            if (players[i] == this.owner) {
                ownerIndex = i;
                break;
            }
        }

        boolean bought = false;
        for (int i = 1; i < players.length; i++) {
            int currentPlayerIndex = (ownerIndex + i) % players.length;
            Player player = players[currentPlayerIndex];

            if (player != this.owner) {
                int playerBid = player.bid(currentBid, this);

                if (playerBid == fixedBid) {
                    currentBidder = player;
                    bought = true;
                    break;
                }
            }
        }
        if (!bought) {
            currentBidder = this.owner;
        }

        sold();
    }

}
