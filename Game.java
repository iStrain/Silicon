
/*
 * JavaFX GAME BY THE SILICON TEAM
 * -	Duncan Baxter s3737140
 */

import java.io.IOException;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/*
 * Top-level Class for our Game
 */

public class Game extends Application {
    GraphicsContext gc; // Graphics Context for drawing on primary Stage
    String oldHint; // Storage: former "press ESC key" message
    KeyCombination oldKey; // Storage: former exit-from-FullScreen key(s)

    /*
     * JavaFX Application thread automatically calls start() method. The parameter
     * Stage stage is our top-level window, then Scene scene, BorderPane root, and
     * finally other Nodes (eg. Canvas canvas, Canvas art, Text wrap).
     * 
     * start() may throw an IOException when trying to load the Image from the file.
     * There's no point in continuing if we can't load the Image, so the exception
     * can go through to the 'keeper.
     * 
     * @see javafx.application.Application#start(javafx.stage.Stage)
     */
    @Override
    public void start(Stage stage) throws IOException {
	// Create an Image from our file
	Image image = new Image("file:src/iconic-photographs-1940-first-computer.jpg");
	// Create a Canvas to draw the GameBoard on
	Canvas canvas = new Canvas(image.getWidth(), image.getHeight());
	// Get the graphics context of the Canvas (used for drawing)
	gc = canvas.getGraphicsContext2D();
	// Create a BorderPane with the Canvas in the Center region
	BorderPane root = new BorderPane(canvas);
	// Use JavaFX CSS to set the style properties of the BorderPane
	root.setStyle("-fx-padding: 10;" + "-fx-border-style: solid inside;" + "-fx-border-width: 2;"
		+ "-fx-border-insets: 5;" + "-fx-border-radius: 5;" + "-fx-border-color: blue;");
	// Set our first "decade/era" photo as the BorderPane background
	root.setBackground(
		new Background(new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
			BackgroundPosition.CENTER, new BackgroundSize(0.0d, 0.0d, false, false, false, true))));
	/*
	 * Draw some WordArt-style text in the Center region (traditional method).
	 * Alternatively, we could use setStyle() and JavaFX CSS to set the style
	 * properties of the Canvas.
	 */
	gc.setTextAlign(TextAlignment.CENTER);
	gc.setTextBaseline(VPos.CENTER);
	gc.setFill(Color.GREEN);
	gc.setStroke(Color.WHITE);
	gc.setLineWidth(2);
	gc.setFont(Font.font("Stencil", FontWeight.BOLD, 48));
	gc.fillText("Silicon", canvas.getWidth() / 2, 0.0);
	gc.strokeText("Silicon", canvas.getWidth() / 2, 0.0);

	// Signal that we need to layout the BorderPane (ie. Nodes are done)
	root.needsLayoutProperty();
	// Set the Style for the primary Stage
	stage.initStyle(StageStyle.TRANSPARENT);
	// Set the title of the primary Stage
	stage.setTitle("Silicon");
	// Create a Scene based on the BorderPane with no background fill
	Scene scene = new Scene(root, null);
	// Load the Stylesheet for the Scene
	scene.getStylesheets().add("file:src/Game.css");
	// Add the Scene to the primary Stage and resize
	stage.setScene(scene);
	// Shift into FullScreen mode
	if (enterFullScreen(stage)) {
	    // Show the primary Stage
	    stage.show();

	    VBox vb = new VBox();
	    vb.setAlignment(Pos.CENTER);

	    // Create the Buttons for our main menu
	    Button btCreate = new Button("Create Game");
	    Button btLoad = new Button("Load Game");
	    Button btSave = new Button("SaveGame");
	    Button btSettings = new Button("Settings");
	    Button btExit = new Button("Exit");
	    vb.getChildren().addAll(btCreate, btLoad, btSave, btSettings, btExit);
	    // Parental secrets: Nobody will ever tell you your child is ugly.  These Buttons are beautiful!
	    root.setCenter(vb);

	    // Create and register the handlers
	    btCreate.setOnAction((ActionEvent ae) -> {
		System.out.println("Process Create");
	    });

	    btLoad.setOnAction(ae -> {
		System.out.println("Process Load");
	    });

	    btSave.setOnAction(ae -> {
		System.out.println("Process Save");
	    });

	    btSettings.setOnAction(ae -> {
		System.out.println("Process Settings");
	    });

	    btExit.setOnAction(ae -> {
		System.out.println("Process Exit");
		leaveFullScreen(stage);
	    });
	}
	root.requestFocus();
    }
    
    /*
     * Methods to enter and leave FullScreen mode
     * - save and restore the screen hint
     * - save and restore the key combination (usually ESC)
     * - disable use of the ESC key to leave
     */
    private boolean enterFullScreen(Stage stage) {
	oldHint = stage.getFullScreenExitHint();
	stage.setFullScreenExitHint("Press \"ESC\" key for main menu");
	oldKey = stage.getFullScreenExitKeyCombination();
	stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
	stage.setFullScreen(true);
	return true;
    }

    private boolean leaveFullScreen(Stage stage) {
	stage.setFullScreenExitHint(oldHint);
	stage.setFullScreenExitKeyCombination(oldKey);
	stage.setFullScreen(false);
	return true;
    }

    /*
     * The usual "main" method - code is only executed on platforms that lack full
     * JavaFX support.
     */
    public static void main(String[] args) {
	Application.launch(args);
    }
}
