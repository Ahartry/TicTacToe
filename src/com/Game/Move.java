package com.Game;

public class Move {

    int turn = 0;

    //loc two is only for quantum, but I want the functions for it to be available at the highest level
    int loc = 0;
    int loc2 = -1;

    double wins = 0d;
    double total = 0d;
    
    public Move(){

    }

    public Move(int loc){
        this.loc = loc;
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
            this.turn = 2;
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
        if(loc2 == -1){
            System.out.println(loc + ", " + turn);
        }else{
            System.out.println(loc + " " + loc2 + ", " + turn);
        }
    }
    
}
