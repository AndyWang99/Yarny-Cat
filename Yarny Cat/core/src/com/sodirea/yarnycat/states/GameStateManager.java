package com.sodirea.yarnycat.states;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Stack;

public class GameStateManager {
    private Stack<State> states;
    private boolean outOfApp; //gsm should know whether or not they are not in the app anymore (i.e. pause() has been called)


    public GameStateManager() {
        states = new Stack<State>();
    }

    public void push(State state) {
        states.push(state);
    }

    public void pop() {
        states.pop().dispose();
    }

    public void set(State state) {
        states.pop().dispose();
        states.push(state);
    }

    public State peek() {
        return states.peek();
    }

    public void update(float dt, boolean outOfApp) {
        this.outOfApp = outOfApp; // constantly updating the outOfApp variable from the main class
        states.peek().update(dt);
    }

    public void render(SpriteBatch sb) {
        states.peek().render(sb);
    }

    public boolean isOutOfApp() {
        return outOfApp;
    }
}
