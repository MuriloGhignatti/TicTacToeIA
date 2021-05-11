package gsn;

public class Const {

    public static final String NORMAL_COLLOR = "\u001B[0m";         //ANSI_RESET
    public static final String WINNER_MARK_COLLOR = "\u001B[36m";   //ANSI_CYAN
    public static final String WINNER_COLLOR = "\u001B[32m";        //ANSI_GREEN
    public static final String LOSER_COLLOR = "\u001B[31m";         //ANSI_RED
    public static final String TIE_COLLOR = "\u001B[33m";           //ANSI_YELLOW

    public static final String CROSS_WON = WINNER_MARK_COLLOR + "X " + NORMAL_COLLOR + WINNER_COLLOR + "Won!" + NORMAL_COLLOR;
    public static final String CIRCLE_WON = WINNER_MARK_COLLOR + "O " + NORMAL_COLLOR + WINNER_COLLOR + "Won!" + NORMAL_COLLOR;

    public static final String TIE = TIE_COLLOR + "Game Ended With A Tie, All Spaces Were Marked!" + NORMAL_COLLOR;
}
