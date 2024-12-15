package com.Game;

import java.util.ArrayList;

public class Board {

    public void move(Move move){
        System.out.println("Operation not supported");
    }

    public void unmove(Move move){
        System.out.println("Operation not supported");
    }

    public ArrayList<Move> getAvailable(){
        System.out.println("Operation not supported");
        return null;
    }

    public int checkEntireBoard(){
        System.out.println("Operation not supported");
        return 0;
    }

    public int checkLoops(Move move){
        System.out.println("Operation not supported");
        return 0;
    }

    public void collapseTile(int location, int turn){
        System.out.println("Operation not supported");
    }

    public int score(){
        System.out.println("Operation not supported");
        return 0;
    }

    public int getTurn(){
        System.out.println("Operation not supported");
        return 0;
    }

    public int getMoveCount(){
        System.out.println("Operation not supported");
        return 0;
    }
}
