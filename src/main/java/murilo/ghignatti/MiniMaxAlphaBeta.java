package murilo.ghignatti;

import exceptions.NoSuchVertexException;

import javax.swing.plaf.synth.SynthOptionPaneUI;

import static murilo.ghignatti.TicTacToe.*;
public class MiniMaxAlphaBeta {
    private static final int MAX_DEPTH = 28; //12

    private MiniMaxAlphaBeta(){
    }
    public static int miniMax(TicTacToe board, int depth, int alpha, int beta,
                              boolean isMax) throws NoSuchVertexException {
        int boardValue = board.checkWinner();
        if( boardValue == 2 || boardValue == 1
            || depth == 0
            || !board.anyMovesAvailable()){
            return boardValue;
        }
        if(isMax){
            int highestValue = Integer.MIN_VALUE;
            for(int row = 0; row < board.getWidth();row++){
                for (int col = 0; col < board.getWidth(); col++) {
                    if(!board.isMarked(row,col)){
                        board.markCross(row+1,col+1);
                        highestValue = Math.max(highestValue,miniMax(
                            board,depth-1, alpha, beta,false));
                        board.markBlank(row+1,col+1);
                        alpha = Math.max(alpha, highestValue);
                        if(alpha >= beta){
                            System.out.println("teste");
                            return highestValue;
                        }
                    }
                }
            }
            return highestValue;
        }
        else{
            int lowestValue = Integer.MAX_VALUE;
            for (int i = 0; i < board.getWidth(); i++) {
                for (int j = 0; j < board.getWidth(); j++) {
                    if(!board.isMarked(i,j)){
                        board.markCircle(i+1,j+1);
                        lowestValue = Math.min(lowestValue,miniMax(
                            board,depth-1, alpha, beta,true));
                        board.markBlank(i+1,j+1);
                        beta = Math.min(beta,lowestValue);
                        if(beta <= alpha){
                            return lowestValue;
                        }
                    }
                }
            }
             return lowestValue;
            }
        }
        public static int[] getBestMove(TicTacToe board) throws NoSuchVertexException {
            int [] bestMove = new int[]{-1, -1};
            int bestValue = Integer.MIN_VALUE;
            for (int i = 0; i < board.getWidth(); i++) {
                for (int j = 0; j < board.getWidth(); j++) {
                    if(!board.isMarked(i,j)){
                        board.markCross(i+1,j+1);
                        int moveValue = miniMax(board,MAX_DEPTH,Integer.MIN_VALUE,
                            Integer.MAX_VALUE, false);
                        board.markBlank(i+1,j+1);
                        if(moveValue > bestValue){
                            bestMove[0] = i;
                            bestMove[1] = j;
                            bestValue = moveValue;
                        }
                    }
                }
            }
            return bestMove;
        }
    private static int evaluateBoard(TicTacToe board) {
        int rowSum = 0;
        int bWidth = board.getWidth();
        int Xwin = 'X' * bWidth;
        int Owin = 'O' * bWidth;
        // Check rows for winner.
        for (int row = 0; row < bWidth; row++) {
            for (int col = 0; col < bWidth; col++) {
                rowSum += board.getMarkAt(row, col);
            }
            if (rowSum == Xwin) {
                return 10;
            } else if (rowSum == Owin) {
                return -10;
            }
            rowSum = 0;
        }

        // Check columns for winner.
        rowSum = 0;
        for (int col = 0; col < bWidth; col++) {
            for (int row = 0; row < bWidth; row++) {
                rowSum += board.getMarkAt(row, col);
            }
            if (rowSum == Xwin) {
                return 10;
            } else if (rowSum == Owin) {
                return -10;
            }
            rowSum = 0;
        }

        // Check diagonals for winner.
        // Top-left to bottom-right diagonal.
        rowSum = 0;
        for (int i = 0; i < bWidth; i++) {
            rowSum += board.getMarkAt(i, i);
        }
        if (rowSum == Xwin) {
            return 10;
        } else if (rowSum == Owin) {
            return -10;
        }

        // Top-right to bottom-left diagonal.
        rowSum = 0;
        int indexMax = bWidth - 1;
        for (int i = 0; i <= indexMax; i++) {
            rowSum += board.getMarkAt(i, indexMax - i);
        }
        if (rowSum == Xwin) {
            return 10;
        } else if (rowSum == Owin) {
            return -10;
        }

        return 0;
    }
    }
