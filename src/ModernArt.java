
import java.util.concurrent.ThreadLocalRandom;
import java.util.ArrayList;
import java.util.List;

import paintings.*;
import players.*;

/**
 * This class represents the ModernArt game and control 
 * the main logic of the game
 * 
 * You cannot add any new field to this class
 * You cannot add any new public method to this class
 * You cannot change the visibility of the existing methods and fields
 */
public class ModernArt{

    /**
     * PRE_DEAL contains the number of paintings that should be dealt to each
     * player before each round
     *
     * So for example, PRE_DEAL[3] means for 3 players, the number of painting
     * to be dealt to each player before round 1 is 10, round 2 is 6, and round
     * 3 is 6, and round 4 is 0
     */
    public static final int[][] PRE_DEAL = {null, null, null, //game can't be played for 0, 1, 2 players
        {10, 6, 6, 0}, {9, 4, 4, 0}, {8, 3, 3, 0}};
    /**
     * The game has 4 rounds in total
     */
    public static final int ROUND = 4;
    /**
     * The initial money each player has is 100
     */
    public static final int INITIAL_MONEY = 100;
    /**
     *
     * @deprecated
     *
     * Use PAINTS instead
     *
     * The number of paintings for each artist is fixed 
     * "0. Manuel Carvalho" = 12 , 
     * "1. Sigrid Thaler" = 13, 
     * "2. Daniel Melim" = 15, 
     * "3. Ramon Martins" = 15, 
     * "4. Rafael Silveira" = 15
     */
    @Deprecated
    public static final int[] INITIAL_COUNT = {12, 13, 15, 15, 15};
    /**
     * Combination of paints for each artist
     *
     * Artist             | Open Auction | Hidden Auction | One Offer Auction | Fixed Price Auction 
     * 0. Manuel Carvalho | 3            | 3              | 3                 | 3 
     * 1. Sigrid Thaler   | 4            | 3              | 3                 | 3 
     * 2. Daniel Melim    | 4            | 3              | 4                 | 4 
     * 3. Ramon Martins   | 4            | 4              | 4                 | 3 
     * 4. Rafael Silveira | 4            | 4              | 3                 | 4
     */
    public static final int[][] PAINTS = {{3, 3, 3, 3}, {4, 3, 3, 3}, {4, 3, 4, 4}, {4, 4, 4, 3}, {4, 4, 3, 4}};

    /**
     * The price of the most sold paintings is 30, the second most sold is 20,
     * and the third most sold is 10
     *
     * Tie-breaker: if two artists have the same number of painting sold the one
     * with the lower id will be the winner, i.e.,
     *
     * If 0. Manuel Carvalho and 1. Sigrid Thaler have the same number of
     * paintings sold then 0. Manuel Carvalho will be considered have more
     * paintings sold than 1. Sigrid Thaler
     *
     */
    private static final int SCORES[] = {30, 20, 10};
    /**
     * Each round a painting can only be played for 5 times. The 5th time the
     * painting is played, it will not be placed in auction and that round ends
     * immediately
     */
    private static final int MAX_PAINTINGS = 5;
    /**
     * The number of players in the game, it should be between 3-5
     */
    private int noOfPlayers;
    /**
     * The array of players in the game
     */
    private Player[] players;
    /**
     * The deck of paintings
     */
    private List<Painting> deck = new ArrayList<>();
    /**
     * The score board of the game
     */
    private int[][] scoreboard = new int[ROUND][Painting.ARTIST_NAMES.length];

    public static void main(String[] args) {
        if (args.length == 1) {
            try {
                int noOfPlayers = Integer.parseInt(args[0]);
                if (noOfPlayers < 3 || noOfPlayers > 5) {
                    throw new Exception();
                }
                new ModernArt(noOfPlayers).startgame();
            } catch (Exception e) {
                System.out.println("Invalid argument. Please enter a valid integer between 3-5.");
            }
        } else {
            new ModernArt().startgame();
        }
    }


    /**
     * Default constructor, the game will be played with 3 players by default
     */
    public ModernArt() {
        this.noOfPlayers = 3;
        this.players = new Player[noOfPlayers];
        players[0] = new Player(INITIAL_MONEY);
        players[1] = new ComputerPlayer(INITIAL_MONEY, scoreboard);

        for (int i = 2; i < noOfPlayers; i++) {
            players[i] = new AFKPlayer(INITIAL_MONEY);
        }
        prepareDeck();

        //TODO
    }

    /**
     * Given, #updated.
     * It will creates a game with the given number of players.
     * Player 1 is a human player, same as PA2
     * Player 2 is a computer player that will bid rationally
     * Player 3 and above are AFK players - Away From Keyboard, they will not bid and will play only the first painting in their round.
     */
    public ModernArt(int noOfPlayers) {
        this.noOfPlayers = noOfPlayers;
        this.players = new Player[noOfPlayers];
        players[0] = new Player(INITIAL_MONEY);
        players[1] = new ComputerPlayer(INITIAL_MONEY, scoreboard);

        for (int i = 2; i < noOfPlayers; i++) {
            players[i] = new AFKPlayer(INITIAL_MONEY);
        }
        prepareDeck();

    }

    /**
     * #updated
     * Prepare the deck of paintings
     * 
     * We have 5 artists, each artist has 4 types of paintings,
     * according to the table PAINTS
     */
    public void prepareDeck() {
        for (int i = 0; i < PAINTS.length; i++) {
            for (int j = 0; j < PAINTS[i].length; j++) {
                for (int k = 0; k < PAINTS[i][j]; k++) {
                    if (j == 0) {
                        deck.add(new OpenAuctionPainting(i));
                    } else if (j == 1) {
                        deck.add(new HiddenAuctionPainting(i));
                    } else if (j == 2) {
                        deck.add(new OneOfferAuctionPainting(i));

                    } else if (j == 3) {
                        deck.add(new FixedPriceAuctionPainting(i));
                    }
                }
            }
        }


        //TODO

        shuffle(deck);
    }

