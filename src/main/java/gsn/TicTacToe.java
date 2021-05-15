package gsn;

import java.util.Scanner;

import exceptions.AdjacencyAlreadyExistsException;
import exceptions.NoSuchVertexException;
import exceptions.VertexAlreadyExistsException;
import graph.Graph;
import graph.Vertex;

enum GameState{
    START,
    END,
    WAIT,
    CLOSE,
    CROSS,
    CIRCLE
}

public class TicTacToe{

    private static final byte X     = 2;
    private static final byte O     = 1;
    private static final byte BLANK = 0;

    private byte[][] gameGrid;
    private int maxMarks;
    private int marks;
    private int gameSize;
    private Graph<String> graph;

    public TicTacToe(int gameSize) throws VertexAlreadyExistsException, NoSuchVertexException, AdjacencyAlreadyExistsException{
        if(gameSize % 2 == 0)
            throw new IllegalArgumentException("Size needs to be a odd number");
        this.gameSize = gameSize;
        this.gameGrid = new byte[gameSize][gameSize];
        this.maxMarks = gameSize * gameSize;
        this.marks = 0;
        //initGraph();
    }

    private void initGraph() throws VertexAlreadyExistsException, NoSuchVertexException, AdjacencyAlreadyExistsException{
        this.graph = new Graph<>(false, true);

        //Creating vertexes
        for(int l = 0; l < gameSize; l++)
            for(int c = 0; c < gameSize; c++)
                graph.addVertex(new Vertex<>(l + "," + c, "0"));

        //Creating adjacencies
        for(int l = 0; l < gameSize; l++)
            for(int c = 0; c < gameSize; c++)
                for(int i = 0; i < 6; i++){
                    switch(i){
                        //UP
                        case 0:
                            if(l - 1 < 0)
                                break;
                            else if(!graph.getVertex(l + "," + c).existAdjacency((l - 1) + "," + c))
                                graph.createAdjacency(l + "," + c, (l - 1) + "," + c);
                            break;
                        //DOWN
                        case 1:
                            if(l + 1 >= gameSize)
                                break;
                            else if(!graph.getVertex(l + "," + c).existAdjacency((l + 1) + "," + c))
                                graph.createAdjacency(l + "," + c, (l + 1) + "," + c);
                            break;
                        //LEFT
                        case 2:
                            if(c - 1 < 0)
                                break;
                            else if(!graph.getVertex(l + "," + c).existAdjacency(l + "," + (c - 1)))
                                graph.createAdjacency(l + "," + c, l + "," + (c - 1));
                            break;
                        //RIGHT
                        case 3:
                            if(c + 1 >= gameSize)
                                break;
                            else if(!graph.getVertex(l + "," + c).existAdjacency(l + "," + (c + 1)))
                                graph.createAdjacency(l + "," + c, l + "," + (c + 1));
                            break;
                        //Primary Diagonal
                        case 4:
                            if(l - 1 < 0 || c - 1 < 0)
                                break;
                            else if(!graph.getVertex(l + "," + c).existAdjacency((l - 1) + "," + (c - 1)))
                                graph.createAdjacency(l + "," + c, (l - 1) + "," + (c - 1));
                            if(l + 1 >= gameSize || c + 1 >= gameSize)
                                break;
                            else if(!graph.getVertex(l + "," + c).existAdjacency((l + 1) + "," + (c + 1)))
                                graph.createAdjacency(l + "," + c, (l + 1) + "," + (c + 1));
                            break;
                        case 5:
                            if(l - 1 < 0 || c + 1 >= gameSize)
                                break;
                            else if(!graph.getVertex(l + "," + c).existAdjacency((l - 1) + "," + (c + 1)))
                                graph.createAdjacency(l + "," + c, (l - 1) + "," + (c + 1));
                            if(l + 1 >= gameSize || c - 1 < 0)
                                break;
                            else if(!graph.getVertex(l + "," + c).existAdjacency((l + 1) + "," + (c - 1)))
                                graph.createAdjacency(l + "," + c, (l + 1) + "," + (c - 1));
                            break;
                        }
                    }
                }

    public void clearGame(){
        this.gameGrid = new byte[gameSize][gameSize];
        this.marks = 0;
    }

    private boolean mark(int pos1, int pos2, byte option) throws NoSuchVertexException{
        //Position 1 or 2 are greather then the grid
        if(pos1 > gameGrid.length || pos2 > gameGrid.length){
            return false;
        }
        //This position was already used before
        else if(gameGrid[pos1][pos2] != 0 && option != BLANK){
            return false;
        }
        else{
            gameGrid[pos1][pos2] = option;
            if(graph != null)
                graph.getVertex(pos1+","+pos2).setLabel(String.valueOf(option));
            if(option == BLANK)
                marks--;
            else
                marks++;
            return true;
        }
    }

