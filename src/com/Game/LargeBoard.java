package com.Game;

public class LargeBoard {

    private SimpleBoard[][] boardArray;
    private boolean active = false;

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
    
}