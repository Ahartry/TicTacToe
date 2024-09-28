package com.Game;

public class QuantumMove {

    int move1;
    int move2;

    int wins = 0;
    int total = 0;

    double winsd = 0;

    int turn = 0;

    public QuantumMove(int one, int two){
        move1 = one;
        move2 = two;
    }

    //this does a completely different thing (collapse options)
    //the number input actually does nothing, but leaving it I guess
    public QuantumMove(int loc){

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

    public void addWin(double x){
        winsd += x;
    }

    public double getWinsd(){
        return winsd;
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

    public int getTurn(){
        return turn;
    }

    public void setTurn(int x){
        turn = x;
    }

}
