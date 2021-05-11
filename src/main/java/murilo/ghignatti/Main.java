package murilo.ghignatti;

import exceptions.AdjacencyAlreadyExistsException;
import exceptions.NoSuchVertexException;
import exceptions.VertexAlreadyExistsException;

public class Main{

    public static void main(String[] args) throws VertexAlreadyExistsException, NoSuchVertexException, AdjacencyAlreadyExistsException {

        TicTacToe ticTacToe = new TicTacToe(7);

        ticTacToe.startInterface();

    }
}
