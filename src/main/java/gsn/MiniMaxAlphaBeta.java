package gsn;

import exceptions.NoSuchVertexException;

public class MiniMaxAlphaBeta {

    private static final int MAX_DEPTH = 6;

    private MiniMaxAlphaBeta() {
    }

    public static int miniMax(TicTacToe game, int depthValue, int valAlpha, int valBeta,
                              boolean isMax) throws NoSuchVertexException {

        int[] boardVal = game.checkWinner();


        //if ((boardVal == 1 || boardVal == 2) || depthValue == 0 || !game.anyMovesAvailable())
        //    return boardVal;

        if((boardVal[0] == 100 || boardVal[1] == 100) || depthValue == 0|| !game.anyMovesAvailable()){
            if(boardVal[0] > boardVal[1])
                return -1 * boardVal[0];

            else if(boardVal[1] > boardVal[0])
                return boardVal[1];

            else if(boardVal[0] == -1 && boardVal[1] == -1)
                return -1;

            else if(boardVal[0] == boardVal[1])
                return -1 * boardVal[0];

            else if(depthValue == 0)
                return boardVal[0] > boardVal[1] ? -1 * boardVal[0] : boardVal[1];
        }

        if (isMax) {
            int highestVal = Integer.MIN_VALUE;
            for (int rowIndex = 0; rowIndex < game.getWidth(); rowIndex++) {
                for (int colIndex = 0; colIndex < game.getWidth(); colIndex++) {
                    if (!game.isMarked(rowIndex, colIndex)) {
                        game.markCross(rowIndex + 1, colIndex + 1);
                        highestVal = Math.max(highestVal, miniMax(game,
                            depthValue - 1, valAlpha, valBeta, false));

                        game.markBlank(rowIndex + 1, colIndex + 1);
                        valAlpha = Math.max(valAlpha, highestVal);
                        if (valAlpha >= valBeta) {
                            return highestVal;
                        }
                    }
                }
            }
            return highestVal;
        } else {
            int lowestVal = Integer.MAX_VALUE;
            for (int rowIndex = 0; rowIndex < game.getWidth(); rowIndex++) {
                for (int colIndex = 0; colIndex < game.getWidth(); colIndex++) {
                    if (!game.isMarked(rowIndex, colIndex)) {
                        game.markCircle(rowIndex + 1, colIndex + 1);
                        lowestVal = Math.min(lowestVal, miniMax(game,
                            depthValue - 1, valAlpha, valBeta, true));
                        game.markBlank(rowIndex + 1, colIndex + 1);
                        valBeta = Math.min(valBeta, lowestVal);
                        if (valBeta <= valAlpha) {
                            return lowestVal;
                        }
                    }
                }
            }
            return lowestVal;
        }
    }

    public static int[] getBestCoord(TicTacToe game) throws NoSuchVertexException {
        int[] bestCoord = new int[]{-1, -1};
        int bestValue = Integer.MIN_VALUE;
        for (int rowIndex = 0; rowIndex < game.getWidth(); rowIndex++) {
            for (int colIndex = 0; colIndex < game.getWidth(); colIndex++) {
                if (!game.isMarked(rowIndex, colIndex)) {
                    game.markCross(rowIndex + 1, colIndex + 1);
                    int moveValue = miniMax(game, MAX_DEPTH, Integer.MIN_VALUE,
                        Integer.MAX_VALUE, false);
                    game.markBlank(rowIndex + 1, colIndex + 1);
                    if (moveValue > bestValue) {
                        bestCoord[0] = rowIndex;
                        bestCoord[1] = colIndex;
                        bestValue = moveValue;
                    }
                }
            }
        }
        return bestCoord;
    }
}
