import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class MineSweeper extends Application {

//	---------------------------------------------Alanlar------------------------------------------------------------------------

	int tileSize = 50;
	int w = 800;
	int h = 600;
	AudioClip click = new AudioClip(getClass().getResource("click_sound_5.mp3").toExternalForm());
	AudioClip bomba = new AudioClip(getClass().getResource("Menu Choice.mp3").toExternalForm());

	int xTile = w / tileSize;
	int yTile = h / tileSize;
	Tile[][] grid = new Tile[xTile][yTile];

//	------------------------------------------content : method---------------------------------------------------------------------	

	public Parent content() {
		Pane root = new Pane();
		root.setPrefSize(w, h);

		for (int y = 0; y < yTile; y++) {

			for (int x = 0; x < xTile; x++) {

				grid[x][y] = new Tile(x, y, Math.random() < 0.2);
				root.getChildren().add(grid[x][y]);

			}

		}

		for (int y = 0; y < yTile; y++) {

			for (int x = 0; x < xTile; x++) {

				long bombs = komsular(x, y).stream().filter(t -> t.bomba).count();

				if (grid[x][y].bomba) {
					grid[x][y].text.setFill(Color.RED);
					continue;
				}

				if (bombs > 0) {
					grid[x][y].text.setText(String.valueOf(bombs));
					grid[x][y].text.setFill(Color.BLUE);

				}
//				else {//gerek yok.Cunku eger kendisinde bomba yoksa text "" olacak ve komuslarda bomba yoksa setTextValueOf(bombs) cagirilmayacak ve text "" olarak kalacak yani Empty sayilacak
//					grid[x][y].text.setText(null);
//					continue;
//				}

			}

		}

		return root;

	}

//	------------------------------------------komsular : method-----------------------------------------------------------------------

	List<Tile> komsular(int x, int y) {

		List<Tile> komsular = new ArrayList<Tile>();

		if (x > 0) komsular.add(grid[x - 1][y]);
		if (x < xTile - 1) komsular.add(grid[x + 1][y]);

		if (y > 0) {
			komsular.add(grid[x][y - 1]);
			if (x > 0) komsular.add(grid[x - 1][y - 1]);
			if (x < xTile - 1) komsular.add(grid[x + 1][y - 1]);
		}

		if (y < yTile - 1) {
			komsular.add(grid[x][y + 1]);
			if (x > 0) komsular.add(grid[x - 1][y + 1]);
			if (x < xTile - 1) komsular.add(grid[x + 1][y + 1]);
		}

		return komsular;
	}

//	---------------------------------------Tile : class-----------------------------------------------------------------------

	class Tile extends StackPane {

		Rectangle border = new Rectangle(tileSize - 2, tileSize - 2);
		Text text = new Text();
		boolean bomba;
		boolean isOpen;
		int x, y;

		public Tile(int x, int y, boolean bomba) {

			this.x = x;
			this.y = y;
			this.bomba = bomba;

			border.setStroke(Color.LIGHTGRAY);
			border.setFill(Color.GOLD);
			text.setText(bomba ? "X" : "");
			text.setVisible(false);
			text.setFont(Font.font(18));
			getChildren().addAll(border, text);

			setTranslateX(x * tileSize);
			setTranslateY(y * tileSize);

			setOnMouseClicked(e -> open(this));

		}

	}

//	------------------------------------------open : method-----------------------------------------------------------------------
	public void open(Tile t) {

		if (t.isOpen) return;

		t.isOpen = true;
		if (t.bomba)
			bomba.play();
		else
			click.play();

		t.text.setVisible(true);
		t.border.setFill(null);
		if (t.text.getText().isEmpty()) komsular(t.x, t.y).stream().forEach(e -> open(e));

	}
//	------------------------------------------start : method----------------------------------------------------------------------

	@Override
	public void start(Stage stage) throws Exception {
		Scene scene = new Scene(content());

		stage.setScene(scene);
		stage.setTitle("Mine Sweeper");
		stage.getIcons().add(new Image("Bolt.png"));
		stage.show();
	}

//	-------------------------------------------main: method-----------------------------------------------------------------------

	public static void main(String[] args) {
		launch(args);
	}

}
