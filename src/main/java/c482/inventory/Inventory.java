package c482.inventory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class will control adding, searching, updating and deleting parts/products.
 */
public class Inventory {
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * This method will be used to add parts to the allParts list
     * @param newPart the part to add
     */
    public static void addPart(Part newPart){
        allParts.add(newPart);
    }

    /**
     * @return allParts
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * This method will be used to add products to the allProducts list
     * @param newProduct the product to add
     */
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    /**
     * @return allProducts
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * This method is for searching products by ID
     * @param temp the ID to search
     */
    public static ObservableList<Product> lookupProductID(int temp) {
        ObservableList<Product> foundProducts = FXCollections.observableArrayList();
        if(temp == 0) {
            foundProducts = allProducts;
        }
        else{
            for (Product allProducts : allProducts) {
                if (allProducts.getProductID() == temp) {
                    foundProducts.add(allProducts);
                }
            }
        }
        return foundProducts;
    }

    /**
     * This method is for searching parts by ID
     * @param temp the ID to search
     */
    public static ObservableList lookupPartID(int temp) {
        ObservableList<Part> foundParts = FXCollections.observableArrayList();
        if(temp == 0) {
            foundParts = allParts;
        }
        else{
            for (Part allPart : allParts) {
                if (allPart.getId() == temp) {
                    foundParts.add(allPart);
                }
            }
        }
        return foundParts;
    }

    /**
     * This method will search products by name
     * @param searchProductName the name to search
     */
    public static ObservableList lookupProduct(String searchProductName){
        ObservableList<Product> foundProducts = FXCollections.observableArrayList();
        if(searchProductName.length() == 0) {
            foundProducts = allProducts;
        }
        else {
            for (int i = 0; i < allProducts.size(); i++) {
                if (allProducts.get(i).getName().toLowerCase().contains(searchProductName.toLowerCase())) {
                    foundProducts.add(allProducts.get(i));
                }
            }
        }
        return foundProducts;
    }

    /**
     * This method will search parts by name
     * @param searchPartName the name to search
     */
    public static ObservableList lookupPart(String searchPartName){
        ObservableList<Part> foundParts = FXCollections.observableArrayList();

        if(searchPartName.length() == 0) {
            foundParts = allParts;
        }
        else {
            for (int i = 0; i < allParts.size(); i++) {
                if (allParts.get(i).getName().toLowerCase().contains(searchPartName.toLowerCase())) {
                    foundParts.add(allParts.get(i));
                }
            }
        }

        return foundParts;
    }
    /**
     * This method will update the selected part
     * @param index to reference
     * @param selectedPart selected part
     */
    public static void updatePart(int index, Part selectedPart){
        allParts.set(index, selectedPart);
    }

    /**
     * This method will update the selected product
     * @param index to reference
     * @param newProduct selected product
     */
    public static void updateProduct(int index, Product newProduct){
        allProducts.set(index, newProduct);
    }

    /**
     * This method will delete the selected part
     * @param selectedPart to delete
     */
    public static boolean deletePart(Part selectedPart){
        return allParts.remove(selectedPart);
    }

    /**
     * This method will delete the selected product
     * @param selectedProduct to delete
     */
    public static boolean deleteProduct(Product selectedProduct){
        return allProducts.remove(selectedProduct);
    }
}
