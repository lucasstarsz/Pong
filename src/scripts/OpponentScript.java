package scripts;

import fastj.engine.FastJEngine;
import fastj.engine.graphics.drawable.Drawable;
import fastj.engine.graphics.nondrawable.Pointf;
import fastj.engine.systems.behavior.Behavior;


/**
 * Behavior script for the opponent player.
 * <p>
 * Pong, made with the FastJ Game Engine.
 *
 * @author Andrew Dey
 */
public class OpponentScript implements Behavior {

    Pointf move;

    @Override
    public void init(Drawable drawable) {
        move = new Pointf(0, 5);
    }

    @Override
    public void update(Drawable drawable) {
        for (Pointf bound : drawable.getBounds()) {
            if (!bound.intersects(FastJEngine.getDisplay().getBackground())) {
                move = Pointf.toNeg(move);
                break;
            }
        }

        drawable.moveObjPos(move);
    }
}