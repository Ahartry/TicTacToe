package com.Game;

public class Move {

    int turn = 0;

    //loc two is only for quantum, but I want the functions for it to be available at the highest level
    int loc = 0;
    int loc2 = 0;

    double wins = 0d;
    double total = 0d;
    
    public Move(){

    }

    public Move(int loc){
        this.loc = loc;
    }

    public Move(int t1, int t2, int t3){
        loc = t3 + (10 * t2) + (100 * t1);
    }

    //for quantum
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

    public void incrementWins(){
        wins++;
    }

    public void decrementWins(){
        wins--;
    }

    public void incrementTotal(){
        total++;
    }

    public void print(){
        if(loc2 == 0){
            System.out.println(loc);
        }else{
            System.out.println(loc + " " + loc2);
        }
    }
}
