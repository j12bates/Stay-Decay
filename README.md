# Stay and Decay
_v1.1_

This game is played using a 3x3[^1] matrix of two-sided chips/coins.
Two players can play, with one player (A) facing the horizontal and one (B) facing the vertical.
The order of the chips is left-to-right and then top-to-bottom from each player's perspective [[Fig.1]](#order-fig1).
The object is to create as many **changes in the chip-state**[^2] on one's order [[Fig.2]](#scoring-example-fig2).

The matrix starts will all chips on one state, except the centre [[Fig.3]](#matrix-initialization-fig3).
On one's turn, one must flip a solitary chip on the matrix.
Both players must each take a turn before touching the same chip.
This also applies to the centre after the game starts.
The game ends when a diagonal on the matrix is entirely the same state [[Fig.4]](#scoring-example-with-game-end-fig4).
If both players have the same score, the next player wins. This is called **Staymate**.
If the difference in scores is the greatest possible (difference of 4 on a 3x3 matrix), it is called **Decaymate**.

[^1]: The game will be tested out on larger odd-numbered matrices.
[^2]: If two consecutive chips are of the same state, this is called "stay."
If they are of different states, this is called "decay."
The idea is to maximize decay on one's order whilst maximizing stay on one's opponent's.

## Diagrams

### Order (Fig.1)

#### Player A
X     | A | B | C
  --- | - | - | -
**1** | 0 | 1 | 2
**2** | 3 | 4 | 5
**3** | 6 | 7 | 8

#### Player B
X     | A | B | C
  --- | - | - | -
**1** | 2 | 5 | 8
**2** | 1 | 4 | 7
**3** | 0 | 3 | 6


### Scoring Example with Game End (Fig.4)
X     | A   | B   | C
  --- | --- | --- | ---
**1** | *0* | 1   | 0
**2** | 0   | *0* | 1
**3** | 1   | 0   | *0*

Order (A): 0 **1** 0 0 0 **1** 1 **0** 0

Order (B): 1 **0** 0 0 0 **1** **0** **1** **0**

Score: 4,5 (B wins)

### Scoring Example (Fig.2)
X     | A   | B   | C
  --- | --- | --- | ---
**1** | X   | O   | O
**2** | O   | X   | O
**3** | X   | X   | O

Order (A): X **O** O O **X** **O** **X** X **O**

Order (B): X **O** **X** X X **O** O O O

Score: 5,3

### Matrix Initialization (Fig.3)
X     | A   | B   | C
  --- | --- | --- | ---
**X** | O   | O   | O
**2** | O   | X   | O
**3** | O   | O   | O