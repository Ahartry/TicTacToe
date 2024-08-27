package com.Game;

public class MassiveMove {

    int largeBoard;
    int board;
    int cell;
    State turn;

    int total;
    int wins;

    public MassiveMove(){

    }

    public MassiveMove(MassiveMove move){
        largeBoard = move.getLarge();
        board = move.getBoard();
        cell = move.getCell();
        turn = move.getTurn();
        total = move.getTotal();
        wins = move.getWins();
    }

    public MassiveMove(int move, State turn){
        largeBoard = move / 100;
        board = (move - (largeBoard * 100)) / 10;
        cell = (move - (largeBoard * 100) - (board * 10));
        this.turn = turn;
    }

    public MassiveMove(int l, int b, int c, State turn){
        largeBoard = l;
        board = b;
        cell = c;
        this.turn = turn;
    }

    public void setTurn(State turn){
        this.turn = turn;
    }

    public State getTurn(){
        return turn;
    }

    public void setMove(int move){
        largeBoard = move / 100;
        board = (move - (largeBoard * 100)) / 10;
        cell = (move - (largeBoard * 100) - (board * 10));
    }

    public void setMove(int l, int b, int c){
        largeBoard = l;
        board = b;
        cell = c;
    }

    public int getMove(){
        return (largeBoard * 100) + (board * 10) + (cell);
    }

    public int getLarge(){
        return largeBoard;
    }

    public int getBoard(){
        return board;
    }

    public int getCell(){
        return cell;
    }

    public void changeWins(int x){
        wins += x;
    }

    public int getWins(){
        return wins;
    }

    public void incrementTotal(){
        total++;
    }

    public int getTotal(){
        return total;
    }

    public void clearCell(){
        cell = -1;
    }

    public void clearBoard(){
        board = -1;
        cell = -1;
    }

    public void print(){
        System.out.println("LargeBoard " + largeBoard + ", board " + board + ", cell " + cell);
    }

}
