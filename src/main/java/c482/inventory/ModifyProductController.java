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
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import c482.inventory.Inventory;

/**
 * This class will control the Modify Part Screen
 */
public class ModifyProductController implements Initializable {

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


    @FXML private TextField Name;
    @FXML private TextField Inventory;
    @FXML private TextField Price;
    @FXML private TextField Maximum;
    @FXML private TextField Minimum;
    @FXML private TextField ID;

    @FXML private TextField SearchField;
    private Product selectedProduct;
    private Product modProduct;
    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int productID;

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
     * Populates info of selected product
     * @param selectedProduct to set
     */
    public void setProduct(Product selectedProduct) {
        this.selectedProduct = selectedProduct;
        productID = c482.inventory.Inventory.getAllProducts().indexOf(selectedProduct);
        ID.setText(Integer.toString(selectedProduct.getProductID()));
        Name.setText(selectedProduct.getName());
        Inventory.setText(Integer.toString(selectedProduct.getStock()));
        Price.setText(Double.toString(selectedProduct.getPrice()));
        Maximum.setText(Integer.toString(selectedProduct.getMax()));
        Minimum.setText(Integer.toString(selectedProduct.getMin()));
        associatedParts.addAll(selectedProduct.getAllAssociatedParts());
    }

    /**
     *  For searching parts in Modify Product screen
     * @param event on clicking search
     */
    @FXML public void searchPartButton(ActionEvent event) {
        String term = SearchField.getText();
        if (isNumeric(term)){
            int num = Integer.parseInt(term);
            ObservableList foundParts = c482.inventory.Inventory.lookupPartID(num);
            if(foundParts.isEmpty()) {
                MainWindowController.confirmDialog("None found", "Unable to find: " + term);
            } else {
                PartsTableView.setItems(foundParts);
            }
        }
        else{
            ObservableList foundParts = c482.inventory.Inventory.lookupPart(term);
            if(foundParts.isEmpty()) {
                MainWindowController.confirmDialog("None found", "Unable to find: " + term);
            } else {
                PartsTableView.setItems(foundParts);
            }
        }
    }

    /**
     * Will navigate to main screen upon click of cancel button
     * @param event on clicking modify product
     */
    @FXML public void modifyProductCancel(ActionEvent event) throws IOException {
        if (MainWindowController.confirmDialog("Cancel?", "Are you sure?")) {
            stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("main.fxml"));
            stage.setTitle("Inventory Management System");
            stage.setScene(new Scene((Parent) scene));
            stage.show();
        }
    }

    /**
     * Will verify that user would like to save and input validate, then send to main screen
     * @param event on clicking save
     */
    @FXML void modifyProductSave(ActionEvent event) throws IOException {
        int productInventory = Integer.parseInt(Inventory.getText());
        int productMinimum = Integer.parseInt(Minimum.getText());
        int productMaximum = Integer.parseInt(Maximum.getText());
        if(MainWindowController.confirmDialog("Save?", "Save?"))
            if(productMaximum < productMinimum) {
                MainWindowController.info("Error", "Invalid min/max field", "Check Min and Max value." );
            }
            else if(productInventory < productMinimum || productInventory > productMaximum) {
                MainWindowController.info("Error", "Invalid inventory field", "Inventory must be between Minimum and Maximum" );
            }
            else {
                this.modProduct = selectedProduct;
                selectedProduct.setProductID(Integer.parseInt(ID.getText()));
                selectedProduct.setName(Name.getText());
                selectedProduct.setStock(Integer.parseInt(Inventory.getText()));
                selectedProduct.setProductPrice(Double.parseDouble(Price.getText()));
                selectedProduct.setMax(Integer.parseInt(Maximum.getText()));
                selectedProduct.setMin(Integer.parseInt(Minimum.getText()));
                selectedProduct.getAllAssociatedParts().clear();
                selectedProduct.addAssociatedPart(associatedParts);
                c482.inventory.Inventory.updateProduct(productID, selectedProduct);

                //Back to Main Screen
                stage = (Stage) ((Button)event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("main.fxml"));
                stage.setTitle("Inventory Management System");
                stage.setScene(new Scene((Parent) scene));
                stage.show();
            }
    }

    /**
     * Will add a part to associate with product
     * @param event on clicking add in product screen
     */
    @FXML void addPartToProduct(ActionEvent event) {
        Part selectedPart = PartsTableView.getSelectionModel().getSelectedItem();

        if(selectedPart != null) {
            associatedParts.add(selectedPart);
            updateAssociatedPartTable();
        }

        else {
            MainWindowController.info("Select","Select a part", "Select a part to add");
        }
    }

    /**
     * Will delete the association of part and product with input validation
     * @param event on clicking delete part in product screen
     */
    @FXML
    void deletePartFromProduct(ActionEvent event) {
        Part selectedPart = AssociatedPartsTableView.getSelectionModel().getSelectedItem();

        if(selectedPart != null) {
            MainWindowController.confirmDialog("Deleting Parts","Delete " + selectedPart.getName() + " from the Product?");
            associatedParts.remove(selectedPart);
            updateAssociatedPartTable();
        }

        else {
            MainWindowController.info("No Selection","No Selection", "Please choose something to remove");
        }
    }
    /**
     * Will update the parts table with allParts
     */
    public void updatePartTable() {
        PartsTableView.setItems(c482.inventory.Inventory.getAllParts());
    }

    /**
     * Will update the associated parts table
     */
    private void updateAssociatedPartTable() {
        AssociatedPartsTableView.setItems(associatedParts);
    }

    /**
     * Upon initialization will resolve cell values
     * @param resources to initialize
     * @param location to intialize
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PartsIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        PartsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        PartsInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        PartsCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        PartsTableView.setItems(c482.inventory.Inventory.getAllParts());
        AssociatedPartsIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        AssociatedPartsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        AssociatedPartsInventoryColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        AssociatedPartsCostColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        AssociatedPartsTableView.setItems(associatedParts);

        updatePartTable();
        updateAssociatedPartTable();
    }
}
