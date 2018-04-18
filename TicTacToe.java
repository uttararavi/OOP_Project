import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class TicTacToe {

    public int board[][];
    int dimension = 3;
    int human = 1, computer = 0;

    int[][] preferredMoves = {
         {1, 1}, {0, 0}, {0, 2}, {2, 0}, {2, 2},
         {0, 1}, {1, 0}, {1, 2}, {2, 1}};

    JButton buttons[][] = new JButton[3][3];
    JLabel resultLabel = new JLabel();
    //starting waala option?

    //TTT constructor
    TicTacToe(boolean start) {
        board = new int[dimension][dimension];

        //decidng who starts
        if (start) {
            human = 0;
            computer = 1;
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setBounds(150 + j*100, 150 + i*100, 100, 100);
            }
        }

        //displays result at the end
        resultLabel.setBounds(250, 500, 100, 50);

        //starting waala option??
    }

    int[] makeMove() {
        int[][] currentBoard = board;

        // If theres a move that makes computer win, take it
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int[] move = {i, j};
                if (board[i][j] == -1) {
                    board[i][j] = computer;
                    //checking if computer can win using that move
                    if (checkBoard() == computer)
                        return move;
                    board[i][j] = -1;
                }
            }
        }

        // If theres a move that makes human win, prevent it
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                int[] move = {i, j};
                if (board[i][j] == -1) {
                    board[i][j] = human;
                    //checking if human can win using that move
                    if (checkBoard() == human) {
                        //place computer's move in that square
                        board[i][j] = computer;
                        return move;
                    }
                    board[i][j] = -1;
                }
            }
        }

        //if neither of the above fill in using the list of prefferedMoves
        for (int[] move : preferredMoves) {
            if (board[move[0]][move[1]] == -1) {
                board[move[0]][move[1]] = computer;
                return move;
            }
        }
        return null;
    }


    int checkBoard() { // Returns winner, -1 if no winner yet, -2 if draw

        for (int symbol = 0; symbol < 2; symbol++) { // checks X and O 
            for (int i = 0; i < dimension; i++) {
                boolean result = false;
                int count1 = 0; 
                int count2 = 0; 

                // For vertical and horizontal
                for (int j = 0; j < dimension; j++) {
                    if (board[i][j] == symbol)
                        count1++;
                    if (board[j][i] == symbol)
                        count2++;
                    
                }

                if (count1 == dimension || count2 == dimension)
                  return symbol;
            }

            //for both the diagonals

            int count1 = 0, count2 = 0;
            for (int diag = 0; diag < dimension; diag++) {
                if (board[diag][diag] == symbol)
                    count1++;
                if (board[dimension-diag-1][diag] == symbol)
                    count2++;
            }
            if (count1 == dimension || count2 == dimension)
                return symbol;
        }

        //checking how many squares have been filled
        int emptyCount = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if(board[i][j] == -1)
                    emptyCount++;
            }
        }

        //if all filled then draw
        if (emptyCount == 0)
            return -2; // draw

        //else game is still in play
        return -1;
    }

    // void showBoard() {
    //     for (int i = 0; i < 3; i++) {
    //         for (int j = 0; j < 3; j++) {
    //             System.out.print(board[i][j] + "  ");
    //         }
    //         System.out.println();
    //     }
    //     System.out.println();
    //     System.out.println();
    // }

    void endGame()
    {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                buttons[i][j].setEnabled(false);
            }
        }

        int winner = checkBoard();

        if (winner == computer)
            resultLabel.setText("You lost!");
        else if (winner == human)
            resultLabel.setText("You won!");
        else if (winner == -2)
            resultLabel.setText("Its a draw!");
    }

    void playGame(boolean start, TicTacToe game){

                //initializing the hidden board
        int[][] board = {{-1, -1, -1}, {-1, -1, -1}, {-1, -1, -1}};
        game.board = board;

        if (!start) {
            int computerMove[] = game.makeMove();
            game.buttons[computerMove[0]][computerMove[1]].setText((game.computer == 0)? "X" : "O");
            //disable the button after move is made
            game.buttons[computerMove[0]][computerMove[1]].setEnabled(false);
        }

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLayout(null);
        frame.setResizable(false);
        frame.setVisible(true);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int fi = i, fj = j;
                game.buttons[i][j].addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // if (game.checkBoard() != -1) {
                        //     game.endGame();
                        //     return;
                        // }

                        if (game.human == 0) {
                            game.buttons[fi][fj].setText("X");
                            game.board[fi][fj] = 0;
                        }
                        else {
                            game.buttons[fi][fj].setText("O");
                            game.board[fi][fj] = 1;
                        }
                        game.buttons[fi][fj].setEnabled(false);

                        //checking if game is done
                        if (game.checkBoard() != -1) { 
                            game.endGame();
                            return;
                        }

                        int computerMove[] = game.makeMove();
                        game.buttons[computerMove[0]][computerMove[1]].setText((game.computer == 0)? "X" : "O");
                        game.buttons[computerMove[0]][computerMove[1]].setEnabled(false);

                        //checking if game is done
                        if (game.checkBoard() != -1) {
                            game.endGame();
                            return;
                        }
                    }
                });
                frame.add(game.buttons[i][j]);
                frame.add(game.resultLabel);
            }
        }

    }
    public static void main(String[] args) {
            
        boolean start = true;
        TicTacToe game = new TicTacToe(start);

        //playGame(start,game);
  }
}
