package murilo.ghignatti;

import exceptions.NoSuchVertexException;

public class MiniMaxAlphaBeta {

    private static final int MAX_DEPTH = 12;

    private MiniMaxAlphaBeta() {
    }

    public static int miniMax(TicTacToe board, int depth, int alpha, int beta,
            boolean isMax) throws NoSuchVertexException {
        //int boardVal = evaluateBoard(board);
        int boardVal = board.checkWinner();

        // Terminal node (win/lose/draw) or max depth reached.
        //if (Math.abs(boardVal) == 10 || depth == 0 || !board.anyMovesAvailable()) {
          if((boardVal == 1 || boardVal == 2) || depth == 0 || !board.anyMovesAvailable())
              return boardVal;

        // Maximising player, find the maximum attainable value.
        if (isMax) {
            int highestVal = Integer.MIN_VALUE;
            for (int row = 0; row < board.getWidth(); row++) {
                for (int col = 0; col < board.getWidth(); col++) {
                    //if (!board.isTileMarked(row, col)) {
                      if(!board.isMarked(row, col)){
                        //board.setMarkAt(row, col, X);
                        board.markCross(row+1, col+1);
                        highestVal = Math.max(highestVal, miniMax(board,
                                depth - 1, alpha, beta, false));
                        //board.setMarkAt(row, col, BLANK);
                        board.markBlank(row+1, col+1);
                        alpha = Math.max(alpha, highestVal);
                        if (alpha >= beta) {
                            return highestVal;
                        }
                    }
                }
            }
            return highestVal;
            // Minimising player, find the minimum attainable value;
        } else {
            int lowestVal = Integer.MAX_VALUE;
            for (int row = 0; row < board.getWidth(); row++) {
                for (int col = 0; col < board.getWidth(); col++) {
                    //if (!board.isTileMarked(row, col)) {
                      if(!board.isMarked(row, col)){  
                        //board.setMarkAt(row, col, O);
                        board.markCircle(row+1, col+1);
                        lowestVal = Math.min(lowestVal, miniMax(board,
                                depth - 1, alpha, beta, true));
                        //board.setMarkAt(row, col, BLANK);
                        board.markBlank(row+1, col+1);
                        beta = Math.min(beta, lowestVal);
                        if (beta <= alpha) {
                            return lowestVal;
                        }
                    }
                }
            }
            return lowestVal;
        }
    }

    public static int[] getBestMove(TicTacToe board) throws NoSuchVertexException {
        int[] bestMove = new int[]{-1, -1};
        int bestValue = Integer.MIN_VALUE;

        for (int row = 0; row < board.getWidth(); row++) {
            for (int col = 0; col < board.getWidth(); col++) {
                //if (!board.isTileMarked(row, col)) {
                  if(!board.isMarked(row, col)){
                    //board.setMarkAt(row, col, X);
                    board.markCross(row+1, col+1);
                    int moveValue = miniMax(board, MAX_DEPTH, Integer.MIN_VALUE,
                            Integer.MAX_VALUE, false);
                    //board.setMarkAt(row, col, BLANK);
                    board.markBlank(row+1, col+1);
                    if (moveValue > bestValue) {
                        bestMove[0] = row;
                        bestMove[1] = col;
                        bestValue = moveValue;
                    }
                }
            }
        }
        return bestMove;
    }
}
