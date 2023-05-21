package c482.inventory;

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
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * FUTURE ENHANCEMENT: The Company Name/Machine ID columns could be added to the main screen. Part/Product relationships could be shown as well
 * This class will control the main window the user first sees
 */
public class MainWindowController implements Initializable {


    @FXML private TableView<Part> partsTableView;
    @FXML private TableColumn<Part, Integer> partIDColumn;
    @FXML private TableColumn<Part, String> partNameColumn;
    @FXML private TableColumn<Part, Integer> partInvLevelColumn;
    @FXML private TableColumn<Part, Double> partCostColumn;
    @FXML private TextField partSearchArea;

    //Products Table
    @FXML private TableView<Product> productsTableView;
    @FXML private TableColumn<Product, Integer> productIDColumn;
    @FXML private TableColumn<Product, String> productNameColumn;
    @FXML private TableColumn<Product, Integer> productInvLevelColumn;
    @FXML private TableColumn<Product, Double> productCostColumn;
    @FXML private TextField productSearchArea;

    private Parent scene;

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
     * This method will update the view of the product table when search is used
     * @param event on clicking search product
     */
    public void searchProductButton(ActionEvent event) {
        String term = productSearchArea.getText();
        if (isNumeric(term)){
            int num = Integer.parseInt(term);
            ObservableList foundProducts = (ObservableList) Inventory.lookupProductID(num);
            if(foundProducts.isEmpty()) {
                MainWindowController.confirmDialog("None found", "Unable to find: " + term);
            } else {
                productsTableView.setItems(foundProducts);
            }
        }
        else{
            ObservableList foundProducts = Inventory.lookupPart(term);
            if(foundProducts.isEmpty()) {
                MainWindowController.confirmDialog("None found", "Unable to find: " + term);
            } else {
                productsTableView.setItems(foundProducts);
            }
        }
    }
    /**
     * This method will take the user to the modify part screen when Modify is clicked on the part table
     * @param event on clicking modify part
     */
    public void modifypartbuttonpushed(ActionEvent event){
        try {
            Part selectedPart = partsTableView.getSelectionModel().getSelectedItem();
            if (selectedPart == null) {
                return;
            }
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyPart.fxml"));
            scene = loader.load();
            ModifyPartController controller = loader.getController();
            controller.setParts(selectedPart);
            stage.setTitle("Modify Part");
            stage.setScene(new Scene(scene));
            stage.show();

        } catch(NumberFormatException | IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Min value must be number");
            alert.showAndWait();
            return;
        }
    }

    /**
     * This method will take the user to the modify product screen when Modify is clicked on the product table
     * @param event on clicking modify product
     */
    public void modifyproductbuttonpushed(ActionEvent event){
        try {
            Product selectedProduct = productsTableView.getSelectionModel().getSelectedItem();
            if (selectedProduct == null) {
                return;
            }
            Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("ModifyProduct.fxml"));
            scene = loader.load();
            ModifyProductController controller = loader.getController();
            controller.setProduct(selectedProduct);
            stage.setTitle("Modify Product");
            stage.setScene(new Scene(scene));
            stage.show();

        } catch(NumberFormatException | IOException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Min value must be number");
            alert.showAndWait();
            return;
        }
    }

    /**
     * This method will update the view of the part table when search is used
     * @param event on clicking search part
     */
    public void searchPartButton(ActionEvent event) {
        String term = partSearchArea.getText();
        if (isNumeric(term)){
            int num = Integer.parseInt(term);
            ObservableList foundParts = Inventory.lookupPartID(num);
            if(foundParts.isEmpty()) {
                MainWindowController.confirmDialog("None found", "Unable to find: " + term);
            } else {
                partsTableView.setItems(foundParts);
            }
        }
        else{
            ObservableList foundParts = Inventory.lookupPart(term);
            if(foundParts.isEmpty()) {
                MainWindowController.confirmDialog("None found", "Unable to find: " + term);
            } else {
                partsTableView.setItems(foundParts);
            }
        }
    }

    /**
     * Will direct user to Add Product screen on pressing Add on Product table
     * @param event on clikcing add product
     */
    @FXML public void addProductButtonPushed(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("AddProduct.fxml"));
        stage.setTitle("Add Product");
        stage.setScene(new Scene(scene));
        stage.show();
    }


    /**
     * This method will validate delete or delete a selected part
     * @param event on clicking delete part
     */
    public void deletePartButton(ActionEvent event) {
        if (partsTableView.getSelectionModel().isEmpty()){
            info("Warning!", "No selection","Choose a part!");
            return;
        }
        if (confirmDialog("Warning!", "Delete part?")){
            int selectedPart = partsTableView.getSelectionModel().getSelectedIndex();
            partsTableView.getItems().remove(selectedPart);
        }
    }

    /**
     * This method will validate delete or delete a selected product
     * @param event on clicking delete product
     */
    public void deleteProductButton(ActionEvent event) {
        Product temp = productsTableView.getSelectionModel().getSelectedItem();
        if (productsTableView.getSelectionModel().isEmpty()){
            info("Warning!", "No selection","Choose a product!");
            return;
        }
        if (confirmDialog("Warning!", "Delete product?")){
            int selectedPart = productsTableView.getSelectionModel().getSelectedIndex();
            if(temp.getAllAssociatedParts() != null){
                MainWindowController.confirmDialog("Error!", "Cannot delete products with parts associated.");
            }
            else{
                productsTableView.getItems().remove(selectedPart);
            }
        }
    }

    /**
     * Confirmation for user actions
     * @param title of alert
     * @param content of alert
     */
    static boolean confirmDialog(String title, String content){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText("Confirm");
        alert.setContentText(content);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            return true;
        }
        else {
            return false;
        }
    }
    /**
     * This method will take the user to the Add Part screen when Add is clicked
     * @param event on clicking add part
     */
    public void addpartbuttonpushed(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("AddPart.fxml"));
        stage.setTitle("Add Part");
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Dialogue for user clarity
     * @param title of alert
     * @param header of alert
     * @param content of alert
     */
    static void info(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }



    /**
     * Exiting the program
     * @param event
     */
    @FXML public void exitButton(ActionEvent event) throws IOException{
        confirmDialog("Exit", "Are you sure you would like to exit the program?");
        {
            System.exit(0);
        }
    }


    /**
     *LOGICAL ERROR productID HAS to say productID in PropertyValueFactory!!!
     *This method uses the PropertyValueFactory to fill tables with inputs
     * @param resources
     * @param location
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        partsTableView.setItems(Inventory.getAllParts());
        partIDColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<Part, String>("name"));
        partInvLevelColumn.setCellValueFactory(new PropertyValueFactory<Part, Integer>("stock"));
        partCostColumn.setCellValueFactory(new PropertyValueFactory<Part, Double>("price"));

        productsTableView.setItems((Inventory.getAllProducts()));
        productIDColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("productID"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<Product, String>("name"));
        productInvLevelColumn.setCellValueFactory(new PropertyValueFactory<Product, Integer>("stock"));
        productCostColumn.setCellValueFactory(new PropertyValueFactory<Product, Double>("price"));


    }

}