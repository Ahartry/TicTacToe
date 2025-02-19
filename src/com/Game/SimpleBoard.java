package com.Game;

public class SimpleBoard {

    private Tile[][] boardArray;
    private boolean active = false;

    public SimpleBoard(Tile[][] inputBoard){

        boardArray = new Tile[3][3];
        //copies the board
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                boardArray[i][j] = new Tile();
                boardArray[i][j].setState(inputBoard[i][j].getState());
            }
        }
        state = State.Blank;
    }

    public SimpleBoard(){
        boardArray = new Tile[3][3];
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                boardArray[i][j] = new Tile();
                boardArray[i][j].setState(State.Blank);
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
        
    }

    public boolean getActive(){
        return active;
    }

    public Tile getBoardArray(int x, int y){
        return boardArray[x][y];
    }

    public void setBoardTile(int x, int y, boolean player){
        if(player){
            boardArray[x][y].setState(State.Player2);
        }else{
            boardArray[x][y].setState(State.Player1);
        }
        
    }

    public int getMoveTally(){
        int blankTally = 0;
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                if(boardArray[i][j].getState() == State.Blank){
                    blankTally++;
                }
            }
        }
        return 9 - blankTally;
    }

    public Tile getBoardTile(int x, int y){
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

    public int checkFakeBoard(int x, int y, boolean movePlayer){
        int result = 0;
        int player = 0; 

        if(!movePlayer){
            boardArray[x][y].setState(State.Player1);
        }else{
            boardArray[x][y].setState(State.Player2);
        }

        if(boardArray[x][y].getState() == State.Player1){
            player = 1;
        }else if(boardArray[x][y].getState() == State.Player2){
            player = 2;
        }

        if(boardArray[x][0].getState().equals(boardArray[x][1].getState()) && boardArray[x][0].getState().equals(boardArray[x][2].getState())){
            result = player;
        }else if(boardArray[0][y].getState().equals(boardArray[1][y].getState()) && boardArray[0][y].getState().equals(boardArray[2][y].getState())){
            result = player;
        }else if(((boardArray[0][0].getState().equals(boardArray[1][1].getState()) && boardArray[0][0].getState().equals(boardArray[2][2].getState()))
             || (boardArray[2][0].getState().equals(boardArray[1][1].getState()) && boardArray[2][0].getState().equals(boardArray[0][2].getState()))) && boardArray[1][1].getState() != State.Blank){
            result = player;
            
        }

        boardArray[x][y].setState(State.Blank);

        
        return result;
    }

    public int toNum(){
        return state.toNum();
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
                    score += -3;
                }else if(i == 4){
                    score += -5;
                }else{
                    score += -4;
                }
            }else if(state == State.Player2){
                if(i % 2 != 0){
                    score += 3;
                }else if(i == 4){
                    score += 5;
                }else{
                    score += 4;
                }
            }

        }

        if(getState() == State.Player1){
            score += - 100;
        }else if(getState() == State.Player2){
            score += 100;
        }

        return score;
    }

    public int checkTwos(){
        int score = 0;
        int lineScore = 0;
    
        // check for larger twos
        lineScore = boardArray[0][0].toNum() + boardArray[1][0].toNum() + boardArray[2][0].toNum();
        score += scoreLine(lineScore, 1);
        lineScore = boardArray[0][1].toNum() + boardArray[1][1].toNum() + boardArray[2][1].toNum();
        score += scoreLine(lineScore, 1);
        lineScore = boardArray[0][2].toNum() + boardArray[1][2].toNum() + boardArray[2][2].toNum();
        score += scoreLine(lineScore, 1);
        lineScore = boardArray[0][0].toNum() + boardArray[0][1].toNum() + boardArray[0][2].toNum();
        score += scoreLine(lineScore, 1);
        lineScore = boardArray[1][0].toNum() + boardArray[1][1].toNum() + boardArray[1][2].toNum();
        score += scoreLine(lineScore, 1);
        lineScore = boardArray[2][0].toNum() + boardArray[2][1].toNum() + boardArray[2][2].toNum();
        score += scoreLine(lineScore, 1);
        lineScore = boardArray[0][0].toNum() + boardArray[1][1].toNum() + boardArray[2][2].toNum();
        score += scoreLine(lineScore, 1);
        lineScore = boardArray[2][0].toNum() + boardArray[1][1].toNum() + boardArray[0][2].toNum();
        score += scoreLine(lineScore, 1);
    
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
    
}
