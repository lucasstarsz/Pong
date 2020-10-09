package scenes;

import fastj.engine.FastJEngine;
import fastj.engine.graphics.drawable.single.Text2D;
import fastj.engine.graphics.nondrawable.Point;
import fastj.engine.graphics.nondrawable.Pointf;
import fastj.engine.io.Display;
import fastj.engine.io.listeners.KeyboardActionListener;
import fastj.engine.systems.game.Scene;
import fastj.engine.util.DrawUtil;
import game.objects.PlayAgainButton;

import java.awt.*;
import java.awt.event.KeyEvent;


/**
 * The game's win scene.
 * <p>
 * Pong, made with the FastJ Game Engine.
 *
 * @author Andrew Dey
 */
public class WinScene extends Scene {

    Text2D winMessage, playAgainMessage;
    PlayAgainButton playAgainBtn;

    public WinScene(String name) {
        setSceneName(name);
    }

    @Override
    public void load(Display display) {
        display.modifyBackgroundColor(Color.black);

        Font winFont = new Font("Consolas", Font.PLAIN, 24);
        Font btnFont = new Font("Consolas", Font.PLAIN, 12);
        Pointf center = Pointf.divide(Point.toPointf(display.getIntendedResolution()), 2f);

        // play again btn
        final Pointf[] playAgainBox = DrawUtil.createBox(center.x - 35, center.y - 10, 70, 20);
        playAgainBtn = (PlayAgainButton) new PlayAgainButton(playAgainBox, Color.white, false, true, this)
                .addAsMouseInputListener(this)
                .addAsGameObject(this);

        // play again message
        Pointf playAgainMsgLoc = Pointf.add(playAgainBtn.getCenter(), new Pointf(-33, 5));
        playAgainMessage = (Text2D) new Text2D("Play Again", playAgainMsgLoc, Color.white, btnFont, true)
                .addAsGameObject(this);

        // win message
        Pointf winMsgLoc = Pointf.subtract(center, new Pointf(90, 50));
        winMessage = (Text2D) new Text2D("", winMsgLoc, Color.white, winFont, true)
                .addAsGameObject(this);

        setInitialized(true);
        addKeyInputListener(new KeyboardActionListener() {
            @Override
            public void onKeyReleased(int keycode) {
                if (keycode == KeyEvent.VK_ESCAPE) {
                    unload(display);
                    FastJEngine.closeGame();
                }
            }
        });
    }

    @Override
    public void unload(Display display) {
        playAgainBtn.destroy(this);
        playAgainMessage.destroy(this);
        winMessage.destroy(this);

        setInitialized(false);
    }

    @Override
    public void update(Display display) {
    }

    public void setWinner(String str) {
        Pointf center = Pointf.divide(Point.toPointf(FastJEngine.getDisplay().getIntendedResolution()), 2f);
        switch (str) {
            case "player":
                winMessage.setText("You Win!");
                winMessage.setObjPos(Pointf.subtract(center, new Pointf(53, 50)));
                break;
            case "opponent":
                winMessage.setText("Opponent Wins.");
                winMessage.setObjPos(Pointf.subtract(center, new Pointf(87, 50)));
                break;
            default:
                throw new IllegalArgumentException(str + " isn't a player in the game.");
        }
    }
}