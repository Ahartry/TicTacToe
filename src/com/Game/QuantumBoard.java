package com.Game;

import java.util.ArrayList;

public class QuantumBoard extends Board{

    private int result = 0;
    private int moveCount = 1;

    //I'm trusting past self that splitting these up is useful
    private ArrayList<Move>[] quantumCacheList;
    private State[] boardState;
    private int[] boardTurn;
    private ArrayList<Move> quantumSkipList;

    @SuppressWarnings("unchecked")
    public QuantumBoard(){
        boardState = new State[9];
        boardTurn = new int[9];
        quantumSkipList = new ArrayList<>();
        quantumCacheList = new ArrayList[9];
        for(int i = 0; i < 9; i++){
            quantumCacheList[i] = new ArrayList<>();
            boardTurn[i] = 0;
            boardState[i] = State.Blank;
        }

        state = State.Blank;
    }

    public QuantumBoard(QuantumBoard3D board, int slice){
        // boardArray = new QuantumTile[3][3];
        // skippedList = new ArrayList<>();
        // quantumCacheList = new ArrayList[27];

        for(int i = 0; i < 9; i++){
            quantumCacheList[i] = new ArrayList<>(board.getQuantumCacheList()[i + (9 * slice)]);
            boardState[i] = board.getBoardStatelist()[i + (9 * slice)];
        }
    }
    
    public int checkLoops(Move move){

        int start = move.loc;
        int next = move.loc2;

        result = 0;

        quantumSkipList.add(move);
        iterativeSearch(start, next);

        quantumSkipList.clear();

        return result;
    }

    public void iterativeSearch(int start, int loc){

        if(loc == start){
            result = 1;
            //System.out.println("Loop found");
        }

        if(result == 1){
            return;
        }

        mainLoop:
        for(int i = 0; i < quantumCacheList[loc].size(); i++){

            //checks for skip list
            for(int j = 0; j < quantumSkipList.size(); j++){
                if(quantumSkipList.get(j) == quantumCacheList[loc].get(i)){
                    //System.out.println("Skipping move " + skippedList.get(j));
                    continue mainLoop;
                }
            }

            //does the stuff otherwise
            quantumSkipList.add(quantumCacheList[loc].get(i));
            if(quantumCacheList[loc].get(i).loc == loc){
                iterativeSearch(start, quantumCacheList[loc].get(i).loc2);
            }else{
                iterativeSearch(start, quantumCacheList[loc].get(i).loc);
            }
        }

    }

    public void incrementMoveCount(){
        moveCount++;
    }

    private State state;
    
    public void setState(State input){
        state = input;
    }

    public State getState(){
        return state;
    }

