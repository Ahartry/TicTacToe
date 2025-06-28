package com.Game;

import java.util.ArrayList;

public class Board {

    boolean quantum;
    int scale;
    ArrayList<int[]> boardArrays;
    //qmlist is move list, stores locations. qtlist is turn list, stores turn numbers
    int[][] qmlist;
    int[][] qtlist;
    int[] qcount;

    //# of possible quantum moves is (n)(n-1)/2, where n is the number of tiles

    //scale is exponent (3^scale tiles)
    //2 is reg, 3 is 3d, 4 is ultimate, 6 is omega
    public Board(int scale, boolean q){

        //initializes the quantum move list
        if(q){
            qmlist = new int[3^scale][3^scale];
            qtlist = new int[3^scale][3^scale];
            qcount = new int[3^scale]; 
        }
        
        quantum = q;
        this.scale = scale;

        //creates board with all tiles. meta boards are created based on exponents
        int s_count = scale;
        boardArrays = new ArrayList<>();
        boardArrays.add(new int[3^scale]);
        while(s_count > 3){
            s_count -= 2;
            boardArrays.add(new int[3^s_count]);
        }
    }

    public void move(Move move, int turn){
        //adds the quantum moves to the next open slot in the quantum move array, then increments indexes
        if(quantum){
            qmlist[move.loc][qcount[move.loc]] = move.loc2;
            qmlist[move.loc2][qcount[move.loc2]] = move.loc;
            qtlist[move.loc][qcount[move.loc]] = turn;
            qtlist[move.loc2][qcount[move.loc2]] = turn;

            qcount[move.loc]++;
            qcount[move.loc2]++;

        }else{
            boardArrays.get(0)[move.loc] = turn;
        }
    }

    //variant without turn, I don't know yet how I am doing it
    public void move(Move move){
        if(quantum){
            qmlist[move.loc][qcount[move.loc]] = move.loc2;
            qmlist[move.loc2][qcount[move.loc2]] = move.loc;
            qtlist[move.loc][qcount[move.loc]] = move.turn;
            qtlist[move.loc2][qcount[move.loc2]] = move.turn;

            qcount[move.loc]++;
            qcount[move.loc2]++;

        }else{
            boardArrays.get(0)[move.loc] = move.turn;
        }
    }
    
    public void unmove(Move move){
        if(quantum){
            //removes entries and decrements counts
            qmlist[move.loc][qcount[move.loc]] = 0;
            qmlist[move.loc2][qcount[move.loc2]] = 0;
            qtlist[move.loc][qcount[move.loc]] = 0;
            qtlist[move.loc2][qcount[move.loc2]] = 0;
            qcount[move.loc]--;
            qcount[move.loc2]--;

            QuantumSolver.uncollapseTile(this, move.loc);
        }else{
            boardArrays.get(0)[move.loc] = 0;
        }
    }

    public int[][] getQMList(){
        return qmlist;
    }

    public int[][] getQTList(){
        return qtlist;
    }

    public int[] getQCount(){
        return qcount;
    }

    public ArrayList<int[]> getBoardArrays(){
        return boardArrays;
    }

    public int getScale(){
        return scale;
    }

    // public int[] getAvailableMoves(){
    //     if(quantum){

    //     }else{

    //     }
    // }
}