    public boolean markCross(int pos1, int pos2) throws NoSuchVertexException{
        //boolean result = mark(pos1 - 1, pos2 - 1, X);
        //return result;
        return true;
    }

    public boolean markCircle(int pos1, int pos2) throws NoSuchVertexException{
        boolean result = mark(pos1 - 1, pos2 - 1, O);
        return result;
    }

    public boolean markBlank(int pos1, int pos2) throws NoSuchVertexException{
        boolean result = mark(pos1 - 1, pos2 - 1, BLANK);
        return result;
    }

    private int[] checkPercentage(int[][] chancePerPosition, int[] result){
        double sum;
        for(int lineChanceIndex = 0; lineChanceIndex < chancePerPosition.length; lineChanceIndex++){
            sum = 0;
            for(int columnChanceIndex = 0; columnChanceIndex < gameSize; columnChanceIndex++){
                if(chancePerPosition[lineChanceIndex][columnChanceIndex] == gameSize && result[lineChanceIndex] != 100){
                    result[lineChanceIndex] = 100;
                }
                else{
                    sum += chancePerPosition[lineChanceIndex][columnChanceIndex]/gameSize;
                    chancePerPosition[lineChanceIndex][columnChanceIndex] = 0;
                }
            }
            if(result[lineChanceIndex] != 100)
                if(result[lineChanceIndex] == -1)
                    result[lineChanceIndex] = (int)Math.round((sum/chancePerPosition[lineChanceIndex].length) * 100);
                else
                result[lineChanceIndex] += (int)Math.round((sum/chancePerPosition[lineChanceIndex].length) * 100);
        }
        return result;
    }

    private int[] checkPercentageDiagonal(int[][] chancePerPosition, int[] result){
        double sum;
        boolean chanceIsNull = false;
        for(int lineChanceIndex = 0; lineChanceIndex < chancePerPosition.length; lineChanceIndex++){
            sum = 0;
            for(int columnChanceIndex = 0; columnChanceIndex < gameSize; columnChanceIndex++){
                if(chancePerPosition[lineChanceIndex][columnChanceIndex] == -1)
                    chanceIsNull = true;
                sum += chancePerPosition[lineChanceIndex][columnChanceIndex];
                chancePerPosition[lineChanceIndex][columnChanceIndex] = 0;
            }
            if(chanceIsNull)
                sum = 0;
            if(result[lineChanceIndex] != 100)
                if(result[lineChanceIndex] == -1)
                    result[lineChanceIndex] = (int)Math.round((sum/chancePerPosition[lineChanceIndex].length) * 100);
                else
                result[lineChanceIndex] += (int)Math.round((sum/chancePerPosition[lineChanceIndex].length) * 100);
        }
        return result;
    }

