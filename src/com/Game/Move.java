package com.Game;

public class Move {

    int turn = 0;

    //loc two is only for quantum, but I want the functions for it to be available at the highest level
    int loc = 0;
    int loc2 = 0;
    
    public Move(){

    }

    public Move(int loc){
        this.loc = loc;
    }

    public Move(int loc, int loc2){
        this.loc = loc;
        this.loc2 = loc2;
    }

    public void setTurn(int turn){
        this.turn = turn;
    }

    public void setTurn(boolean turn){
        if(turn){
            this.turn = 0;
        }else{
            this.turn = 1;
        }
    }
}
