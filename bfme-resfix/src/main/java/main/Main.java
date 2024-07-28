package main;

import controller.Controller;
import listener.ChangeListener;
import helper.ExceptionHandler;
import helper.GameInfo;
import model.Display;
import model.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import view.MainView;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;

public class Main {

    public static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void setup() {
        try {
            // ResourceBundle Initialization
            ResourceBundle properties = ResourceBundle.getBundle("application");
            ResourceBundle labels = ResourceBundle.getBundle("labels", Locale.getDefault());

            // Model Initialization
            List<Game> games = new GameInfo().initializeGames();
            Display display = new Display();

            // View Initialization
            MainView mainView = new MainView(properties, labels);

            // Controller Initialization
            Controller controller = new Controller(properties, labels, games, display, mainView);
            mainView.setController(controller);

            // ChangeListener Initialization
            ChangeListener listener = new ChangeListener(controller);
            controller.setChangeListener(listener);

            // Async Initialization
            CompletableFuture<Void> initCustomFont = controller.initCustomFont();
            CompletableFuture<Void> initExHandler = controller.initExHandler();
            CompletableFuture<Void> initData = listener.poll();

            // Wait for initial tasks to complete
            initCustomFont.join();
            initExHandler.join();
            initData.join();

            // On Completion
            CompletableFuture.allOf(initCustomFont, initExHandler, initData).thenRun(() -> {
                controller.showUI();
                controller.runUpdateCheck();
                listener.listenToRegistry();
                listener.listenToFilesystem();
                logger.info("Setup complete. {}", Locale.getDefault());
            }).exceptionally(e -> {
                ExceptionHandler.getInstance().promptError("Error in Main.setup()", (Exception) e);
                return null;
            });
        } catch (Exception e) {
            ExceptionHandler.getInstance().promptError("Error in Main.setup()", e);
        }
    }
}
