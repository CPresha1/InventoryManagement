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

/**
 * This class will control the Modify Part Screen
 */
public class ModifyPartController implements Initializable {

    public ToggleGroup Options_Modify;
    @FXML private RadioButton outsourced;
    @FXML private RadioButton inHouse;
    @FXML private Label inhouseoroutsourced;
    @FXML private TextField ID;
    @FXML private TextField Name;
    @FXML private TextField Inventory;
    @FXML private TextField Price;
    @FXML private TextField Maximum;
    @FXML private TextField Minimum;
    @FXML private TextField companyORmachineID;

    private Stage stage;
    private Object scene;
    public Part selectedPart;
    private int partID;

    /**
     * Changes field to be filled out based on radio button selection
     */
    public void radioadd()
    {
        if (outsourced.isSelected())
            this.inhouseoroutsourced.setText("Company Name");
        else
            this.inhouseoroutsourced.setText("Machine ID");
    }

    /**
     * Populates info of selected part, taking into account InHouse or Outsourced
     * @param selectedPart to get
     */
    public void setParts(Part selectedPart) {
        this.selectedPart = selectedPart;
        partID = c482.inventory.Inventory.getAllParts().indexOf(selectedPart);
        ID.setText(Integer.toString(selectedPart.getId()));
        Name.setText(selectedPart.getName());
        Inventory.setText(Integer.toString(selectedPart.getStock()));
        Price.setText(Double.toString(selectedPart.getPrice()));
        Maximum.setText(Integer.toString(selectedPart.getMax()));
        Minimum.setText(Integer.toString(selectedPart.getMin()));
        if(selectedPart instanceof InHouse){
            InHouse i = (InHouse) selectedPart;
            inHouse.setSelected(true);
            this.inhouseoroutsourced.setText("Machine ID");
            companyORmachineID.setText(Integer.toString(i.getMachineID()));
        }
        else{
            Outsourced o = (Outsourced) selectedPart;
            outsourced.setSelected(true);
            this.inhouseoroutsourced.setText("Company Name");
            companyORmachineID.setText(o.getCompanyName());
        }
    }

    /**
     * Will verify that user would like to save and input validate, then send to main screen
     * @param event on clicking save
     */
    @FXML void onActionSave(ActionEvent event) throws IOException {
        int partInventory = Integer.parseInt(Inventory.getText());
        int partMin = Integer.parseInt(Minimum.getText());
        int partMax = Integer.parseInt(Maximum.getText());
        if (MainWindowController.confirmDialog("Would you like to save?", "Save?"))
            if (partMax < partMin) {
                MainWindowController.info("Error", "Invalid min/max", "Enter valid min/max");
            } else if (partInventory < partMin || partInventory > partMax) {
                MainWindowController.info("Error", "Invalid Inventory value", "Enter value between min and max");
            } else {
                int id = Integer.parseInt(ID.getText());
                String name = Name.getText();
                double price = Double.parseDouble(Price.getText());
                int stock = Integer.parseInt(Inventory.getText());
                int min = Integer.parseInt(Minimum.getText());
                int max = Integer.parseInt(Maximum.getText());
                if (inHouse.isSelected()) {
                    try {
                        int machineID = Integer.parseInt(companyORmachineID.getText());
                        InHouse temp = new InHouse(id, name, price, min, max, stock, machineID);
                        c482.inventory.Inventory.updatePart(partID, temp);
                        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                        scene = FXMLLoader.load(getClass().getResource("main.fxml"));
                        stage.setTitle("Inventory Management System");
                        stage.setScene(new Scene((Parent) scene));
                        stage.show();
                    }
                    catch (NumberFormatException e){
                        MainWindowController.info("Error", "Invalid Machine ID ", "Machine ID must only have numbers 0-9");
                    }
                }
                else {
                    String companyName = companyORmachineID.getText();
                    Outsourced temp = new Outsourced(id, name, price, min, max, stock, companyName);
                    c482.inventory.Inventory.updatePart(partID, temp);
                    stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("main.fxml"));
                    stage.setTitle("Inventory Management System");
                    stage.setScene(new Scene((Parent) scene));
                    stage.show();
                }
            }
    }

    /**
     * Will direct user to main if the cancel button is clicked and confirmation
     * @param event on clicking cancel
     */
    @FXML public void onActionCancel(ActionEvent event) throws IOException {
        if(MainWindowController.confirmDialog("Cancel?", "Back to main menu?")) {
            stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("main.fxml"));
            stage.setTitle("Inventory Management System");
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
    }

    /**
     * For controller to implement
     * @param location to initialize?
     * @param resources to initialize
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }
}