    /**
     *
     * @return
     * <code>{@value #X}</code> if cross won;
     * <code>{@value #O}</code> if circle won;
     * <code>-1</code> if no one won
     * <code>-2</code> if the game ended with a tie.
     */
    public int[] checkWinner(){
        int[][] chancePerPosition = new int[2][gameSize];
        int[] result = new int[]{-1,-1};
        byte lastPlayer;
        if(marks == maxMarks)
            return result;
        else{
            //Check Line
            for(int lineIndex = 0; lineIndex < gameSize; lineIndex++){
                lastPlayer = -1;
                position:{
                    for(int columnIndex = 0; columnIndex < gameSize; columnIndex++){

                        if(lastPlayer == -1 && gameGrid[lineIndex][columnIndex] != 0)
                            lastPlayer = gameGrid[lineIndex][columnIndex];

                        else if(lastPlayer != gameGrid[lineIndex][columnIndex] && gameGrid[lineIndex][columnIndex] != 0){
                            if(lastPlayer == O)
                                chancePerPosition[0][lineIndex] = 0;
                            else
                                chancePerPosition[1][lineIndex] = 0;
                            break position;
                        }

                        //Add a point to the circle
                        if(gameGrid[lineIndex][columnIndex] == O)
                            chancePerPosition[0][lineIndex] += 1;

                        //Add a point to the cross
                        else if(gameGrid[lineIndex][columnIndex] == X)
                            chancePerPosition[1][lineIndex] += 1;
                    }
                }
                }

                checkPercentage(chancePerPosition, result);

                //If some one has winned we just return the result, don't need to check the rest
                if(result[0] == 100 || result[1] == 100)
                    return result;

            //Check Column
            for(int columnIndex = 0; columnIndex < gameGrid.length; columnIndex++){
                lastPlayer = -1;
                    //Running through the line
                    position:{
                        for(int lineIndex = 0; lineIndex < gameGrid.length; lineIndex++){

                            if(lastPlayer == -1 && gameGrid[lineIndex][columnIndex] != 0)
                                lastPlayer = gameGrid[lineIndex][columnIndex];
    
                            else if(lastPlayer != gameGrid[lineIndex][columnIndex] && gameGrid[lineIndex][columnIndex] != 0){
                                if(lastPlayer == O)
                                    chancePerPosition[0][lineIndex] = 0;
                                else
                                    chancePerPosition[1][lineIndex] = 0;
                                break position;
                            }
    
                            //Add a point to the circle
                            if(gameGrid[lineIndex][columnIndex] == O)
                                chancePerPosition[0][columnIndex] += 1;
    
                            //Add a point to the cross
                            else if(gameGrid[lineIndex][columnIndex] == X)
                                chancePerPosition[1][columnIndex] += 1;
                        }
                    }
                }
            checkPercentage(chancePerPosition, result);

            //If some one has winned we just return the result, don't need to check the rest
            if(result[0] == 100 || result[1] == 100)
                return result;

            lastPlayer = -1;
            int middleIndex = (int) gameSize/2;
            //Check Primary Diagonal
            for(int lineIndex = 0; lineIndex < gameGrid.length; lineIndex++){
                
                if(lastPlayer == -1 && gameGrid[lineIndex][lineIndex] != 0)
                    lastPlayer = gameGrid[lineIndex][lineIndex];

                if(lastPlayer != gameGrid[lineIndex][lineIndex] && gameGrid[lineIndex][lineIndex] != 0){
                    chancePerPosition[0][0] = -1;
                    break;
                }

                if(lastPlayer != gameGrid[middleIndex][middleIndex] && gameGrid[middleIndex][middleIndex] != 0){
                    chancePerPosition[0][0] = -1;
                    break;
                }

                //Add a point to the circle
                if(gameGrid[lineIndex][lineIndex] == O)
                    chancePerPosition[0][lineIndex] += 1;

                //Add a point to the cross
                else if(gameGrid[lineIndex][lineIndex] == X)
                    chancePerPosition[1][lineIndex] += 1;
            }

            checkPercentageDiagonal(chancePerPosition, result);

            //If some one has winned we just return the result, don't need to check the rest
            if(result[0] == 100 || result[1] == 100)
                return result;

            //Check Secondary Diagonal
            for(int line = gameGrid.length - 1; line >= 0; line--){

                if(lastPlayer == -1 && gameGrid[line][gameGrid.length - 1 - line] != 0)
                lastPlayer = gameGrid[line][gameGrid.length - 1 - line];

                if(lastPlayer != gameGrid[line][gameGrid.length - 1 - line] && gameGrid[line][gameGrid.length - 1 - line] != 0){
                    chancePerPosition[0][0] = -1;
                    break;
                }

                if(lastPlayer != gameGrid[middleIndex][middleIndex] && gameGrid[middleIndex][middleIndex] != 0){
                    chancePerPosition[0][0] = -1;
                    break;
                }

                //Add a point to the circle
                if(gameGrid[line][gameGrid.length - 1 - line] == O)
                    chancePerPosition[0][gameGrid.length - 1 - line] += 1;

                //Add a point to the cross
                else if(gameGrid[line][gameGrid.length - 1 - line] == X)
                    chancePerPosition[1][gameGrid.length - 1 - line] += 1;
            }
            checkPercentageDiagonal(chancePerPosition, result);
            
            //If some one has winned we just return the result, don't need to check the rest
            if(result[0] == 100 || result[1] == 100)
                return result;
                
            for(int resultIndex = 0; resultIndex < result.length; resultIndex++)
                //Since we do 4 checks we have to divide the result by 4 to have the avarege
                result[resultIndex] = result[resultIndex]/4;
            return result;
        }
    }

    private void printWinnerGrid(byte winner){
        for(byte[] byteArray: gameGrid){
            System.out.println("");
            for(byte currentByte: byteArray){
                if(currentByte == X)
                    if(currentByte == winner)
                        System.out.print(Const.WINNER_COLLOR + "X ");
                    else
                        System.out.print(Const.LOSER_COLLOR + "X ");
                else if(currentByte == O)
                    if(currentByte == winner)
                        System.out.print(Const.WINNER_COLLOR + "O ");
                    else
                        System.out.print(Const.LOSER_COLLOR + "O ");
                else
                    System.out.print(Const.NORMAL_COLLOR + "- ");
            }
        }
        System.out.println(Const.NORMAL_COLLOR);
    }

