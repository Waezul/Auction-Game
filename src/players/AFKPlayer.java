package players;

import paintings.FixedPriceAuctionPainting;
import paintings.OpenAuctionPainting;
import paintings.Painting;

public class AFKPlayer extends Player {

    public AFKPlayer(int money) {
        super(money);

    }

    @Override
    public Painting playPainting(){
        Painting painting = handPaintings.get(0);
        handPaintings.remove(0);
        return painting;
    }

    @Override
    public int bid(int currentBid,Painting p) {
        if(p instanceof FixedPriceAuctionPainting && !(p.getOwner() ==this)){
            System.out.println(this.getName()+" pass.");
            return 0;
        }if(p instanceof OpenAuctionPainting || p instanceof FixedPriceAuctionPainting){
            return 0;
        }else{
            System.out.println(this);
            return 0;
        }

    }
//    @Override
//    public String toString() {
//        return this.getName()+" bids "+0;
//    }
}
