package com.Game;

import java.util.ArrayList;

public class MassiveBoard {

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
        System.out.println("Generating Massive Board");
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

    public ArrayList<MassiveMove> listActive(State turn){
        ArrayList<MassiveMove> list = new ArrayList<>();
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
                        list.add(new MassiveMove(i, j, k, turn));
                    }
                }
            }
        }

        return list;

    }
    
    public int move(MassiveMove move){
        int result = 0;
        boardArray[move.getLarge() % 3][move.getLarge() / 3].getBoardArray(move.getBoard() % 3, move.getBoard() / 3).
        getBoardArray(move.getCell() % 3, move.getCell() / 3).setState(move.getTurn());

        boardArray[move.getLarge() % 3][move.getLarge() / 3].getBoardArray(move.getCell() % 3, move.getCell() / 3).setActive(true);

        if(boardArray[move.getLarge() % 3][move.getLarge() / 3].getBoardArray(move.getBoard() % 3, move.getBoard() / 3).checkBoard(move.getCell() % 3, move.getCell() / 3) != 0){
            result = 1;
            boardArray[move.getBoard() % 3][move.getBoard() / 3].setActive(true);
            boardArray[move.getLarge() % 3][move.getLarge() / 3].getBoardArray(move.getBoard() % 3, move.getBoard() / 3).setState(move.getTurn());
            if(boardArray[move.getLarge() % 3][move.getLarge() / 3].checkBoard(move.getBoard() % 3, move.getBoard() / 3) != 0){
                result = 2;
                boardArray[move.getLarge() % 3][move.getLarge() / 3].setState(move.getTurn());
                if(checkBoard(move.getLarge() % 3, move.getLarge() / 3) == 1){
                    result = 3;
                }else if(checkBoard(move.getLarge() % 3, move.getLarge() / 3) == 2){
                    result = 4;
                }
            }
        }

        return result;
    }

    public void backTrack(ArrayList<MassiveMove> list){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).getCell() == -1){
                if(list.get(i).getBoard() == -1){
                    boardArray[list.get(i).getLarge() % 3][list.get(i).getLarge() / 3].setState(State.Blank);
                }else{
                    boardArray[list.get(i).getLarge() % 3][list.get(i).getLarge() / 3].getBoardArray(list.get(i).getBoard() % 3, list.get(i).getBoard() / 3).setState(State.Blank);
                }
            }else{
                boardArray[list.get(i).getLarge() % 3][list.get(i).getLarge() / 3].getBoardArray(list.get(i).getBoard() % 3, list.get(i).getBoard() / 3).getBoardArray(list.get(i).getCell() % 3, list.get(i).getCell() / 3).setState(State.Blank);
            }
        }

    }
}
