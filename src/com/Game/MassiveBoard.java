package com.Game;

import java.util.ArrayList;

public class MassiveBoard extends Board{

    private LargeBoard[][] boardArray;
    private boolean active = false;

    public MassiveBoard (LargeBoard[][] inputBoard){

        boardArray = new LargeBoard[3][3];
        //copies the board
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                boardArray[i][j] = new LargeBoard();
                boardArray[i][j].setState(inputBoard[i][j].getState());
                boardArray[i][j].setActive(inputBoard[i][j].getActive());
            }
        }
        state = State.Blank;
    }

    public MassiveBoard (){
        boardArray = new LargeBoard[3][3];
        //System.out.println("Generating Massive Board");
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                boardArray[i][j] = new LargeBoard();
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

    public boolean getActive(){
        return active;
    }

    public LargeBoard getBoardArray(int x, int y){
        return boardArray[x][y];
    }

    public void setBoardTile(int x, int y, boolean player){
        if(player){
            boardArray[x][y].setState(State.Player2);
        }else{
            boardArray[x][y].setState(State.Player1);
        }
        
    }

    public LargeBoard getBoardTile(int x, int y){
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

        if(result != 0){
            //System.out.println("Player " + result + " victory found");
        }
        
        return result;
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

    public int checkEntireBoard(){
        int result = 0;
        int player = 0;

        for(int i = 0; i < 3; i++){
            if(boardArray[i][0].getState().equals(boardArray[i][1].getState()) && boardArray[i][0].getState().equals(boardArray[i][2].getState())){
                if(boardArray[i][0].getState().equals(State.Player1)){
                    player = 1;
                }else if(boardArray[i][0].getState().equals(State.Player2)){
                    player = 2;
                }
            }else if(boardArray[0][i].getState().equals(boardArray[1][i].getState()) && boardArray[0][i].getState().equals(boardArray[2][i].getState())){
                if(boardArray[0][i].getState().equals(State.Player1)){
                    player = 1;
                }else if(boardArray[0][i].getState().equals(State.Player2)){
                    player = 2;
                }
            }else if(((boardArray[0][0].getState().equals(boardArray[1][1].getState()) && boardArray[0][0].getState().equals(boardArray[2][2].getState()))
                 || (boardArray[2][0].getState().equals(boardArray[1][1].getState()) && boardArray[2][0].getState().equals(boardArray[0][2].getState()))) && boardArray[1][1].getState() != State.Blank){
                if(boardArray[1][1].getState().equals(State.Player1)){
                    player = 1;
                }else if(boardArray[1][1].getState().equals(State.Player2)){
                    player = 2;
                }
                
            }
        }

        result = player;

        return result;
    }

    public void calculateActive(int xl, int yl, int xb, int yb, int xc, int yc){
        //clear active
        this.setActive(false);

        //check if last move was victory
        if(boardArray[xl][yl].getBoardArray(xb, yb).checkBoard(xc, yc) != 0){
            
            if(boardArray[xb][yb].getState() == State.Blank){
                boardArray[xb][yb].setActive(true);
            }else{
                this.setActive(true);
            }

        }else if(boardArray[xl][yl].getBoardArray(xc, yc).getState() == State.Blank){
            if(boardArray[xl][yl].getState() == State.Blank){
                boardArray[xl][yl].getBoardArray(xc, yc).setActive(true);
            }else{
                this.setActive(true);
            }
        }else{
            if(boardArray[xl][yl].getState() == State.Blank){
                boardArray[xl][yl].setActive(true);
            }else{
                this.setActive(true);
            }
        }
    }

    public void copy(MassiveBoard board){
        //terribly inefficient, will fix later
        for(int i = 0; i < 9; i++){
            int xl = i % 3;
            int yl = i / 3;
            for(int j = 0; j < 9; j++){
                int xb = j % 3;
                int yb = j / 3;
                for(int k = 0; k < 9; k++){
                    int xc = k % 3;
                    int yc = k / 3;

                    State state = board.getBoardArray(xl, yl).getBoardArray(xb, yb).getBoardArray(xc, yc).getState();
                    boardArray[xl][yl].getBoardArray(xb, yb).getBoardArray(xc, yc).setState(state);
                }
                State state = board.getBoardArray(xl, yl).getBoardArray(xb, yb).getState();
                boardArray[xl][yl].getBoardArray(xb, yb).setState(state);

                boolean activeb = board.getBoardArray(xl, yl).getBoardArray(xb, yb).getActive();
                boardArray[xl][yl].getBoardArray(xb, yb).setActive(activeb);
            }
            State state = board.getBoardArray(xl, yl).getState();
            boardArray[xl][yl].setState(state);

            boolean activel = board.getBoardArray(xl, yl).getActive();
            boardArray[xl][yl].setActive(activel);
        }

        boolean activel = board.getActive();
        setActive(activel);
    }

    public ArrayList<Move> getAvailable(){
        ArrayList<Move> list = new ArrayList<>();
        for(int i = 0; i < 9; i++){
            int xl = i % 3;
            int yl = i / 3;
            // if(!boardArray[xl][yl].getActive()){
            //     continue;
            // }
            for(int j = 0; j < 9; j++){
                int xb = j % 3;
                int yb = j / 3;
                if(!boardArray[xl][yl].getBoardArray(xb, yb).getActive()){
                    continue;
                }
                for(int k = 0; k < 9; k++){
                    int xc = k % 3;
                    int yc = k / 3;
                    if(boardArray[xl][yl].getBoardArray(xb, yb).getBoardArray(xc, yc).getState() == State.Blank){
                        list.add(new Move(i, j, k));
                        // System.out.println("Tile " + i + " " + j + " " + k); seems to be working fine
                    }
                }
            }
        }
        //System.out.println("Board has " + list.size() + " move options");
        return list;
    }
    
    public void move(Move move){
        int t3 = move.loc / 100;
        int t2 = (move.loc - (t3 * 100)) / 10;
        int t1 = (move.loc - (t3 * 100) - (t2 * 10));

        int turn = move.turn;
        State s = State.Player1;
        if(turn % 2 == 0){
            s = State.Player2;
        }

        boardArray[t3 % 3][t3 / 3].getBoardArray(t2 % 3, t2 / 3).
        getBoardArray(t1 % 3, t1 / 3).setState(s);

        boardArray[t3 % 3][t3 / 3].getBoardArray(t1 % 3, t1 / 3).setActive(true);

        if(boardArray[t3 % 3][t3 / 3].getBoardArray(t2 % 3, t2 / 3).checkBoard(t1 % 3, t1 / 3) != 0){
            boardArray[t2 % 3][t2 / 3].setActive(true);
            boardArray[t3 % 3][t3 / 3].getBoardArray(t2 % 3, t2 / 3).setState(s);
            if(boardArray[t3 % 3][t3 / 3].checkBoard(t2 % 3, t2 / 3) != 0){
                boardArray[t3 % 3][t3 / 3].setState(s);
                // if(checkBoard(t3 % 3, t3 / 3) == 1){
                // }else if(checkBoard(t3 % 3, t3 / 3) == 2){
                // }
            }
        }

        calculateActive(t3 % 3, t3 / 3, t2 % 3, t2 / 3, t1 % 3, t1 / 3);
    }

    public void unmove(Move move){
        int t3 = move.loc / 100;
        int t2 = (move.loc - (t3 * 100)) / 10;
        int t1 = (move.loc - (t3 * 100) - (t2 * 10));

        State s = State.Blank;

        if(boardArray[t3 % 3][t3 / 3].checkBoard(t2 % 3, t2 / 3) != 0){
            boardArray[t3 % 3][t3 / 3].setState(s);
        }
        if(boardArray[t3 % 3][t3 / 3].getBoardArray(t2 % 3, t2 / 3).checkBoard(t1 % 3, t1 / 3) != 0){
            boardArray[t3 % 3][t3 / 3].getBoardArray(t2 % 3, t2 / 3).setState(s);
        }

        boardArray[t3 % 3][t3 / 3].getBoardArray(t2 % 3, t2 / 3).
        getBoardArray(t1 % 3, t1 / 3).setState(s);

    }

}
