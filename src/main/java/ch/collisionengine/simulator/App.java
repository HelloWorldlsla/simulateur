package ch.collisionengine.simulator;

import ch.collisionengine.window.Drawable;

import javax.swing.*;

public interface App {

    void setup(JPanel toolBar);

    void reset();

    void update(double time);

    Drawable drawable();
}
