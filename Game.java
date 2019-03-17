
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
	gc.fillText("Silicon", canvas.getWidth() / 2, canvas.getHeight() / 2);
	gc.strokeText("Silicon", canvas.getWidth() / 2, canvas.getHeight() / 2);

	// Signal that we need to layout the BorderPane (ie. Nodes are done)
	root.needsLayoutProperty();
	// Set the Style for the primary Stage
	stage.initStyle(StageStyle.TRANSPARENT);
	// Set the title of the primary Stage
	stage.setTitle("Silicon");
	// Create a Scene based on the BorderPane with no background fill
	Scene scene = new Scene(root, null);
//	scene.getStylesheets().add("file:src/button.css"); // Grr ... why won't it work?
	// Add the Scene to the primary Stage and resize
	stage.setScene(scene);
	// Shift into FullScreen mode
	if (enterFullScreen(stage)) {
	    // Show the primary Stage
	    stage.show();

	    VBox vb = new VBox();
	    vb.setAlignment(Pos.CENTER);
	    vb.setStyle("-fx-padding: 10;" + "-fx-spacing: 8;");

	    // "DB Code - it's kludgy, but it works ... most of the time."
	    // Example 1: Can't make this effing CSS StyleSheet work!
	    // Solution: set the Button's style in a method instead
	    // - how many reasons can *you* think of, that make this a bad idea?
	    Button btCreate = MyButton("Create Game");
	    Button btLoad = MyButton("Load Game");
	    Button btSave = MyButton("SaveGame");
	    Button btSettings = MyButton("Settings");
	    Button btExit = MyButton("Exit");
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
     * All this is doing, is what the effing CSS style sheet would do, if it worked as advertised
     * But I am mortally ashamed of how inelegant this is
     * ... and I apologise (in advance) to the other programmers on the team for making them look at such garbage code!
     */
    private Button MyButton(String str) {
	Button rButton = new Button(str);
	rButton.setStyle("-fx-background-color:#ecebe9,rgba(0,0,0,0.05),linear-gradient(#dcca8a, #c7a740),"
		+ "linear-gradient(#f9f2d6 0%, #f4e5bc 20%, #e6c75d 80%, #e2c045 100%),"
		+ "linear-gradient(#f6ebbe, #e6c34d);" + "-fx-background-insets: 0,9 9 8 9,9,10,11;"
		+ "-fx-background-radius: 50;" + "-fx-padding: 15 30 15 30;" + "-fx-font-family: 'Helvetica';"
		+ "-fx-font-size: 18px;" + "-fx-text-fill: #311c09;"
		+ "-fx-effect: innershadow( three-pass-box , rgba(0,0,0,0.1) , 2, 0.0 , 0 , 1);");
	return rButton;
    }

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
