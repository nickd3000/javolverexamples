package com.physmo.javolverexamples.animals;

public class FoodEntity implements Entity {
    Vec2 position = new Vec2();

    @Override
    public void tick(double t) {

    }

    @Override
    public Vec2 getPosition() {
        return position;
    }
}
