package com.Game;

import java.util.ArrayList;

public class LargeBoard extends Board{

    private SimpleBoard[][] boardArray;
    private boolean active = false;
    private ArrayList<LargeLocation> locationList = new ArrayList<>();

    public LargeBoard(SimpleBoard[][] inputBoard){

        boardArray = new SimpleBoard[3][3];
        //copies the board
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                boardArray[i][j] = new SimpleBoard();
                boardArray[i][j].setState(inputBoard[i][j].getState());
                boardArray[i][j].setActive(inputBoard[i][j].getActive());
            }
        }
        state = State.Blank;
    }

    public LargeBoard(){
        boardArray = new SimpleBoard[3][3];
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                boardArray[i][j] = new SimpleBoard();
                boardArray[i][j].setState(State.Blank);
                boardArray[i][j].setActive(true);
            }
        }
        state = State.Blank;
    }

    private State state;
    
    public void setState(State input){
        state = input;
    }

    public State getState(){
        return state;
    }

    public void setActive(boolean x){
        active = x;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(boardArray[i][j].getState() == State.Blank){
                    boardArray[i][j].setActive(x);
                }else{
                    boardArray[i][j].setActive(false);
                }
            }
        }
    }

    public int getMoveTally(){
        int blankTally = 0;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(boardArray[i][j].getState() == State.Blank){
                    blankTally++;
                    if(boardArray[i][j].getMoveTally() == 9){
                        blankTally--;
                    }
                }
            }
        }
        return 9 - blankTally;
    }

    public boolean getActive(){
        return active;
    }

    public SimpleBoard getBoardArray(int x, int y){
        return boardArray[x][y];
    }

    public void setBoardTile(int x, int y, boolean player){
        if(player){
            boardArray[x][y].setState(State.Player2);
        }else{
            boardArray[x][y].setState(State.Player1);
        }
        
    }

    public SimpleBoard getBoardTile(int x, int y){
        return boardArray[x][y];
    }

    public int checkBoard(int x, int y){
        int result = 0;
        int player = 0; 

        if(boardArray[x][y].getState() == State.Player1){
            player = 1;
        }else if(boardArray[x][y].getState() == State.Player2){
            player = 2;
        }

        //System.out.println(player);
        if(boardArray[x][0].getState().equals(boardArray[x][1].getState()) && boardArray[x][0].getState().equals(boardArray[x][2].getState())){
            result = player;
        }else if(boardArray[0][y].getState().equals(boardArray[1][y].getState()) && boardArray[0][y].getState().equals(boardArray[2][y].getState())){
            result = player;
        }else if(((boardArray[0][0].getState().equals(boardArray[1][1].getState()) && boardArray[0][0].getState().equals(boardArray[2][2].getState()))
             || (boardArray[2][0].getState().equals(boardArray[1][1].getState()) && boardArray[2][0].getState().equals(boardArray[0][2].getState()))) && boardArray[1][1].getState() != State.Blank){
            result = player;
            
        }

        
        return result;
    }

    public int checkEntireBoard(){
        int result = 0;

        //verticals
        for(int i = 0; i < 3; i++){
            if(boardArray[i][0].getState().equals(boardArray[i][1].getState()) && (boardArray[i][0].getState().equals(boardArray[i][2].getState()))){
                if(boardArray[i][0].getState().equals(State.Player1)){
                    result = 1;
                }else if(boardArray[i][0].getState().equals(State.Player2)){
                    result = 2;
                }
            }
        }

        //horizontals
        for(int i = 0; i < 3; i++){
            if(boardArray[0][i].getState().equals(boardArray[1][i].getState()) && (boardArray[0][i].getState().equals(boardArray[2][i].getState()))){
                if(boardArray[0][i].getState().equals(State.Player1)){
                    result = 1;
                }else if(boardArray[0][i].getState().equals(State.Player2)){
                    result = 2;
                }
            }
        }

        //diagonals
        if(boardArray[0][0].getState().equals(boardArray[1][1].getState()) && boardArray[0][0].getState().equals(boardArray[2][2].getState())){
            if(boardArray[1][1].getState().equals(State.Player1)){
                result = 1;
            }else if(boardArray[1][1].getState().equals(State.Player2)){
                result = 2;
            }
        }
        if(boardArray[2][0].getState().equals(boardArray[1][1].getState()) && boardArray[2][0].getState().equals(boardArray[0][2].getState())){
            if(boardArray[1][1].getState().equals(State.Player1)){
                result = 1;
            }else if(boardArray[1][1].getState().equals(State.Player2)){
                result = 2;
            }
        }
        
        return result;
    }

    public ArrayList<Integer> listActiveBoards(){
        ArrayList<Integer> activeList = new ArrayList<>();

        for(int i = 0; i < 9; i++){
            int x1 = i % 3;
            int y1 = (int) Math.floor(i / 3);

            if(getBoardArray(x1, y1).getActive()){
                activeList.add(i);
            }
        }

        return activeList;
    }

    public ArrayList<Integer> listActiveCells(){
        ArrayList<Integer> activeList = new ArrayList<>();

        for(int i = 0; i < 9; i++){
            int x1 = i % 3;
            int y1 = i / 3;

            if(getBoardArray(x1, y1).getActive()){
                for(int j = 0; j < 9; j++){
                    int x2 = j % 3;
                    int y2 = j / 3;
                    if(getBoardArray(x1, y1).getBoardTile(x2, y2).getState() == State.Blank){
                        activeList.add((i * 10) + j);
                        //System.out.println("Adding active tile: " + ((i * 10) + j));
                    }
                }
                
            }
        }

        return activeList;
    }

    public ArrayList<Move> getAvailable(){
        ArrayList<Move> activeList = new ArrayList<>();

        for(int i = 0; i < 9; i++){
            int x1 = i % 3;
            int y1 = i / 3;

            if(getBoardArray(x1, y1).getActive()){
                for(int j = 0; j < 9; j++){
                    int x2 = j % 3;
                    int y2 = j / 3;
                    if(getBoardArray(x1, y1).getBoardTile(x2, y2).getState() == State.Blank){
                        //bad practice, but this is only getting called for player two
                        Move m = new Move((i * 10) + j);
                        m.setTurn(true);
                        activeList.add(m);
                    }
                }
                
            }
        }

        return activeList;
    }

    public void move(Move m){
        //player true is player two
        //huh wait that rhymes, good mnemonic

        int move = m.loc;

        int turn = m.turn;
        boolean player = false;
        if(turn % 2 == 0){
            player = true;
        }

        //silly preamble
        int board = move / 10;
        int boardX = board % 3;
        int boardY = board / 3;
        int cell = move - (board * 10);
        int cellX = cell % 3;
        int cellY = cell / 3;

        //System.out.println("Move " + move + ", board: " + boardX + ", " + boardY + ", cell: " + cellX + ", " + cellY + " (Cell: " + cell + ")");

        getBoardArray(boardX, boardY).setBoardTile(cellX, cellY, player);
        add(boardX, boardY, cellX, cellY, player);

        //does stuff in case of victory
        int result = getBoardArray(boardX, boardY).checkBoard(cellX, cellY);
        if(result == 1){
            getBoardArray(boardX, boardY).setState(State.Player1);
            //clearLocationList(boardX, boardY);
        }else if(result == 2){
            getBoardArray(boardX, boardY).setState(State.Player2);
            //clearLocationList(boardX, boardY);
        }
        result = checkBoard(boardX, boardY);
        if(result == 1){
            setState(State.Player1);
            //clearLocationList(boardX, boardY);
        }else if(result == 2){
            setState(State.Player2);
            //clearLocationList(boardX, boardY);
        }

        calculateActive(move);
    }

    public void unmove(Move m){

        int move = m.loc;
        //player true is player two
        //huh wait that rhymes, good mnemonic

        //silly preamble
        int board = move / 10;
        int boardX = board % 3;
        int boardY = board / 3;
        int cell = move - (board * 10);
        int cellX = cell % 3;
        int cellY =cell / 3;

        int result = checkBoard(boardX, boardY);
        if(result != 0){
            //System.out.println("test");
            setState(State.Blank);
        }
        result = getBoardArray(boardX, boardY).checkBoard(cellX, cellY);
        if(result != 0){
            //System.out.println("test");
            getBoardArray(boardX, boardY).setState(State.Blank);
        }

        //System.out.println("unMove " + move + ", board: " + boardX + ", " + boardY + ", cell: " + cellX + ", " + cellY + " (Cell: " + cell + ")");

        getBoardArray(boardX, boardY).getBoardTile(cellX, cellY).setState(State.Blank);
        //remove(boardX, boardY, cellX, cellY);
        removeLast();
    }

    public void calculateActive(int xc, int yc){
        //clear active
        if(getBoardArray(xc, yc).getState() == State.Blank){
            setActive(false);
            getBoardArray(xc, yc).setActive(true);
        }else{
            setActive(true);
        }
    }

    public void calculateActive(int move){
        int board = (int) Math.floor(move / 10);
        int cell = move - (board * 10);
        int cellX = cell % 3;
        int cellY = (int) Math.floor(cell / 3);

        if(getBoardArray(cellX, cellY).getState() == State.Blank){
            setActive(false);
            getBoardArray(cellX, cellY).setActive(true);
        }else{
            setActive(true);
        }
    }

    public void add(int x1, int y1, int x2, int y2, boolean turn){
        locationList.add(new LargeLocation(x1, y1, x2, y2, turn));
    }

    public void remove(int x1, int y1, int x2, int y2){
        for(int i = 0; i < locationList.size(); i++){
            if(locationList.get(i).getBX() == x1 && locationList.get(i).getBY() == y1 && locationList.get(i).getCX() == x2 && locationList.get(i).getCY() == y2){
                locationList.get(i);
                break;
            }
        }
    }

    public void removeLast(){
        locationList.remove(locationList.size() - 1);
    }

    public LargeLocation get(int i){
        return locationList.get(i);
    }

    public void clearLocationList(int x, int y){
        //System.out.println("Clearing list for board " + x + ", " + y);
        for(int i = 0; i < locationList.size(); i++){
            if(get(i).getBX() == x && get(i).getBY() == y){
                locationList.remove(i);
                i--;
            }
        }
    }

    public int getLocSize(){
        return locationList.size();
    }

    public int score(){
        //player two is positive
        int score = 0;

        //values the location of each move
        score += checkAllMoves();

        //checks the two in a rows
        score += checkTwos();

        return score;
    }

    public int checkAllMoves(){
        int score = 0;
        State state;

        for(int i = 0; i < 9; i++){
            state = boardArray[i % 3][i / 3].getState();
            if(state == State.Player1){
                if(i % 2 != 0){
                    score += -30;
                }else if(i == 4){
                    score += -50;
                }else{
                    score += -40;
                }
            }else if(state == State.Player2){
                if(i % 2 != 0){
                    score += 30;
                }else if(i == 4){
                    score += 50;
                }else{
                    score += 40;
                }
            }

            //does the board a size smaller
            score += boardArray[i % 3][i / 3].score();

        }

        if(getState() == State.Player1){
            score += - 10000;
        }else if(getState() == State.Player2){
            score += 10000;
        }

        return score;
    }

    public int checkTwos(){
        int score = 0;
        int lineScore = 0;
    
        // check for larger twos
        lineScore = boardArray[0][0].toNum() + boardArray[1][0].toNum() + boardArray[2][0].toNum();
        score += scoreLine(lineScore, 10);
        lineScore = boardArray[0][1].toNum() + boardArray[1][1].toNum() + boardArray[2][1].toNum();
        score += scoreLine(lineScore, 10);
        lineScore = boardArray[0][2].toNum() + boardArray[1][2].toNum() + boardArray[2][2].toNum();
        score += scoreLine(lineScore, 10);
        lineScore = boardArray[0][0].toNum() + boardArray[0][1].toNum() + boardArray[0][2].toNum();
        score += scoreLine(lineScore, 10);
        lineScore = boardArray[1][0].toNum() + boardArray[1][1].toNum() + boardArray[1][2].toNum();
        score += scoreLine(lineScore, 10);
        lineScore = boardArray[2][0].toNum() + boardArray[2][1].toNum() + boardArray[2][2].toNum();
        score += scoreLine(lineScore, 10);
        lineScore = boardArray[0][0].toNum() + boardArray[1][1].toNum() + boardArray[2][2].toNum();
        score += scoreLine(lineScore, 10);
        lineScore = boardArray[2][0].toNum() + boardArray[1][1].toNum() + boardArray[0][2].toNum();
        score += scoreLine(lineScore, 10);
    
        return score;
    }

    public int scoreLine(int line, int mult){
        int score = 0;

        if(line == -6){
            score = -5;
        }else if(line == 4){
            score = 5;
        }

        score *= mult;

        return score;
    }

    public int toNum(){
        return state.toNum();
    }

    public int getTurn(){
        //this requires some explanation. This gets called once by the AI, and so this returns that it is turn two (so player two's move). The reason this gets
        //used at all is because quantum requires turn moves
        return 2;
    }

}