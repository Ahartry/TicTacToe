package com.Game;

enum State{

    Blank,
    Player1,
    Player2;

    private int value;

    static {
        Blank.value = 0;
        Player1.value = -3;
        Player2.value = 2;
    }

    public int toNum(){
        return value;
    }

}