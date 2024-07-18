package com.Game;

public class Tile{

    private State state;

    public Tile(){

        state = State.Blank;

    }

    public void setState(State input){
        state = input;
    }

    public State getState(){
        return state;
    }

    public int toNum(){
        return state.toNum();
    }
    
}


