package com.Game;

import java.util.ArrayList;

public class QuantumBoard3D extends Board{

    private int result = 0;
    private int moveCount = 1;

    //I'm trusting past self that splitting these up is useful
    private ArrayList<Move>[] quantumCacheList;
    private State[] boardState;
    private int[] boardTurn;
    private ArrayList<Move> quantumSkipList;

    //stores the availability of each tile as a byte
    //probably will be deprecated for compatibility
    private int available = Integer.MAX_VALUE;

    @SuppressWarnings("unchecked")
    public QuantumBoard3D(){
        boardState = new State[27];
        boardTurn = new int[27];
        quantumSkipList = new ArrayList<>();
        quantumCacheList = new ArrayList[27];
        for(int i = 0; i < 27; i++){
            quantumCacheList[i] = new ArrayList<>();
            boardTurn[i] = 0;
            boardState[i] = State.Blank;
        }

        state = State.Blank;
    }

    //because naming has not been fixed yet

    public int checkLoops(Move move){
        result = 0;

        quantumSkipList.clear();
        quantumSkipList.add(move);

        int start = move.loc;
        int next = move.loc2;

        iterativeSearch(start, next);

        quantumSkipList.clear();

        return result;
    }

    public void iterativeSearch(int start, int next){
        if(next == start){
            result = 1;
        }
        if(result == 1){
            return;
        }
        //System.out.println("Tile cache " + next + " has " + quantumCacheList[next].size() + " moves");
        mainloop:
        for(int i = 0; i < quantumCacheList[next].size(); i++){
            for(int j = 0; j < quantumSkipList.size(); j++){
                if(quantumSkipList.get(j) == quantumCacheList[next].get(i)){
                    continue mainloop;
                }
            }
            quantumSkipList.add(quantumCacheList[next].get(i));
            if(quantumCacheList[next].get(i).loc == next){
                iterativeSearch(start, quantumCacheList[next].get(i).loc2);
            }else{
                iterativeSearch(start, quantumCacheList[next].get(i).loc);
            }
        }

        return;
    }

    private State state;
    
    public void setState(State input){
        state = input;
    }

    public State getState(){
        return state;
    }

    public void setBoardTurn(int x, int turn){
        boardTurn[x] = turn;
    }

    public State[] getBoardStatelist(){
        return boardState;
    }

    public int[] getBoardTurnlist(){
        return boardTurn;
    }


