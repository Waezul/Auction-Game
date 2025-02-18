ModernArt Game

The ModernArt game is a simulation of an auction game where players compete to buy paintings from various artists. The game has different rounds, each with specific auction types, where players attempt to acquire paintings and earn money based on their success. The game features human players, computer players, and AFK (Away From Keyboard) players.

Features:
Multiple Auction Types: Includes Open Auction, Hidden Auction, One Offer Auction, and Fixed Price Auction.
Auction Rounds: The game runs for 4 rounds with a series of auctions, each auction round involving different players bidding on paintings.
Player Interactions: Players can be human or AI-controlled, with different behaviors for each.
Scoring System: Players earn money based on the paintings they purchase, and a leaderboard is updated after each round.

Table of Contents:
Installation
Usage
Game Flow
Classes
Contributing
License
Installation


Requirements:
Java 8 or later
A Java IDE (e.g., IntelliJ IDEA, Eclipse) or a text editor (e.g., VS Code)

Usage
Starting the Game:
The game starts with a default of 3 players. To play with more players (between 3 and 5), you can pass an integer argument specifying the number of players when running the program:
bash
Copy
Edit
java ModernArt 4
The game will display the auction rounds and the corresponding results after each round, including the players' scores.
Game Flow:
Preparation: Each round, paintings are dealt to players, and the deck is shuffled.
Auction Phase: Players bid on paintings in different auction formats.
Score Calculation: After the auctions, scores are calculated based on the number of paintings each player has acquired, and the scoreboard is updated.
End of Game: After 4 rounds, the game ends, and the final scores for each player are displayed.
Game Flow
1. Setup Phase:
The game begins with an initialization phase where players are created (Human, Computer, or AFK).
A deck of paintings is prepared and shuffled.
2. Auction Phase:
In each round, paintings are dealt to players according to the PRE_DEAL matrix.
Players take turns playing paintings, and the auction for each painting is held.
Open Auction: All players can see the current bid.
Hidden Auction: Bids are hidden from other players.
One Offer Auction: A single bid is made.
Fixed Price Auction: The painting has a fixed price.
3. Score Calculation:
The number of paintings sold for each artist is tracked.
The top three artists are awarded points based on the number of paintings sold, with ties broken by artist ID.
4. End Phase:
After the final round, the total score of each player is displayed.
Classes
Here is an overview of the key classes in the ModernArt game:

ModernArt:

The main class that controls the game flow.
Manages the number of players, deals paintings, and handles auctions.
Player:

Represents a player in the game (human, computer, or AFK).
Methods for bidding, selling paintings, and interacting with the game.
ComputerPlayer:

An AI-controlled player that bids rationally based on the current situation in the game.
AFKPlayer:

A player that doesn't participate actively in the auction, but still plays paintings when their turn comes.
Painting:

Represents a painting that can be sold or bought in an auction.
Has attributes like artist, auction type, and owner.
Auction Types:

OpenAuctionPainting: Represents a painting in an open auction.
HiddenAuctionPainting: Represents a painting in a hidden auction.
OneOfferAuctionPainting: Represents a painting in a one-off auction.
FixedPriceAuctionPainting: Represents a painting with a fixed price auction.