    /**
     * Deal the paintings to the players. After this method,
     * each player should receive some number of new paintings in their hand
     * as specified in the PRE_DEAL array
     *
     * The parameter round indicate which round the game is currently in
     */
    public void dealPainting(int round) {

        for (int i = 0; i < players.length; i++) {
            for(int j=0;j< PRE_DEAL[noOfPlayers][round];j++){
                players[i].dealPaintings(deck.remove(0));

            }
        }


        //TODO
    }

    /**
     * Deal the paintings to the players. After this method,
     * each player should receive some number of new paintings in their hand
     * as specified in the PRE_DEAL array
     *
     * The parameter round indicate which round the game is currently in
     */
    public int[] updateScoreboard(int round, int[] paintingCount) {
        int[] round_scoreboard = new int[Painting.ARTIST_NAMES.length];

        int first = -1, second = -1, third = -1;
        int firstInd = -1, secondInd = -1, thirdInd = -1;

        for (int i = 0; i < paintingCount.length; i++) {
            int count = paintingCount[i];

            if (count > first) {
                third = second;
                thirdInd = secondInd;
                second = first;
                secondInd = firstInd;

                first = count;
                firstInd = i;
            } else if (count > second || (count == first && i < firstInd)) {
                third = second;
                thirdInd = secondInd;

                second = count;
                secondInd = i;
            } else if (count > third || (count == second && i < secondInd)) {
                third = count;
                thirdInd = i;
            }
        }

        for (int i = 0; i < round_scoreboard.length; i++) {
            if (paintingCount[i] == 0) {
                round_scoreboard[i] = 0;
            } else if (i == firstInd) {
                round_scoreboard[i] = SCORES[0];
            } else if (i == secondInd) {
                round_scoreboard[i] = SCORES[1];
            } else if (i == thirdInd) {
                round_scoreboard[i] = SCORES[2];
            } else {
                round_scoreboard[i] = 0;
            }
        }

        for (int i = 0; i < round_scoreboard.length; i++) {
            scoreboard[round][i] = round_scoreboard[i];
        }

        int[] cumulativeScores = new int[round_scoreboard.length];
        for (int i = 0; i < cumulativeScores.length; i++) {
            if (round_scoreboard[i] == 0) {
                cumulativeScores[i] = 0;
            } else {
                for (int r = 0; r <= round; r++) {
                    cumulativeScores[i] += scoreboard[r][i];
                }
            }
        }

        return cumulativeScores;
    }

    /**
     * #updated - given
     * This is the main logic of the game and has been completed for you
     * You are not supposed to change this method
     */
    public void startgame() {
        int currentPlayer = 0;

        for (int round = 0; round < ROUND; round++) {
            //deal the paintings
            dealPainting(round);
            //start auction
            int[] paintingCount = new int[Painting.ARTIST_NAMES.length];
            while (true) {
                Player player = players[currentPlayer];
                Painting p = player.playPainting();

                if (p != null) {
                    System.out.println(p.getOwner().getName() + " plays " + p);
                    p.auction(players);
                    if (++paintingCount[p.getArtistId()] == MAX_PAINTINGS) {
                        break; //this round end immediately and the painting is not putting up for auction
                    }
                }

                currentPlayer = (currentPlayer + 1) % noOfPlayers;
                System.out.println("The number of painting sold: ");
                for (int i = 0; i < Painting.ARTIST_NAMES.length; i++) {
                    System.out.println(Painting.ARTIST_NAMES[i] + " " + paintingCount[i]);
                }
            }
            System.out.println("Complete auction - sell paintings");
            //update score board
            int[] scoreForThisRound = updateScoreboard(round, paintingCount);
            System.out.println("Print the score board after auction");
            System.out.print("\t\t");
            for (int j = 0; j < Painting.ARTIST_NAMES.length; j++) {
                System.out.print("\t" + j);
            }
            for (int i = 0; i < round + 1; i++) {
                System.out.print("\nRound " + i + ":\t\t");

                for (int j = 0; j < Painting.ARTIST_NAMES.length; j++) {
                    System.out.print(scoreboard[i][j] + "\t");
                }
            }

            System.out.println("\n\nPrint the price for each artist's painting");
            for (int i = 0; i < Painting.ARTIST_NAMES.length; i++) {
                System.out.println(Painting.ARTIST_NAMES[i] + " " + scoreForThisRound[i]);
            }

            //Sell the paintings
            for (Player p : players) {
                p.sellPainting(scoreForThisRound);
            }
        }
        System.out.println("Game over, score of each player");
        for (int i = 0; i < noOfPlayers; i++) {
            System.out.println(players[i]);
        }
    }

    /**
     * Shuffle the deck of paintings - given
     *
     * This method is completed for you.
     */
    public void shuffle(List<Painting> deck) {
        for (int i = 0; i < deck.size(); i++) {
            int index = ThreadLocalRandom.current().nextInt(deck.size());
            Painting temp = deck.get(i);
            deck.set(i, deck.get(index));
            deck.set(index, temp);
        }
    }

    /**
     * This method is completed for you. We use this for grading purpose
     *
     * You are not allowed to use this method in your code
     */
    public int[][] getScoreboard() {
        return scoreboard;
    }

    /**
     * This method is for grading purpose.
     *
     * You are not allowed to use this method in your code
     */
    public List<Painting> getDeck() {
        return deck;
    }

    /**
     * This method is for grading purpose.
     *
     * You are not allowed to use this method in your code
     */
    public Player[] getPlayers() {
        return players;
    }
}