    public void printGrid(){
        for(byte[] byteArray: gameGrid){
            System.out.println("");
            for(byte currentByte: byteArray){
                switch(currentByte){
                    case X:
                        System.out.print("X ");
                        continue;
                    case O:
                        System.out.print("O ");
                        continue;
                    default:
                        System.out.print("- ");
                        continue;
                }
            }
        }
        System.out.println("");
    }

    private boolean circlePlay(Scanner sc) throws NumberFormatException, NoSuchVertexException{
        System.out.print("Please, input your desired location to mark (L,C or L C): ");
        System.out.println("");
        String[] line = sc.nextLine().split("(\\,|\\s)");
        return markCircle(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
        /*int[] line = MiniMaxAlphaBeta.getBestMove(TicTacToe.this);
        System.out.println(line[0] + " " + line[1]);
        return markCircle(line[0], line[1]);*/
    }

    private boolean crossPlay(Scanner sc) throws NumberFormatException, NoSuchVertexException, AdjacencyAlreadyExistsException, VertexAlreadyExistsException {
        TicTacToe temp = new TicTacToe(gameGrid.length);
        temp.changeGrid(getGrid());
        int[] line = MiniMaxAlphaBeta.getBestCoord(temp);
        return markCross(line[0]+1, line[1]+1);
        /*
        printGrid();
        System.out.print("Please, input your desired location to mark (L,C or L C): ");
        String[] line = sc.nextLine().split("(\\,|\\s)");
        System.out.println("");
        return markCross(Integer.parseInt(line[0]), Integer.parseInt(line[1]));*/
    }

    public byte[][] getGrid(){
        byte[][] tempGrid = new byte[gameGrid.length][gameGrid.length];
        for (int i = 0; i < tempGrid.length; i++) {
            for (int j = 0; j < tempGrid[i].length; j++) {
                tempGrid[i][j] = gameGrid[i][j];
            }
        }
        return tempGrid;
    }

    public void startInterface() throws NumberFormatException, NoSuchVertexException, AdjacencyAlreadyExistsException, VertexAlreadyExistsException {
        Scanner sc = new Scanner(System.in);
        GameState state  = GameState.START;
        byte lastPlayer = -1;
        int checkWinnerResult[] = new int[]{-2, -2};
        byte winner = -1;
        byte playerToStart = X;
        boolean closeGame = false;
        while(!closeGame){
            switch(state){
                case START:
                    if(marks > 0)
                        clearGame();
                    System.out.println("Starting the game...");
                    if(playerToStart == X)
                        lastPlayer = O;
                    else
                        lastPlayer = X;
                    state = GameState.WAIT;
                    break;
                case END:
                    if(winner != -1)
                        printWinnerGrid(winner);
                    System.out.print("Game ended, want to play again (Y/N)? ");
                    if(sc.nextLine().equalsIgnoreCase("Y"))
                        state = GameState.START;
                    else
                        state = GameState.CLOSE;
                    break;
                case WAIT:
                /*
                    if((checkWinnerResult = checkWinner())[0] == 100){
                        winner = O;
                        state = GameState.END;
                        break;
                    }
                    else if(checkWinnerResult[1] == 100){
                        winner = X;
                        state = GameState.END;
                        break;
                    }
                    else if(checkWinnerResult[0] == -1 && checkWinnerResult[1] == -1){
                        printGrid();
                        state = GameState.END;
                        System.out.println(Const.TIE_COLLOR + Const.TIE);
                        break;
                    }
                    */
                    if(false){
                        System.out.println("hur dur");
                    }
                    else{
                        printGrid();
                        if(lastPlayer == X)
                            state = GameState.CIRCLE;
                        else
                            state = GameState.CROSS;
                        break;
                    }
                case CLOSE:
                    System.out.println("Thanks for playing :D");
                    closeGame = true;
                    break;
                case CROSS:
                    if(!crossPlay(sc)){
                        System.out.println("Something went wrong, check your selection and try again");
                        break;
                    }
                    lastPlayer = X;
                    state = GameState.WAIT;
                    break;
                case CIRCLE:
                    if(!circlePlay(sc)){
                        System.out.println("Something went wrong, check your selection and try again");
                        break;
                    }
                    lastPlayer = O;
                    state = GameState.WAIT;
                    break;
                default:
                    break;
            }
        }
        System.exit(1);
    }

    //MiniMax
    public int getWidth(){
        return gameGrid.length;
    }

    public boolean isMarked(int row, int col){
        return gameGrid[row][col] != 0;
    }

    public boolean anyMovesAvailable(){
        return marks < maxMarks;
    }

    public void changeGrid(byte[][] newGrid){
        gameGrid = newGrid;
    }
}
