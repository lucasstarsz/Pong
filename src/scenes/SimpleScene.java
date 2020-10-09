package scenes;

import fastj.engine.FastJEngine;
import fastj.engine.graphics.drawable.single.Polygon2D;
import fastj.engine.graphics.drawable.single.Text2D;
import fastj.engine.graphics.nondrawable.Point;
import fastj.engine.graphics.nondrawable.Pointf;
import fastj.engine.io.Display;
import fastj.engine.io.listeners.KeyboardActionListener;
import fastj.engine.systems.game.Scene;
import fastj.engine.util.DrawUtil;
import scripts.OpponentScript;
import scripts.PlayerScript;
import scripts.PongBallScript;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * The game's main scene.
 * <p>
 * Pong, made with the FastJ Game Engine.
 *
 * @author Andrew Dey
 */
public class SimpleScene extends Scene {

    Polygon2D player, opponent, pongBall;
    Text2D playerScoreText, opponentScoreText;
    private int playerScore;
    private int opponentScore;

    private enum PlayerType {
        USER,
        OPPONENT
    }

    public SimpleScene(String name) {
        setSceneName(name);
    }

    @Override
    public void load(Display display) {
        display.modifyBackgroundColor(Color.black);

        /* Local Globals */
        final Pointf displaySize = Point.toPointf(display.getIntendedResolution());
        final Point playerSize = new Point(20, 100);
        final Font scoreFont = new Font("Consolas", Font.PLAIN, 12);

        /* Objects */
        player = (Polygon2D) createPlayer(PlayerType.USER, displaySize, playerSize).addAsGameObject(this);
        playerScoreText = (Text2D) createPlayerScore(PlayerType.USER, displaySize, scoreFont).addAsGameObject(this);

        opponent = (Polygon2D) createPlayer(PlayerType.OPPONENT, displaySize, playerSize).addAsGameObject(this);
        opponentScoreText = (Text2D) createPlayerScore(PlayerType.OPPONENT, displaySize, scoreFont).addAsGameObject(this);

        pongBall = (Polygon2D) createPongBall(displaySize).addAsGameObject(this);

        addKeyInputListener(new KeyboardActionListener() {
            @Override
            public void onKeyReleased(int keycode) {
                if (keycode == KeyEvent.VK_ESCAPE) {
                    unload(display);
                    FastJEngine.closeGame();
                }
            }
        });

        setInitialized(true);
    }

    @Override
    public void unload(Display display) {
        player.destroy(this);
        playerScoreText.destroy(this);
        playerScoreText = null;
        playerScore = 0;

        opponent.destroy(this);
        opponentScoreText.destroy(this);
        opponentScoreText = null;
        opponentScore = 0;

        pongBall.destroy(this);
        pongBall = null;

        clearAllLists();

        setInitialized(false);
    }

    @Override
    public void update(Display display) {
    }

    public boolean updateScore(String scorer) {
        switch (scorer.toLowerCase()) {
            case "opponent":
                opponentScore++;
                opponentScoreText.setText("Opponent Score: " + opponentScore);
                break;
            case "player":
                playerScore++;
                playerScoreText.setText("Player Score: " + playerScore);
                break;
            default:
                throw new IllegalArgumentException(scorer + " isn't a player in the game.");
        }

        if (playerScore >= 7) {
            unload(FastJEngine.getDisplay());
            FastJEngine.getLogicManager().setCurrentScene("Win");
            FastJEngine.getLogicManager().initCurrentScene(FastJEngine.getDisplay());
            ((WinScene) FastJEngine.getLogicManager().getCurrentScene()).setWinner("player");
            return true;
        } else if (opponentScore >= 7) {
            unload(FastJEngine.getDisplay());
            FastJEngine.getLogicManager().setCurrentScene("Win");
            FastJEngine.getLogicManager().initCurrentScene(FastJEngine.getDisplay());
            ((WinScene) FastJEngine.getLogicManager().getCurrentScene()).setWinner("opponent");
            return true;
        }
        return false;
    }

    /* Object create methods */

    private Polygon2D createPlayer(PlayerType playerType, Pointf displaySize, Point playerSize) {
        Pointf offset;
        Pointf[] playerBox;

        switch (playerType) {
            case USER:
                offset = new Pointf(10);
                playerBox = DrawUtil.createBox(offset, playerSize.x, playerSize.y);

                return (Polygon2D) new Polygon2D(playerBox, Color.white, true, true)
                        .addBehavior(new PlayerScript(this), this)
                        .addTag("ballCollider", this);
            case OPPONENT:
                offset = new Pointf(displaySize.x - (playerSize.x + 10), 10);
                playerBox = DrawUtil.createBox(offset, playerSize.x, playerSize.y);

                return (Polygon2D) new Polygon2D(playerBox, Color.white, true, true)
                        .addBehavior(new OpponentScript(), this)
                        .addTag("ballCollider", this);
            default:
                throw new IllegalStateException("Unexpected value: " + playerType);
        }
    }

    private Text2D createPlayerScore(PlayerType playerType, Pointf displaySize, Font scoreFont) {
        Pointf scoreLocation;
        String scoreString;

        switch (playerType) {
            case USER:
                scoreLocation = Pointf.multiply(displaySize, new Pointf(0.35f, 0.05f));
                scoreString = String.format("Player Score: %d", playerScore);
                break;
            case OPPONENT:
                scoreLocation = Pointf.multiply(displaySize, new Pointf(0.5f, 0.05f));
                scoreString = String.format("Opponent Score: %d", opponentScore);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + playerType);
        }

        return new Text2D(scoreString, scoreLocation, Color.white, scoreFont, true);
    }

    private Polygon2D createPongBall(Pointf displaySize) {
        Pointf centerWithBallOffset = Pointf.subtract(Pointf.divide(displaySize, 2f), 5);
        Pointf[] ballBox = DrawUtil.createBox(centerWithBallOffset, 10);

        return (Polygon2D) new Polygon2D(ballBox, Color.lightGray, true, true)
                .addBehavior(new PongBallScript(this), this);
    }
}