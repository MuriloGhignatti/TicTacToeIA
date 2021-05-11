package murilo.ghignatti;

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
        boolean result = mark(pos1 - 1, pos2 - 1, X);
        return result;
    }

    public boolean markCircle(int pos1, int pos2) throws NoSuchVertexException{
        boolean result = mark(pos1 - 1, pos2 - 1, O);
        return result;
    }

    public boolean markBlank(int pos1, int pos2) throws NoSuchVertexException{
        boolean result = mark(pos1 - 1, pos2 - 1, BLANK);
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
    public byte checkWinner(){
        if(marks == maxMarks)
            return -2;
        else{
            byte winner = -1;
            //Check Line
            //Get the line
            for(byte[] byteArray: gameGrid){
                winner = -1;
                //Naming my section so i can break only the inner loop
                element: {
                    //Running through the line
                    for(byte currentByte: byteArray){

                        //If my current position is empty {0} the rest of the line doesn't need to be checked since in this line we can't have a winner
                        if(currentByte == 0){
                            winner = -1;
                            break element;
                        }
                        //Used to start the comparation, this will be the first position of the line
                        else if(winner == -1)
                            winner = currentByte;
                        //If our current position is different from the first we don't need to check the rest of the line since we can't have a winner here
                        else if(currentByte != winner){
                            winner = -1;
                            break element;
                        }
                    }
                }
                if(winner == X)
                    return X;
                else if(winner == O)
                    return O;
            }

            //Check Column
            for(int elementColumn = 0; elementColumn < gameGrid.length; elementColumn++){
                winner = -1;
                //Naming my section so i can break only the inner loop
                element: {
                    //Running through the line
                    for(int line = 0; line < gameGrid.length; line++){

                        //If my current position is empty {0} the rest of the line doesn't need to be checked since in this line we can't have a winner
                        if(gameGrid[line][elementColumn] == 0){
                            winner = -1;
                            break element;
                        }
                        //Used to start the comparation, this will be the first position of the line
                        else if(winner == -1)
                            winner = gameGrid[line][elementColumn];
                        //If our current position is different from the first we don't need to check the rest of the line since we can't have a winner here
                        else if(gameGrid[line][elementColumn] != winner){
                            winner = -1;
                            break element;
                        }
                    }
                }
                if(winner == X)
                    return X;
                else if(winner == O)
                    return O;
            }

            int middleElementPosition = (int)(gameGrid.length / 2);
            winner = -1;

            //Check Primary Diagonal
            for(int line = 0; line < gameGrid.length; line++){
                //Naming my section so i can break only the inner loop
                if(gameGrid[middleElementPosition][middleElementPosition] == 0){
                    winner = -1;
                    break;
                }
                else if(gameGrid[line][line] != gameGrid[middleElementPosition][middleElementPosition]){
                    winner = -1;
                    break;
                }
                else if(winner == -1)
                    winner = gameGrid[line][line];
                else if(gameGrid[line][line] != winner){
                    winner = -1;
                    break;
                }
            }
                if(winner == X)
                    return X;
                else if(winner == O)
                    return O;

            winner = -1;
            //Check Secondary Diagonal
            for(int line = gameGrid.length - 1; line >= 0; line--){
                //Naming my section so i can break only the inner loop
                if(gameGrid[middleElementPosition][middleElementPosition] == 0){
                    winner = -1;
                    break;
                }
                else if(gameGrid[line][gameGrid.length - 1 - line] != gameGrid[middleElementPosition][middleElementPosition]){
                    winner = -1;
                    break;
                }
                else if(winner == -1)
                    winner = gameGrid[line][gameGrid.length - 1 - line];
                else if(gameGrid[line][gameGrid.length - 1 - line] != winner){
                    winner = -1;
                    break;
                }
            }

            if(winner == X)
                    return X;
            else if(winner == O)
                return O;
        }
        return -1;
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
        printGrid();
        System.out.print("Please, input your desired location to mark (L,C or L C): ");
        System.out.println("");
        String[] line = sc.nextLine().split("(\\,|\\s)");
        return markCircle(Integer.parseInt(line[0]), Integer.parseInt(line[1]));
        /*int[] line = MiniMaxAlphaBeta.getBestMove(TicTacToe.this);
        System.out.println(line[0] + " " + line[1]);
        return markCircle(line[0], line[1]);*/
    }

    private boolean crossPlay(Scanner sc) throws NumberFormatException, NoSuchVertexException, AdjacencyAlreadyExistsException, VertexAlreadyExistsException {
        printGrid();

        TicTacToe temp = new TicTacToe(gameGrid.length);
        temp.changeGrid(getGrid());
        int[] line = MiniMaxAlphaBeta.getBestMove(temp);
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
                    printWinnerGrid(winner);
                    System.out.print("Game ended, want to play again (Y/N)? ");
                    if(sc.nextLine().equalsIgnoreCase("Y"))
                        state = GameState.START;
                    else
                        state = GameState.CLOSE;
                    break;
                case WAIT:
                    if((winner = checkWinner()) != -1 && winner != -2)
                        state = GameState.END;
                    else if(lastPlayer == X)
                        state = GameState.CIRCLE;
                    else
                        state = GameState.CROSS;
                    /*System.out.println("Ready (Y/N)? ");
                    if(sc.nextLine().equalsIgnoreCase("Y"))
                        break;*/
                    if(true)
                        break;
                    else
                        state = GameState.CLOSE;
                    break;
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