    public int checkEntireBoard(){
        int result = 0;
        int player1 = 0; 
        int player2 = 0;
        int highestp1 = 0;
        int highestp2 = 0;

        for(int i = 0; i < 3; i++){
            //I'm just copying this from quantumboard3d, hopefully this works
            //horizontal
            if(boardState[i * 3].equals(boardState[i * 3 + 1]) && boardState[i * 3].equals(boardState[i * 3 + 2])){
                if(boardState[i * 3].equals(State.Player1)){
                    player1++;
                    int highest = Math.max(boardTurn[i * 3],  Math.max(boardTurn[i * 3 + 1], boardTurn[i * 3 + 2]));
                    if(highest > highestp1){
                        highestp1 = highest;
                    }

                }else if(boardState[i * 3].equals(State.Player2)){
                    player2++;
                    int highest = Math.max(boardTurn[i * 3],  Math.max(boardTurn[i * 3 + 1], boardTurn[i * 3 + 2]));
                    if(highest > highestp2){
                        highestp2 = highest;
                    }
                }
            }
            //vertical
            if(boardState[i].equals(boardState[i + 3]) && boardState[i].equals(boardState[i + 6])){
                if(boardState[i].equals(State.Player1)){
                    player1++;
                    int highest = Math.max(boardTurn[i], Math.max(boardTurn[i + 3], boardTurn[i + 6]));
                    if(highest > highestp1){
                        highestp1 = highest;
                    }

                }else if(boardState[i].equals(State.Player2)){
                    player2++;
                    int highest = Math.max(boardTurn[i], Math.max(boardTurn[i + 3], boardTurn[i + 6]));
                    if(highest > highestp2){
                        highestp2 = highest;
                    }
                }
            }
        }

        //diagonals
        if(boardState[0].equals(boardState[4]) && boardState[0].equals(boardState[8])){
            if(boardState[4].equals(State.Player1)){
                player1++;
                int highest = Math.max(boardTurn[0], Math.max(boardTurn[4], boardTurn[8]));
                if(highest > highestp1){
                    highestp1 = highest;
                }
            }else if(boardState[4].equals(State.Player2)){
                player2++;
                int highest = Math.max(boardTurn[0], Math.max(boardTurn[4], boardTurn[8]));
                if(highest > highestp2){
                    highestp2 = highest;
                }
            }
        }
        if(boardState[2].equals(boardState[4]) && boardState[2].equals(boardState[6])){
            if(boardState[4].equals(State.Player1)){
                player1++;
                int highest = Math.max(boardTurn[0], Math.max(boardTurn[4], boardTurn[8]));
                if(highest > highestp1){
                    highestp1 = highest;
                }
            }else if(boardState[4].equals(State.Player2)){
                player2++;
                int highest = Math.max(boardTurn[0], Math.max(boardTurn[4], boardTurn[8]));
                if(highest > highestp2){
                    highestp2 = highest;
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

    public int getHighestMove(Move first, Move second, Move third){
        int move1 = first.turn;
        int move2 = second.turn;
        int move3 = third.turn;

        return Math.max(move1, Math.max(move2, move3));
    }

    public void collapseTile(int loc, int turn){

        if(boardState[loc] != State.Blank){
            System.out.println("Cannot collapse tile " + loc + ", already occupied");
            return;
        }

        boardTurn[loc] = turn;

        if(turn % 2 != 0){
            boardState[loc] =  State.Player1;
        }else{
            boardState[loc] =  State.Player2;
        }

        //available -= (Math.pow(2, loc));

        //does the chain reaction to collapse tiles
        for(int i = 0; i < quantumCacheList[loc].size(); i++){
            int m1 = quantumCacheList[loc].get(i).loc;
            int m2 = quantumCacheList[loc].get(i).loc2;
            int t = quantumCacheList[loc].get(i).turn;

            //I believe the blank check is to stop the chain reaction from going on forever
            if(m1 == loc){
                if(boardState[m2].equals(State.Blank)){
                    collapseTile(m2, t);
                }
            }else{
                if(boardState[m1].equals(State.Blank)){
                    collapseTile(m1, t);
                }
            }
        }

    }

    public void uncollapseTile(int loc){

        if(boardState[loc] == State.Blank){
            return;
        }

        boardTurn[loc] = 0;

        boardState[loc] = State.Blank;

        //available += (Math.pow(2, loc));

        for(int i = 0; i < quantumCacheList[loc].size(); i++){
            int m1 = quantumCacheList[loc].get(i).loc;
            int m2 = quantumCacheList[loc].get(i).loc2;

            if(m1 == loc){
                if(!boardState[m2].equals(State.Blank)){
                    uncollapseTile(m2);
                }
            }else{
                if(!boardState[m1].equals(State.Blank)){
                    uncollapseTile(m1);
                }
            }
        }

    }
    
    public int getMoveCount(){
        return moveCount;
    }

    public int getTurn(){
        return moveCount;
    }

    public void move(Move move){
        quantumCacheList[move.loc].add(move);
        quantumCacheList[move.loc2].add(move);
        moveCount++;
        //move.print();
        //incrementMoveCount();
    }

    public void unmove(Move move){
        boolean one = quantumCacheList[move.loc].remove(move);
        boolean two = quantumCacheList[move.loc2].remove(move);

        if(!(one || two)){
            System.out.println("Failed to remove a move object (move not found)");
        }
        moveCount--;

        uncollapseTile(move.loc);
    }

    public State[] getBoardStatelist(){
        return boardState;
    }

    public int[] getBoardTurnlist(){
        return boardTurn;
    }

    public ArrayList<Move> getAvailable(){
        ArrayList<Move> list = new ArrayList<>();

        for(int i = 0; i < 9; i++){
            if(boardState[i] != State.Blank){
                continue;
            }
            for(int j = i + 1; j < 9; j++){
                if(boardState[j]  != State.Blank){
                    continue;
                }

                //This should NOT be needed, but here we are
                if(i == j){
                    continue;
                }

                Move move = new Move(i, j);
                move.setTurn(getMoveCount());
                list.add(move);
            }
        }

        return list;
    }

    public ArrayList<Move>[] getQuantumCacheList(){
        return quantumCacheList;
    }

    public int[] getTurnArray(){
        return boardTurn;
    }
}


