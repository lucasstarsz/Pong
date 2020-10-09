package scripts;

import fastj.engine.FastJEngine;
import fastj.engine.graphics.drawable.Drawable;
import fastj.engine.graphics.nondrawable.Pointf;
import fastj.engine.io.Keyboard;
import fastj.engine.io.listeners.KeyboardActionListener;
import fastj.engine.systems.behavior.Behavior;
import fastj.engine.systems.game.Scene;

import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;


/**
 * Behavior script for the player.
 * <p>
 * Pong, made with the FastJ Game Engine.
 *
 * @author Andrew Dey
 */
public class PlayerScript implements Behavior, KeyboardActionListener {

    Pointf movement;
    Drawable object;
    Scene origin;

    public PlayerScript(Scene originScene) {
        origin = originScene;
    }

    @Override
    public void init(Drawable drawable) {
        movement = new Pointf();
        object = drawable;
        origin.addKeyInputListener(this);
    }

    @Override
    public void update(Drawable drawable) {
    }

    @Override
    public void destroy() {
        origin.removeKeyInputListener(this);

        origin = null;
        object = null;
        movement = null;
    }

    @Override
    public void onKeyDown() {
        movement.reset();

        if (object != null) {

            if (Keyboard.isKeyDown(KeyEvent.VK_W) && isOnScreen(object.getBounds(), KeyEvent.VK_W)) {
                movement.y -= 5;
            }

            if (Keyboard.isKeyDown(KeyEvent.VK_S) && isOnScreen(object.getBounds(), KeyEvent.VK_S)) {
                movement.y += 5;
            }
        }

        assert object != null;
        object.moveObjPos(movement);
    }

    private boolean isOnScreen(Pointf[] bounds, int key) {
        Rectangle2D background = FastJEngine.getDisplay().getBackground();

        for (Pointf bound : bounds) {
            if (!Pointf.add(bound, (key == KeyEvent.VK_W) ? -1 : 1).intersects(background)) {
                return false;
            }
        }

        return true;
    }
}