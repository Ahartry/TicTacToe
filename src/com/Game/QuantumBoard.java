package com.Game;

import java.util.ArrayList;

public class QuantumBoard {

    private QuantumTile[][] boardArray;
    private int result = 0;
    private ArrayList<Integer> skippedList;

    public QuantumBoard(){
        boardArray = new QuantumTile[3][3];
        skippedList = new ArrayList<>();
        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){
                boardArray[i][j] = new QuantumTile();
                boardArray[i][j].setState(State.Blank);
            }
        }

        state = State.Blank;
    }

    public int checkLoops(int turn){

        int start = getBoardWithMove(turn);
        int next = getLink(turn, start);

        result = 0;

        //System.out.println("\nStarting location: Square " + start);

        skippedList.add(turn);
        iterativeSearch(start, next);

        skippedList.clear();

        return result;
    }

    public void iterativeSearch(int start, int loc){


        int x = loc % 3;
        int y = (int) Math.floor(loc / 3);

        if(loc == start){
            result = 1;
            System.out.println("Loop found");
        }

        if(result == 1){
            return;
        }

        mainLoop:
        for(int i = 0; i < boardArray[x][y].getMovesList().size(); i++){

            int move = boardArray[x][y].getMovesList().get(i);
            int link = getLink(move, (x + (y * 3)));

            //checks for skip list
            for(int j = 0; j < skippedList.size(); j++){
                if(skippedList.get(j) == boardArray[x][y].getMovesList().get(i)){
                    //System.out.println("Skipping move " + skippedList.get(j));
                    continue mainLoop;
                }
            }

            //does the stuff otherwise
            skippedList.add(move);
            iterativeSearch(start, link);
        }

    }

    public int getLink(int turn, int start){
        int boardResult = -1;

        mainLoop:
        for(int i = 0; i < 9; i++){

            int x = i % 3;
            int y = (int) Math.floor(i / 3);
            if(i != start){
                for(int j = 0; j < boardArray[x][y].getMovesList().size(); j++){
                    if(boardArray[x][y].getMovesList().get(j) == turn){
                        boardResult = i;
                        break mainLoop;
                    }
                }
            }

        }

        return boardResult;
    }

    public int getBoardWithMove(int turn){

        int boardResult = 0;
        mainLoop:
        for(int i = 0; i < 9; i++){

            int x = i % 3;
            int y = (int) Math.floor(i / 3);
            for(int j = 0; j < boardArray[x][y].getMovesList().size(); j++){
                if(boardArray[x][y].getMovesList().get(j) == turn){
                    boardResult = i;
                    break mainLoop;
                }
            }
        }

        return boardResult;
    }

    public int getOtherBoardWithMove(int turn){

        int boardResult = 0;
        boolean first = false;
        mainLoop:
        for(int i = 0; i < 9; i++){

            int x = i % 3;
            int y = (int) Math.floor(i / 3);
            for(int j = 0; j < boardArray[x][y].getMovesList().size(); j++){
                if(first && boardArray[x][y].getMovesList().get(j) == turn){
                    boardResult = i;
                    break mainLoop;
                }
                if(boardArray[x][y].getMovesList().get(j) == turn){
                    first = true;
                }
            }
        }

        return boardResult;
    }

    private State state;
    
    public void setState(State input){
        state = input;
    }

    public State getState(){
        return state;
    }

    public void setBoardTile(int x, int y, boolean player){
        if(player){
            boardArray[x][y].setState(State.Player2);
        }else{
            boardArray[x][y].setState(State.Player1);
        }
        
    }

    public QuantumTile getBoardTile(int x, int y){
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

    public int checkEntireBoard(){
        int result = 0;
        int player1 = 0; 
        int player2 = 0;
        int highestp1 = 0;
        int highestp2 = 0;

        //verticals
        for(int i = 0; i < 3; i++){
            if(boardArray[i][0].getState().equals(boardArray[i][1].getState()) && (boardArray[i][0].getState().equals(boardArray[i][2].getState()))){
                if(boardArray[i][0].getState().equals(State.Player1)){
                    player1++;
                    if(getHighestMove(i, i + 3, i + 6) > highestp1){
                        highestp1 = getHighestMove(i, i + 3, i + 6);
                    }

                }else if(boardArray[i][0].getState().equals(State.Player2)){
                    player2++;
                    if(getHighestMove(i, i + 3, i + 6) > highestp2){
                        highestp2 = getHighestMove(i, i + 3, i + 6);
                    }
                }
            }
        }

        //horizontals
        for(int i = 0; i < 3; i++){
            if(boardArray[0][i].getState().equals(boardArray[1][i].getState()) && (boardArray[0][i].getState().equals(boardArray[2][i].getState()))){
                if(boardArray[0][i].getState().equals(State.Player1)){
                    player1++;
                    if(getHighestMove((i * 3), (i * 3) + 1, (i * 3) + 2) > highestp1){
                        highestp1 = getHighestMove((i * 3), (i * 3) + 1, (i * 3) + 2);
                    }

                }else if(boardArray[0][i].getState().equals(State.Player2)){
                    player2++;
                    if(getHighestMove((i * 3), (i * 3) + 1, (i * 3) + 2) > highestp2){
                        highestp2 = getHighestMove(i * 3, (i * 3) + 1, (i * 3) + 2);
                    }

                }
            }
        }

        //diagonals
        if(boardArray[0][0].getState().equals(boardArray[1][1].getState()) && boardArray[0][0].getState().equals(boardArray[2][2].getState())){
            if(boardArray[1][1].getState().equals(State.Player1)){
                player1++;
                if(getHighestMove(0, 4, 8) > highestp1){
                    highestp1 = getHighestMove(0, 4, 8);
                }  
            }else if(boardArray[1][1].getState().equals(State.Player2)){
                player2++;
                if(getHighestMove(0, 4, 8) > highestp2){
                    highestp2 = getHighestMove(0, 4, 8);
                }  
            }
        }
        if(boardArray[2][0].getState().equals(boardArray[1][1].getState()) && boardArray[2][0].getState().equals(boardArray[0][2].getState())){
            if(boardArray[1][1].getState().equals(State.Player1)){
                player1++;
                if(getHighestMove(2, 4, 6) > highestp1){
                    highestp1 = getHighestMove(2, 4, 6);
                }  
            }else if(boardArray[1][1].getState().equals(State.Player2)){
                player2++;
                if(getHighestMove(2, 4, 6) > highestp2){
                    highestp2 = getHighestMove(2, 4, 6);
                }  
            }
        }
        
        //silly stuff with turn count and checking stuff
        if(player1 == player2 && player1 != 0){
            if(highestp1 < highestp2){
                result = 1;
            }else{
                result = 2;
            }
        }else if(player1 > player2){
            result = 1;
        }else if(player2 > player1){
            result = 2;
        }
        return result;
    }

    public int getHighestMove(int first, int second, int third){
        int move1 = boardArray[first % 3][(int) Math.floor(first / 3)].getTurn();
        int move2 = boardArray[second % 3][(int) Math.floor(second / 3)].getTurn();
        int move3 = boardArray[third % 3][(int) Math.floor(third / 3)].getTurn();

        return Math.max(move1, Math.max(move2, move3));
    }

    public void printBoard(){
        System.out.println();

        for(int i = 0; i < 9; i++){
            int x = i % 3;
            int y = (int) Math.floor(i /3);

            for(int j = 0; j < boardArray[x][y].getMovesList().size(); j++){
                System.out.print(boardArray[x][y].getMovesList().get(j));
            }
            if(boardArray[x][y].getMovesList().size() < 3){
                for(int j = 0; j < (3 - boardArray[x][y].getMovesList().size()); j++){
                    System.out.print("0");
                }
            }
            System.out.print(" ");
        }
    }

    public void printTile(int x, int y){
        System.out.println("\nMoves at " + x + ", " + y);
        for(int i = 0; i < boardArray[x][y].getMovesList().size(); i++){
            System.out.print(boardArray[x][y].getMovesList().get(i) + " ");
        }
    }

    public int getMoveLocationInArray(int tile, int turn){
        int result = 0;

        for(int i = 0; i < boardArray[tile % 3][(int) Math.floor(tile / 3)].getMovesList().size(); i++){
            if(boardArray[tile % 3][(int) Math.floor(tile / 3)].getMovesList().get(i) == turn){
                result = i;
            }
        }

        return result;
    }

    public void collapseTile(int loc, int turn){

        int x = loc % 3;
        int y = (int) Math.floor(loc / 3);

        if(turn % 2 != 0){
            boardArray[x][y].setState(State.Player1);
        }else{
            boardArray[x][y].setState(State.Player2);
        }

        boardArray[x][y].setTurn(turn);

        boardArray[x][y].getMovesList().clear();

        checkBrokenLinks();

    }

    public void checkBrokenLinks(){
        int linksResolved = 0;

        for(int i = 0; i < 3; i++){
            for(int j = 0; j < 3; j++){

                //iterates through moves on each square
                for(int k = 0; k < boardArray[i][j].getMovesList().size(); k++){

                    int loc = (i + (3 * j));

                    int move = boardArray[i][j].getMovesList().get(k);

                    int link = getLink(move, loc);
                    
                    if(link == -1){
                        //System.out.println("No link for tile " + loc + ", move " + move);
                        collapseTile(loc, move);
                        linksResolved++;
                    }
                }
            }
        }
        if(linksResolved > 0){
            checkBrokenLinks();
        }
    }
    
}


