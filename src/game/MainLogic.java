package game;

import fastj.engine.FastJEngine;
import fastj.engine.graphics.nondrawable.Point;
import fastj.engine.io.Display;
import fastj.engine.systems.game.LogicManager;
import scenes.SimpleScene;
import scenes.WinScene;

/**
 * Main Logic Manager for the game.
 * <p>
 * Pong, made with the FastJ Game Engine.
 *
 * @author Andrew Dey
 */
public class MainLogic extends LogicManager {

    @Override
    public void setup(Display display) {
        addScene(new WinScene("Win"));
        addScene(new SimpleScene("Game"));

        setCurrentScene("Game");
        initCurrentScene(display);
    }

    public static void main(String[] args) {
        FastJEngine.init("Pong", new MainLogic());

        FastJEngine.configureIntendedResolution(new Point(960, 540));
        FastJEngine.setTargetUPS(75);
        FastJEngine.setTargetFPS(225);
        FastJEngine.showFPSOnDisplay(true);

        if (System.getProperty("os.name").startsWith("Win")) {
            FastJEngine.configureHardwareAcceleration(FastJEngine.HWAccelType.DIRECT3D);
        } else {
            FastJEngine.configureHardwareAcceleration(FastJEngine.HWAccelType.OPENGL);
        }

        FastJEngine.run();
    }
}