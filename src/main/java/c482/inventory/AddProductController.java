package c482.inventory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/** This class controls the Add Product Screen */
public class AddProductController implements Initializable {

    private Stage stage;
    private Object scene;

    @FXML private TableView<Part> PartsTableView;
    @FXML private TableColumn<Part, Integer> PartsIDColumn;
    @FXML private TableColumn<Part, String> PartsNameColumn;
    @FXML private TableColumn<Part, Integer> PartsInventoryColumn;
    @FXML private TableColumn<Part, Double> PartsCostColumn;


    @FXML private TableView<Part> AssociatedPartsTableView;
    @FXML private TableColumn<Product, Integer> AssociatedPartsIDColumn;
    @FXML private TableColumn<Product, String> AssociatedPartsNameColumn;
    @FXML private TableColumn<Product, Integer> AssociatedPartsInventoryColumn;
    @FXML private TableColumn<Product, Double> AssociatedPartsCostColumn;


    @FXML private TextField APRName;
    @FXML private TextField APRInventory;
    @FXML private TextField APRPrice;
    @FXML private TextField APRMaximum;
    @FXML private TextField APRMinimum;


    @FXML private TextField SearchField;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private ObservableList<Part> originalParts = FXCollections.observableArrayList();

    /**
     * This method is used to test whether input is numeric
     * @param str the string to test
     * @return true or false
     */
    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

        /**
         * This method will return the user to the main form page.
         * @param event the click of the cancel button on Add Product screen
         */
    @FXML public void cancelAddProduct(ActionEvent event) throws IOException {
        if (MainWindowController.confirmDialog("Cancel?", "Are you sure?")) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("main.fxml"));
            stage.setTitle("Inventory Management System");
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
    }
    /**
     * This method controls the search button on the Part table
     * @param event the click of the search button in the part table
     */
    @FXML public void searchPartButton(ActionEvent event) {
        String term = SearchField.getText();
        if (isNumeric(term)){
            int num = Integer.parseInt(term);
            ObservableList foundParts = Inventory.lookupPartID(num);
            if(foundParts.isEmpty()) {
                MainWindowController.confirmDialog("None found", "Unable to find: " + term);
            } else {
                PartsTableView.setItems(foundParts);
            }
        }
        else{
            ObservableList foundParts = Inventory.lookupPart(term);
            if(foundParts.isEmpty()) {
                MainWindowController.confirmDialog("None found", "Unable to find: " + term);
            } else {
                PartsTableView.setItems(foundParts);
            }
        }
    }

    /**
     * This method is used to delete parts.
     * @param event the click of the delete part button
     */
    @FXML
    void deletePartFromProduct(ActionEvent event) {
        Part selectedPart = AssociatedPartsTableView.getSelectionModel().getSelectedItem();
        if(selectedPart != null) {
            MainWindowController.confirmDialog("Deleting Parts","Delete " + selectedPart.getName() + " from the Product?");
            associatedParts.remove(selectedPart);
            updatePartTable();
            updateAssociatedPartTable();
        }
        else {
            MainWindowController.info("Nothing selected","Nothing selected", "Make a selection");
        }
    }
    /**
     * This method will generate unique IDs for all products.
     */
    public static int getNewID(){
        int ID = 1;
        for (int i = 0; i < Inventory.getAllProducts().size(); i++) {
            if (Inventory.getAllProducts().get(i).getProductID() == ID) {
                ID++;
            }
        }
        return ID;
    }
    /**
     * This method is used to associate parts with products.
     * @param event the click of the add button on the right side of the add product screen
     */
    @FXML void addPartToProduct(ActionEvent event) {
        Part selectedPart = PartsTableView.getSelectionModel().getSelectedItem();
        if(selectedPart != null) {
            associatedParts.add(selectedPart);
            updatePartTable();
            updateAssociatedPartTable();
        }
        else {
            MainWindowController.info("Select a part","Select a part", "Select a part to add to the Product");
        }
    }

    /**
     * This method will input validate, then return the user to the main form page.
     * @param event the click of the save button
     */
    @FXML void saveProduct(ActionEvent event) throws IOException {
        if (associatedParts.isEmpty()){
            MainWindowController.info("Input Error", "Please add one or more parts", "A be associated with a part.");
            return;}
        if (APRName.getText().isEmpty() || APRMinimum.getText().isEmpty() || APRMaximum.getText().isEmpty() || APRMaximum.getText().isEmpty() || APRPrice.getText().isEmpty()) {
            MainWindowController.info("Input Error", "Cannot have blank fields", "Check all the fields.");
            return;
        }
        Integer inv = Integer.parseInt(this.APRInventory.getText());
        Integer min = Integer.parseInt(this.APRMinimum.getText());
        Integer max = Integer.parseInt(this.APRMaximum.getText());
        if (max < min) {
            MainWindowController.info("Input Error", "Error in min/max field", "Check Min and Max value.");
            return;
        }
        if (inv < min || inv > max) {
            MainWindowController.info("Input Error", "Error in inventory field", "Inventory must be between Minimum and Maximum");
            return;
        }
        if (MainWindowController.confirmDialog("Save?", "Save?")) {
            Product product = new Product();
            product.setProductID(getNewID());
            product.setName(this.APRName.getText());
            product.setStock(Integer.parseInt(this.APRInventory.getText()));
            product.setMin(Integer.parseInt(this.APRMinimum.getText()));
            product.setMax(Integer.parseInt(this.APRMaximum.getText()));
            product.setProductPrice(Double.parseDouble(this.APRPrice.getText()));
            product.addAssociatedPart(associatedParts);
            Inventory.addProduct(product);

            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("main.fxml"));
            stage.setTitle("Inventory Management System");
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }

    }

    /**
     * This method will update the part table.
     */
    public void updatePartTable() {
        PartsTableView.setItems(Inventory.getAllParts());
    }

    /**
     * This method will update the ASSOCIATED parts table.
     */
    private void updateAssociatedPartTable() {
        AssociatedPartsTableView.setItems(associatedParts);
    }

    /**
     * Upon Initialization, this method will populate cell values in the parts and associated parts table.
     * @param resources initialize resources?
     * @param location initialize with location?
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        originalParts = Inventory.getAllParts();
        PartsIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        PartsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        PartsInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        PartsCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        PartsTableView.setItems(originalParts);
        AssociatedPartsIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        AssociatedPartsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        AssociatedPartsInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        AssociatedPartsCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        AssociatedPartsTableView.setItems(associatedParts);
        updatePartTable();
        updateAssociatedPartTable();
    }
}