    public int checkEntireBoard(){
        int result = 0;
        int player1 = 0; 
        int player2 = 0;
        int highestp1 = 0;
        int highestp2 = 0;

        for(int i = 0; i < 9; i++){
            //horizontal x
            if(boardState[i * 3].equals(boardState[i * 3 + 1]) && boardState[i * 3].equals(boardState[i * 3 + 2])){
                if(boardState[i * 3].equals(State.Player1)){
                    //System.out.println("Line 107");
                    player1++;
                    int highest = Math.max(boardTurn[i * 3],  Math.max(boardTurn[i * 3 + 1], boardTurn[i * 3 + 2]));
                    if(highest > highestp1){
                        highestp1 = highest;
                    }

                }else if(boardState[i * 3].equals(State.Player2)){
                    //System.out.println("Line 115");
                    player2++;
                    int highest = Math.max(boardTurn[i * 3],  Math.max(boardTurn[i * 3 + 1], boardTurn[i * 3 + 2]));
                    if(highest > highestp2){
                        highestp2 = highest;
                    }
                }
            }
        }

        for(int i = 0; i < 3; i++){
            //horizontal y
            if(boardState[i].equals(boardState[i + 3]) && boardState[i].equals(boardState[i + 6])){
                if(boardState[i].equals(State.Player1)){
                    //System.out.println("Line 129");
                    player1++;
                    int highest = Math.max(boardTurn[i],  Math.max(boardTurn[i + 3], boardTurn[i + 6]));
                    if(highest > highestp1){
                        highestp1 = highest;
                    }

                }else if(boardState[i].equals(State.Player2)){
                    //System.out.println("Line 137");
                    player2++;
                    int highest = Math.max(boardTurn[i],  Math.max(boardTurn[i + 3], boardTurn[i + 6]));
                    if(highest > highestp2){
                        highestp2 = highest;
                    }
                }
            }
            if(boardState[i + 9].equals(boardState[i + 12]) && boardState[i + 9].equals(boardState[i + 15])){
                if(boardState[i + 9].equals(State.Player1)){
                    //System.out.println("Line 147");
                    player1++;
                    int highest = Math.max(boardTurn[i + 9],  Math.max(boardTurn[i + 12], boardTurn[i + 15]));
                    if(highest > highestp1){
                        highestp1 = highest;
                    }

                }else if(boardState[i + 9].equals(State.Player2)){
                    //System.out.println("Line 155");
                    player2++;
                    int highest = Math.max(boardTurn[i + 9],  Math.max(boardTurn[i + 12], boardTurn[i + 15]));
                    if(highest > highestp2){
                        highestp2 = highest;
                    }
                }
            }
            if(boardState[i + 18].equals(boardState[i + 21]) && boardState[i + 18].equals(boardState[i + 24])){
                if(boardState[i + 18].equals(State.Player1)){
                    //System.out.println("Line 165");
                    player1++;
                    int highest = Math.max(boardTurn[i + 18],  Math.max(boardTurn[i + 21], boardTurn[i + 24]));
                    if(highest > highestp1){
                        highestp1 = highest;
                    }

                }else if(boardState[i + 18].equals(State.Player2)){
                    //System.out.println("Line 173");
                    player2++;
                    int highest = Math.max(boardTurn[i + 18],  Math.max(boardTurn[i + 21], boardTurn[i + 24]));
                    if(highest > highestp2){
                        highestp2 = highest;
                    }
                }
            }
        }

        for(int i = 0; i < 9; i++){
            //horizontal z
            if(boardState[i].equals(boardState[i + 9]) && boardState[i].equals(boardState[i + 18])){
                if(boardState[i].equals(State.Player1)){
                    //System.out.println("Line 187");
                    player1++;
                    int highest = Math.max(boardTurn[i],  Math.max(boardTurn[i + 9], boardTurn[i + 18]));
                    if(highest > highestp2){
                        highestp2 = highest;
                    }

                }else if(boardState[i].equals(State.Player2)){
                    //System.out.println("Line 195");
                    player2++;
                    int highest = Math.max(boardTurn[i],  Math.max(boardTurn[i + 9], boardTurn[i + 18]));
                    if(highest > highestp2){
                        highestp2 = highest;
                    }
                }
            }
        }

        //diagonals
        for(int i = 0; i < 3; i++){
            //x ascending
            if(boardState[i * 3].equals(boardState[(i * 3) + 10]) && boardState[i * 3].equals(boardState[(i * 3) + 20])){
                if(boardState[i * 3].equals(State.Player1)){
                    //System.out.println("Line 210");
                    player1++;
                    int highest = Math.max(boardTurn[i * 3],  Math.max(boardTurn[(i * 3) + 10], boardTurn[(i * 3) + 20]));
                    if(highest > highestp1){
                        highestp1 = highest;
                    }

                }else if(boardState[i * 3].equals(State.Player2)){
                    //System.out.println("Line 218");
                    player2++;
                    int highest = Math.max(boardTurn[i * 3],  Math.max(boardTurn[(i * 3) + 10], boardTurn[(i * 3) + 20]));
                    if(highest > highestp2){
                        highestp2 = highest;
                    }
                }
            }

            //x descending
            if(boardState[(i * 3) + 2].equals(boardState[(i * 3) + 10]) && boardState[(i * 3) + 2].equals(boardState[(i * 3) + 18])){
                if(boardState[(i * 3) + 2].equals(State.Player1)){
                    //System.out.println("Line 230");
                    player1++;
                    int highest = Math.max(boardTurn[(i * 3) + 2],  Math.max(boardTurn[(i * 3) + 10], boardTurn[(i * 3) + 18]));
                    if(highest > highestp1){
                        highestp1 = highest;
                    }

                }else if(boardState[(i * 3) + 2].equals(State.Player2)){
                    //System.out.println("Line 238");
                    player2++;
                    int highest = Math.max(boardTurn[(i * 3) + 2],  Math.max(boardTurn[(i * 3) + 10], boardTurn[(i * 3) + 18]));
                    if(highest > highestp2){
                        highestp2 = highest;
                    }
                }
            }
            
            //z ascending
            if(boardState[(i * 9) + 6].equals(boardState[(i * 9) + 4]) && boardState[(i * 9) + 6].equals(boardState[(i * 9) + 2])){
                if(boardState[(i * 9) + 6].equals(State.Player1)){
                    //System.out.println("Line 250");
                    player1++;
                    int highest = Math.max(boardTurn[(i * 9) + 6],  Math.max(boardTurn[(i * 9) + 4], boardTurn[(i * 9) + 2]));
                    if(highest > highestp1){
                        highestp1 = highest;
                    }

                }else if(boardState[(i * 9) + 6].equals(State.Player2)){
                    //System.out.println("Line 258");
                    player2++;
                    int highest = Math.max(boardTurn[(i * 9) + 6],  Math.max(boardTurn[(i * 9) + 4], boardTurn[(i * 9) + 2]));
                    if(highest > highestp2){
                        highestp2 = highest;
                    }
                }
            }

            //z descending
            if(boardState[(i * 9)].equals(boardState[(i * 9) + 4]) && boardState[(i * 9)].equals(boardState[(i * 9) + 8])){
                if(boardState[(i * 9)].equals(State.Player1)){
                    //System.out.println("Line 270");
                    player1++;
                    int highest = Math.max(boardTurn[(i * 9)],  Math.max(boardTurn[(i * 9) + 4], boardTurn[(i * 9) + 8]));
                    if(highest > highestp1){
                        highestp1 = highest;
                    }

                }else if(boardState[(i * 9)].equals(State.Player2)){
                    //System.out.println("Line 278");
                    player2++;
                    int highest = Math.max(boardTurn[(i * 9)],  Math.max(boardTurn[(i * 9) + 4], boardTurn[(i * 9) + 8]));
                    if(highest > highestp2){
                        highestp2 = highest;
                    }
                }
            }

            //y ascending
            if(boardState[(i) + 6].equals(boardState[(i) + 12]) && boardState[i + 6].equals(boardState[(i) + 18])){
                if(boardState[(i) + 6].equals(State.Player1)){
                    //System.out.println("Line 290");
                    player1++;
                    int highest = Math.max(boardTurn[(i) + 6],  Math.max(boardTurn[(i) + 12], boardTurn[(i) + 18]));
                    if(highest > highestp1){
                        highestp1 = highest;
                    }

                }else if(boardState[(i) + 6].equals(State.Player2)){
                    //System.out.println("Line 298");
                    player2++;
                    int highest = Math.max(boardTurn[(i) + 6],  Math.max(boardTurn[(i) + 12], boardTurn[(i) + 18]));
                    if(highest > highestp2){
                        highestp2 = highest;
                    }
                }
            }
            
            //y descending
            if(boardState[(i)].equals(boardState[(i) + 12]) && boardState[i].equals(boardState[(i) + 24])){
                if(boardState[(i)].equals(State.Player1)){
                    //System.out.println("Line 310");
                    player1++;
                    int highest = Math.max(boardTurn[(i)],  Math.max(boardTurn[(i) + 12], boardTurn[(i) + 24]));
                    if(highest > highestp1){
                        highestp1 = highest;
                    }

                }else if(boardState[(i)].equals(State.Player2)){
                    //System.out.println("Line 318");
                    player2++;
                    int highest = Math.max(boardTurn[(i)],  Math.max(boardTurn[(i) + 12], boardTurn[(i) + 24]));
                    if(highest > highestp2){
                        highestp2 = highest;
                    }
                }
            }
        }

        //true diagonals
        if(boardState[0].equals(boardState[13]) && boardState[0].equals(boardState[26])){
            if(boardState[(13)].equals(State.Player1)){
                //System.out.println("Line 331");
                player1++;
                int highest = Math.max(boardTurn[(0)],  Math.max(boardTurn[13], boardTurn[26]));
                if(highest > highestp1){
                    highestp1 = highest;
                }

            }else if(boardState[(13)].equals(State.Player2)){
                //System.out.println("Line 339");
                player2++;
                int highest = Math.max(boardTurn[(0)],  Math.max(boardTurn[13], boardTurn[26]));
                if(highest > highestp2){
                    highestp2 = highest;
                }
            }
        }
        if(boardState[18].equals(boardState[13]) && boardState[18].equals(boardState[8])){
            if(boardState[(13)].equals(State.Player1)){
                //System.out.println("Line 349");
                player1++;
                int highest = Math.max(boardTurn[(18)],  Math.max(boardTurn[13], boardTurn[8]));
                if(highest > highestp1){
                    highestp1 = highest;
                }

            }else if(boardState[(13)].equals(State.Player2)){
                //System.out.println("Line 357");
                player2++;
                int highest = Math.max(boardTurn[(18)],  Math.max(boardTurn[13], boardTurn[8]));
                if(highest > highestp2){
                    highestp2 = highest;
                }
            }
        }
        if(boardState[6].equals(boardState[13]) && boardState[6].equals(boardState[20])){
            if(boardState[(13)].equals(State.Player1)){
                //System.out.println("Line 367");
                player1++;
                int highest = Math.max(boardTurn[(6)],  Math.max(boardTurn[13], boardTurn[20]));
                if(highest > highestp1){
                    highestp1 = highest;
                }

            }else if(boardState[(13)].equals(State.Player2)){
                //System.out.println("Line 375");
                player2++;
                int highest = Math.max(boardTurn[(6)],  Math.max(boardTurn[13], boardTurn[20]));
                if(highest > highestp2){
                    highestp2 = highest;
                }
            }
        }
        if(boardState[2].equals(boardState[13]) && boardState[2].equals(boardState[24])){
            if(boardState[2].equals(State.Player1)){
                //System.out.println("Line 385");
                player1++;
                int highest = Math.max(boardTurn[(2)],  Math.max(boardTurn[13], boardTurn[24]));
                if(highest > highestp1){
                    highestp1 = highest;
                }

            }else if(boardState[2].equals(State.Player2)){
                //System.out.println("Line 393");
                player2++;
                int highest = Math.max(boardTurn[(2)],  Math.max(boardTurn[13], boardTurn[24]));
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
            System.out.println("Cannot collape tile " + loc + ", already occupied");
            return;
        }

        boardTurn[loc] = turn;

        if(turn % 2 != 0){
            boardState[loc] =  State.Player1;
        }else{
            boardState[loc] =  State.Player2;
        }

        available -= (Math.pow(2, loc));

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

        available += (Math.pow(2, loc));

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

    public QuantumBoard getQuantumBoard(int slice){
        QuantumBoard board = new QuantumBoard(this, slice);
        return board;
    }

    public void incrementMoveCount(){
        moveCount++;
    }

    public ArrayList<Move> getAvailable(){
        ArrayList<Move> list = new ArrayList<>();

        for(int i = 0; i < 27; i++){
            if(getBoardStatelist()[i] != State.Blank){
                continue;
            }
            for(int j = i + 1; j < 27; j++){
                if(getBoardStatelist()[j] != State.Blank){
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

    public int getAvailableInt(){
        return available;
    }

    public ArrayList<Move>[] getQuantumCacheList(){
        return quantumCacheList;
    }

    public int getMoveCount(){
        return moveCount;
    }

    public int getTurn(){
        return moveCount;
    }

    public void move(Move move){
        //move.setTurn(moveCount);
        quantumCacheList[move.loc].add(move);
        quantumCacheList[move.loc2].add(move);
        moveCount++;

    }

    public void unmove(Move move){
        quantumCacheList[move.loc].remove(move);
        quantumCacheList[move.loc2].remove(move);
        moveCount--;

        uncollapseTile(move.loc);
    }

    public void print(){
        System.out.println("--------------");
        System.out.print(quantumCacheList[0].size() + " " + quantumCacheList[1].size() + " " + quantumCacheList[2].size() + "   ");
        System.out.print(quantumCacheList[9].size() + " " + quantumCacheList[10].size() + " " + quantumCacheList[11].size() + "   ");
        System.out.print(quantumCacheList[18].size() + " " + quantumCacheList[19].size() + " " + quantumCacheList[20].size() + "   ");

        System.out.println();

        System.out.print(quantumCacheList[3].size() + " " + quantumCacheList[4].size() + " " + quantumCacheList[5].size() + "   ");
        System.out.print(quantumCacheList[12].size() + " " + quantumCacheList[13].size() + " " + quantumCacheList[14].size() + "   ");
        System.out.print(quantumCacheList[21].size() + " " + quantumCacheList[22].size() + " " + quantumCacheList[23].size() + "   ");

        System.out.println();

        System.out.print(quantumCacheList[6].size() + " " + quantumCacheList[7].size() + " " + quantumCacheList[8].size() + "   ");
        System.out.print(quantumCacheList[15].size() + " " + quantumCacheList[16].size() + " " + quantumCacheList[17].size() + "   ");
        System.out.print(quantumCacheList[24].size() + " " + quantumCacheList[25].size() + " " + quantumCacheList[26].size() + "   ");

        System.out.println("\n");
    }

    public int[] getTurnArray(){
        return boardTurn;
    }

}