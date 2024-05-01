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
    
}

enum State{

    Blank,
    Player1,
    Player2

}


