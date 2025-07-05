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

    int[] active;

    //# of possible quantum moves is (n)(n-1)/2, where n is the number of tiles

    //scale is exponent (3^scale tiles)
    //2 is reg, 3 is 3d, 4 is ultimate, 6 is omega
    public Board(int scale, boolean q){

        //initializes the quantum move list
        if(q){
            qmlist = new int[(int) Math.pow(3, scale)][(int) Math.pow(3, scale)];
            qtlist = new int[(int) Math.pow(3, scale)][(int) Math.pow(3, scale)];
            qcount = new int[(int) Math.pow(3, scale)]; 
        }
        
        quantum = q;
        this.scale = scale;

        //creates board with all tiles. meta boards are created based on exponents
        //81 9 1
        int s_count = scale;
        boardArrays = new ArrayList<>();
        boardArrays.add(new int[(int) Math.pow(3, scale)]);
        while(s_count > 1){
            s_count -= 2;
            boardArrays.add(new int[(int) Math.pow(3, s_count)]);
        }

        //two numbers (scale and loc) of active board
        active = new int[2];
        active[0] = scale / 2;
        active[1] = 0;
    }

    public Board(String data){
        
    }

    //for reg
    public void move(int loc, int turn){
        boardArrays.get(0)[loc] = turn;
        
        //sets active layer (+1 will be added in regResolve)
        active[0] = 0;

        regResolveMove(loc, 0, turn);
    }

    //for quantum
    public void move(int loc, int loc2, int turn){
        qmlist[loc][qcount[loc]] = loc2;
        qmlist[loc2][qcount[loc2]] = loc;
        qtlist[loc][qcount[loc]] = turn;
        qtlist[loc2][qcount[loc2]] = turn;

        qcount[loc]++;
        qcount[loc2]++;
    }
    
    //for reg
    public void unmove(int loc){
        boardArrays.get(0)[loc] = 0;
    }
    
    //for quantum
    public void unmove(int loc, int loc2){
        qmlist[loc][qcount[loc]] = 0;
        qmlist[loc2][qcount[loc2]] = 0;
        qtlist[loc][qcount[loc]] = 0;
        qtlist[loc2][qcount[loc2]] = 0;
        qcount[loc]--;
        qcount[loc2]--;

        QuantumSolver.uncollapseTile(this, loc);
    }

    public void regResolveMove(int loc, int level, int turn){
        if(level == scale / 2){
            return;
        }

        int result = RegSolver.checkMove(this, loc, level);
        
        if(level + 1 == scale / 2){
            //idk
        }else{
            //sets active board
            active[0] = active[0] + 1;
            //not quite sure why or how this works
            active[1] = (loc / 81) * 9 + loc % 9;
        }

        escalateActive();

        if(result != 0){
            boardArrays.get(level + 1)[loc / 9] = turn;
            regResolveMove(loc / 9, level + 1, turn);
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

    public int getTileCount(){
        return boardArrays.get(0).length;
    }

    public int[] getActive(){
        return active;
    }

    public boolean isActive(int scale, int loc){
        boolean res = (scale == active[0]) && (loc == active[1]);

        if(scale == getScale() / 2 + 1){
            return false;
        }

        if(res){
            return true;
        }else{
            return isActive(scale + 1, loc / 9);
        }
    }

    public void escalateActive(){
        if(boardArrays.get(active[0])[active[1]] != 0){
            active[0] = active[0] + 1;
            active[1] = active[1] / 9;
            escalateActive();
        }
    }

}
