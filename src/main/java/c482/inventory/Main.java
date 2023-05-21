package c482.inventory;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
/**
 * Javadocs to be found in Inventory/Javadoc
 * Logical Error in MainWindowController.java on Line 234!!!
 * Future Enhancement to be found in MainWindowController.java on Line 20!!!
 * This is our main class, to be ran from
 */
public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        Inventory.addPart(new InHouse(1, "Wrench", 15, 20,10 , 95, 19));
        Inventory.addPart(new InHouse(2, "Hammer", 12, 25,10 , 84, 12));
        Inventory.addPart(new InHouse(3, "Nail", 1, 520, 20, 1000, 82));
        Inventory.addPart(new InHouse(4, "Screw", 2, 500,20 , 1000, 99));


        Inventory.addProduct(new Product(1, 10, 1, 50, "Toolkit", 249.99));
        Inventory.addProduct(new Product(2, 8, 1, 50, "Carpenter Bundle", 159.99));
        Inventory.addProduct(new Product(3, 5, 1, 50, "Building Bundle", 159.99));



        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1400, 600);
        stage.setTitle("Inventory Management System");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * launching main
     * @param args args
     */
    public static void main(String[] args) {
        launch();
    }
}