package game.objects;

import fastj.engine.FastJEngine;
import fastj.engine.graphics.drawable.Drawable;
import fastj.engine.graphics.drawable.single.Polygon2D;
import fastj.engine.graphics.nondrawable.Pointf;
import fastj.engine.io.Mouse;
import fastj.engine.io.listeners.MouseActionListener;
import fastj.engine.systems.game.Scene;

import java.awt.*;
import java.awt.event.MouseEvent;


/**
 * A GUI button that switches from the win scene to the game scene.
 * <p>
 * Pong, made with the FastJ Game Engine.
 *
 * @author Andrew Dey
 */
public class PlayAgainButton extends Polygon2D implements MouseActionListener {

    Scene origin;

    public PlayAgainButton(Pointf[] points, Color color, boolean b, boolean b1, Scene o) {
        super(points, color, b, b1);
        origin = o;
    }

    public Drawable addAsMouseInputListener(Scene scene) {
        scene.addMouseInputListener(this);
        return this;
    }

    @Override
    public void destroy(Scene scene) {
        scene.removeMouseInputListener(this);
        super.destroy(scene);
    }

    @Override
    public void onMousePressed(MouseEvent mouseEvent) {
        if (Mouse.interactsWith(this, Mouse.MouseAction.PRESS) && mouseEvent.getButton() == MouseEvent.BUTTON1) {
            origin.unload(FastJEngine.getDisplay());
            FastJEngine.getLogicManager().switchScenes("Game");
        }
    }
}