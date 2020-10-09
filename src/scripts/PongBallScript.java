package scripts;

import fastj.engine.FastJEngine;
import fastj.engine.graphics.drawable.Drawable;
import fastj.engine.graphics.nondrawable.Point;
import fastj.engine.graphics.nondrawable.Pointf;
import fastj.engine.systems.behavior.Behavior;
import fastj.engine.systems.tags.TaggableEntity;
import fastj.engine.util.MathUtil;
import scenes.SimpleScene;

import java.awt.geom.Rectangle2D;


/**
 * Behavior script for the pong ball.
 * <p>
 * Pong, made with the FastJ Game Engine.
 *
 * @author Andrew Dey
 */
public class PongBallScript implements Behavior {

    Pointf movement;
    SimpleScene origin;

    public PongBallScript(SimpleScene s) {
        origin = s;
    }

    @Override
    public void init(Drawable obj) {
        randomizeDirection();
    }

    @Override
    public void update(Drawable obj) {
        if (!playerCollisionCheck(obj) && !wallCollisionCheck(obj)) {
            obj.moveObjPos(movement);
        }
    }

    private double negOneOrOne(double value) {
        return (value < 0) ? -1 : 1;
    }

    private boolean wallCollisionCheck(Drawable obj) {
        for (TaggableEntity te : origin.getAllWithTag("ballCollider")) {
            Drawable collider = (Drawable) te;

            if (collider.collidesWith(obj)) {
                checkDirection(movement, obj, collider);
            }
        }

        return false;
    }

    private boolean playerCollisionCheck(Drawable obj) {
        Rectangle2D.Float background = FastJEngine.getDisplay().getBackground();

        for (Pointf bound : obj.getBounds()) {
            final Pointf modifiedBound = Pointf.add(bound, movement);
            if (!modifiedBound.intersects(background)) {
                if (modifiedBound.y <= 0 || modifiedBound.y >= (background.y + background.height)) {
                    movement.y *= -1;
                }

                if (modifiedBound.x <= 0) {
                    return updateScore("opponent", obj);
                } else if (modifiedBound.x >= (background.x + background.width)) {
                    return updateScore("player", obj);
                }
            }
        }

        return false;
    }

    private void checkDirection(Pointf m, Drawable drawable, Drawable collider) {
        float topDistance = Math.abs(drawable.getCenter().y - collider.getBound(Drawable.Boundary.TOP_RIGHT).y);
        float bottomDistance = Math.abs(drawable.getCenter().y - collider.getBound(Drawable.Boundary.BOTTOM_RIGHT).y);

        if (topDistance < bottomDistance) {
            m.y = -5;
        } else {
            m.y = 5;
        }
        m.x *= -1;

        movement = m;
    }

    private void setToMiddle(Drawable drawable) {
        final Point displaySize = FastJEngine.getDisplay().getIntendedResolution();
        drawable.setObjPos((displaySize.x / 2f) - 5, (displaySize.y / 2f) - 5);
    }

    private void randomizeDirection() {
        final float rMoveX = (float) (negOneOrOne(MathUtil.random(-1, 1))) * 5;
        final float rMoveY = (float) (negOneOrOne(MathUtil.random(-1, 1))) * 5;
        movement = new Pointf(rMoveX, rMoveY);
    }

    private boolean updateScore(String victor, Drawable drawable) {
        setToMiddle(drawable);
        randomizeDirection();

        return origin.updateScore(victor);
    }
}