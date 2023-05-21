package c482.inventory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static c482.inventory.Inventory.getAllParts;

/**
 * This class controls the Add Part Screen
 */
public class AddPartController implements Initializable {
    public RadioButton inHouse;
    public ToggleGroup Options;
    @FXML private RadioButton outsourced;
    @FXML private Label inhouseoroutsourced;
    @FXML private TextField Inventory;
    @FXML private TextField Price;
    @FXML private TextField companyORmachineID;
    @FXML private TextField Name;
    @FXML private TextField Maximum;
    @FXML private TextField Minimum;

    private Stage stage;
    private Object scene;

/**
 * This method will set the text in Add Part based on the radio buttons selected.
 */
    public void radioadd()
    {
        if (outsourced.isSelected())
            this.inhouseoroutsourced.setText("Company Name");
        else
            this.inhouseoroutsourced.setText("Machine ID");
    }
/**
 * On clicking cancel, this will return the user back to the Main Screen.
 * @param event the click of the cancel button
 */
    @FXML public void onActionCancel(ActionEvent event) throws IOException {
        if(MainWindowController.confirmDialog("Would you like to cancel?", "Are you sure?")) {
            stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("main.fxml"));
            stage.setTitle("Inventory Management System");
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
    }

    /**
     * Randomly generates am ID for all Parts
     */
    public static int getNewID(){
        int ID = 1;
        for (int i = 0; i < getAllParts().size(); i++) {
            ID++;
        }
        return ID;
    }
    /**
     * On clicking save, this method will error check and add part to the part table upon valid input.
     * @param event the click of the save button
     */
    @FXML public void onActionSave(ActionEvent event) {
        try {
            int partInventory = Integer.parseInt(Inventory.getText());
            int partMin = Integer.parseInt(Minimum.getText());
            int partMax = Integer.parseInt(Maximum.getText());
            if(MainWindowController.confirmDialog("Save?", "Save?"))
                if(partMax < partMin) {
                    MainWindowController.info("Input Error", "Error in min and max field", "Check Min and Max value." );
                }
                else if(partInventory < partMin || partInventory> partMax) {
                    MainWindowController.info("Input Error", "Error in inventory field", "Inventory must be between Minimum and Maximum" );
                }
                else{
                    int newID = getNewID();
                    String name = Name.getText();
                    int stock = partInventory;
                    double price = Double.parseDouble(Price.getText());
                    int min = partMin;
                    int max = partMax;
                    if (outsourced.isSelected()) {
                        String companyName = companyORmachineID.getText();
                        Outsourced o = new Outsourced(newID, name, price, stock, min, max, companyName);
                        c482.inventory.Inventory.addPart(o);
                    } else {
                        int machineID = Integer.parseInt(companyORmachineID.getText());
                        InHouse i = new InHouse(newID, name, price, stock, min, max, machineID);
                        c482.inventory.Inventory.addPart(i);
                    }
                    stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("main.fxml"));
                    stage.setTitle("Inventory Management System");
                    stage.setScene(new Scene((Parent) scene));
                    stage.show();
                }
        }
        catch (Exception e){
            MainWindowController.info("Input Error", "Error in adding part", "Check for valid inputs" );
        }
    }
    /**
     * For implementation of Initializable
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}