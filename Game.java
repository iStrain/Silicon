
/*
 * JavaFX GAME BY THE SILICON TEAM
 * -	Duncan Baxter s3737140
 */

import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/*
 * Top-level Class for our Game
 */

public class Game extends Application {
    Stage stage;
    GraphicsContext gc; // Graphics Context for drawing on primary Stage
    BorderPane root;
    String oldHint; // Storage: former "press ESC key" message
    KeyCombination oldKey; // Storage: former exit-from-FullScreen key(s)

    /*
     * JavaFX Application thread automatically calls start() method. The parameter
     * Stage stage is our top-level window, then Scene scene, BorderPane root, and
     * finally other Nodes. This is quite short: it just creates the GUI.
     * 
     * start() may throw an IOException when trying to load the Image from the file.
     * There's no point in continuing if we can't load the Image, so the exception
     * can go through to the 'keeper.
     * 
     * @see javafx.application.Application#start(javafx.stage.Stage)
     */
    @Override
    public void start(Stage stage) throws IOException {
	this.stage = stage;
	// Create an Image from our file
	Image image = new Image("file:bin/images/iconic-photographs-1940-first-computer.jpg");
	// Create a Canvas to draw the GameBoard on
	Canvas canvas = new Canvas(image.getWidth(), image.getHeight());
	// Get the graphics context of the Canvas (used for drawing)
	gc = canvas.getGraphicsContext2D();
	// Create a BorderPane with the Canvas in the Center region
	root = new BorderPane(canvas);
	// Load the JavaFX CSS and set the style properties of the BorderPane
	root.getStylesheets().add("file:src/Game.css");
	root.getStyleClass().add("my-border-pane");
	// Set our first "decade/era" photo as the BorderPane background
	root.setBackground(
		new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
			BackgroundPosition.CENTER, new BackgroundSize(0.0d, 0.0d, false, false, false, true))));
	// Set the Style for the primary Stage
	stage.initStyle(StageStyle.TRANSPARENT);
	// Set the title of the primary Stage
	stage.setTitle("Silicon");
	// Create a Scene based on the BorderPane with no background fill
	Scene scene = new Scene(root, null);
	// Add the Scene to the primary Stage and resize
	stage.setScene(scene);
	// Can we enter FullScreen mode?
	if (enterFullScreen()) {
	    // Y: Show the Game Board and Main Menu
	    drawLogo();
	    mainMenu();
	    // Signal that we need to layout the BorderPane (ie. Nodes are done)
	    root.needsLayoutProperty();
	    stage.show();
	}
    }

    /*
     * drawLogo() method - draws the Silicon logo on the Game Board. The text
     * formatting is defined in the Game.css file as ".logo-text"
     */
    private boolean drawLogo() {
	Text text = new Text("silicon");
	text.getStyleClass().add("logo-text");
	root.setTop(text);
	BorderPane.setAlignment(text, Pos.CENTER);
	return true;
    }

    /*
     * mainMenu() method - draws main menu buttons, includes event handlers
     */
    private boolean mainMenu() {
	// Create a VBox to hold our main menu
	VBox vb = new VBox(5.0d);
	vb.setAlignment(Pos.CENTER);

	// Create the Buttons for our main menu (styles are in Game.css)
	Button btCreate = new Button("Create Game");
	Button btLoad = new Button("Load Game");
	Button btSave = new Button("Save Game");
	Button btSettings = new Button("Settings");
	Button btExit = new Button("Exit");
	vb.getChildren().addAll(btCreate, btLoad, btSave, btSettings, btExit);
	root.setCenter(vb);
	BorderPane.setAlignment(vb, Pos.CENTER);

	// Create and register the event-handlers
	btCreate.setOnAction((ActionEvent ae) -> {
	    /*
	     * Use our sound generator to play "middle C" for 1/10 of a second. Of course,
	     * here we hit a tiny problem: our Button ActionEvent fires on mouse button
	     * lift-off, but we want to play the sound on mouse button down. Playing the
	     * sound on lift-off just makes our game seem like it has a latency problem. Do
	     * I have volunteers to write some mouse event-handlers?
	     */
	    Sound.tone(262, 100);
	    System.out.println("Process Create");
	});

	btLoad.setOnAction(ae -> {
	    Sound.tone(262, 100);
	    System.out.println("Process Load");
	});

	btSave.setOnAction(ae -> {
	    Sound.tone(262, 100);
	    System.out.println("Process Save");
	});

	btSettings.setOnAction(ae -> {
	    Sound.tone(262, 100);
	    System.out.println("Process Settings");
	});

	btExit.setOnAction(ae -> {
	    Sound.tone(262, 100);
	    System.out.println("Process Exit");
	    leaveFullScreen();
	});
	return true;
    }

    /*
     * Methods to enter and leave FullScreen mode. - save and restore the on-screen
     * hint; - save and restore the key combination (usually ESC); - disable use of
     * the ESC key to leave.
     */
    private boolean enterFullScreen() {
	oldHint = stage.getFullScreenExitHint();
	stage.setFullScreenExitHint("");
	oldKey = stage.getFullScreenExitKeyCombination();
	stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
	stage.setFullScreen(true);
	return true;
    }

    private boolean leaveFullScreen() {
	stage.setFullScreenExitHint(oldHint);
	stage.setFullScreenExitKeyCombination(oldKey);
	stage.setFullScreen(false);
	return true;
    }

    /*
     * The usual "main" method - this code is only executed on platforms that lack
     * full JavaFX support.
     */
    public static void main(String[] args) {
	Application.launch(args);
    }
}
