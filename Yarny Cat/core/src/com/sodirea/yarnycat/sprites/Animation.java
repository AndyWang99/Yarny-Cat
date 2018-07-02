package com.sodirea.yarnycat.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class Animation {
    private Array<Texture> textures;
    private int activeFrame;
    private float maxFrameTime;
    private float timer;

    public Animation(Array<Texture> textures, float maxFrameTime) {
        this.textures = textures;
        this.maxFrameTime = maxFrameTime;
        activeFrame = 0;
        timer = 0;
    }

    public void update(float dt) {
        timer += dt;
        if (timer > maxFrameTime) {
            if (activeFrame < textures.size-1) {
                activeFrame++;
            } else {
                activeFrame = 0;
            }
            timer = 0;
        }
    }

    public Texture getActiveTexture() {
        return textures.get(activeFrame);
    }

    public void setMaxFrameTime(float maxFrameTime) {
        this.maxFrameTime = maxFrameTime;
    }
}
