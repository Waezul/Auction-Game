package paintings;
import players.Player;

import java.util.ArrayList;

public class HiddenAuctionPainting extends Painting{
    public HiddenAuctionPainting(int artist_id){
        super(artist_id);
    }

    @Override
    public String getType() {
        return "Hidden Auction";
    }

    @Override
    public void auction(Player[] players) {
        ArrayList<Integer> bidList = new ArrayList<>();

        for (int i = 0; i < players.length; i++) {
            int playerBid = players[i].bid(currentBid, this);
            bidList.add(playerBid);
        }

        int ownerIndex = owner(players);
        int winningIndex = maxIndex(bidList, ownerIndex);
        currentBidder = players[winningIndex];
        currentBid = bidList.get(winningIndex);
        sold();
    }

    private int maxIndex(ArrayList<Integer> list, int ownerIndex) {
        int maxIndex = 0;

        for (int i = 1; i < list.size(); i++) {
            if (list.get(i) > list.get(maxIndex)) {
                maxIndex = i;
            } else if (list.get(i) == list.get(maxIndex)) {
                if (closer(ownerIndex, i, maxIndex, list.size())) {
                    maxIndex = i;
                }
            }
        }
        return maxIndex;
    }

    private boolean closer(int ownerIndex, int i, int maxIndex, int playerCount) {
        int playerDistance = (i - ownerIndex + playerCount) % playerCount;
        int highestDistance = (maxIndex - ownerIndex + playerCount) % playerCount;
        return playerDistance < highestDistance;
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
