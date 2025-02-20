package com.Game;

import java.util.ArrayList;

public class Board {

    public void move(Move move){
        System.out.println("Operation not supported (move)");
    }

    public void unmove(Move move){
        System.out.println("Operation not supported (unmove)");
    }

    public ArrayList<Move> getAvailable(){
        System.out.println("Operation not supported (getAvailable)");
        return null;
    }

    public int checkEntireBoard(){
        System.out.println("Operation not supported (checkEntireBoard)");
        return 0;
    }

    public int checkLoops(Move move){
        System.out.println("Operation not supported (checkLoops)");
        return 0;
    }

    public void collapseTile(int location, int turn){
        System.out.println("Operation not supported (collapseTile)");
    }

    public void uncollapseTile(int location){
        System.out.println("Operation not supported (uncollapseTile)");
    }

    public int score(){
        System.out.println("Operation not supported (score)");
        return 0;
    }

    public int getTurn(){
        System.out.println("Operation not supported (getTurn)");
        return 0;
    }

    // public int getMoveCount(){
    //     System.out.println("Operation not supported (getMoveCount)");
    //     return 0;
    // }

    public int[] getTurnArray(){
        System.out.println("Operation not supported (getTurnArray)");
        return null;
    }

    public void print(){
        System.out.println("Operation not supported (print)");
    }
}
