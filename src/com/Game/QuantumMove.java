package com.Game;

public class QuantumMove {

    int move1;
    int move2;

    int wins = 0;
    int total = 0;

    public QuantumMove(int one, int two){
        move1 = one;
        move2 = two;
    }

    public QuantumMove(int loc){
        move1 = loc % 3;
        move2 = loc / 3;
    }

    public void setMove1(int x){
        move1 = x;
    }

    public void setMove2(int x){
        move2 = x;
    }

    public int getMove1(){
        return move1;
    }

    public int getMove2(){
        return move2;
    }

    public void addWin(int x){
        wins += x;
    }

    public void addTotal(int x){
        total += x;
    }

    public int getWins(){
        return wins;
    }

    public int getTotal(){
        return total;
    }

    public int getLoc(){
        return move1 + (move2 * 3);
    }

}
