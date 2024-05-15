package com.Game.Files;

import java.util.ArrayList;

public class QuantumTile{

    private State state;
    private ArrayList<Integer> movesList;
    private Integer turn = 0;

    public QuantumTile(){

        state = State.Blank;
        movesList = new ArrayList<>();

    }

    public void setState(State input){
        state = input;
    }

    public ArrayList<Integer> getMovesList(){
        return movesList;
    }

    public void addMove(int x){
        movesList.add(x);
    }

    public State getState(){
        return state;
    }

    public void setTurn(int x){
        turn = x;
    }

    public int getTurn(){
        return turn;
    }
    
}


