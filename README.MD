# Stay & Decay
_v1.0_

This game is played using a 3x3[^1] matrix of two-sided chips/coins.
Two players can play, with one player (A) facing the horizontal and one (B) facing the vertical.
The order of the chips is left-to-right and then top-to-bottom from each player's perspective. [Fig.1][Order (Fig.1)]
The object is to create as many **changes in the chip-state**[^2] on one's order. [Fig.2][Scoring Example (Fig.2)]

The matrix starts will all chips on one state, except the centre. [Fig.3][Matrix Initialization (Fig.3)]
On one's turn, one must flip a solitary chip on the matrix.
A chip cannot be touched twice within three consecutive turns.
In other words, both players must each take a turn before touching the same chip.
The game ends when a diagonal on the matrix is entirely the same state. [Fig.4][Scoring Example with Game End (Fig.4)]

[^1]: The game will be tested out on larger odd-numbered matrices.
[^2]: If two consecutive chips are of the same state, this is called "stay."
If they are of different states, this is called "decay."
The idea is to maximize decay on one's order whilst maximizing stay on one's opponent's.

## Diagrams (A,B)

### Order (Fig.1)
      | A   | B   | C
  --- | --- | --- | ---
**1** | 0,2 | 1,5 | 2,8
**2** | 3,1 | 4,4 | 5,7
**3** | 6,0 | 7,3 | 8,6

### Scoring Example (Fig.2)
      | A   | B   | C
  --- | --- | --- | ---
**1** | 1   | 0   | 0
**2** | 0   | 1   | 0
**3** | 1   | 1   | 0
Order (A): 1 **0** 0 0 **1** **0** **1** 1 **0**
Order (B): 1 **0** **1** 1 1 **0** 0 0 0
Score: 5,3

### Matrix Initialization (Fig.3)
      | A   | B   | C
  --- | --- | --- | ---
**1** | 0   | 0   | 0
**2** | 0   | 1   | 0
**3** | 0   | 0   | 0

### Scoring Example with Game End (Fig.4)
      | A   | B   | C
  --- | --- | --- | ---
**1** | *0* | 1   | 0
**2** | 0   | *0* | 1
**3** | 1   | 0   | *0*
Order (A): 0 **1** 0 0 0 **1** 1 **0** 0
Order (B): 1 **0** 0 0 0 **1** **0** **1** **0**
Score: 4,5 (B wins)
