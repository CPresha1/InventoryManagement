package c482.inventory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This is the Product class, which has a dependency on Part
 */
public class Product{

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    private int productID, stock, min, max;
    private String name;
    private double price;

    public Product(int productID, int stock, int min, int max, String name, double price) {
        this.productID = productID;
        this.stock = stock;
        this.min = min;
        this.max = max;
        this.name = name;
        this.price = price;
    }

    public Product() {
        this(0, 0, 0, 0, null, 0.00);
    }

    /**
     * @return the productID
     */
    public int getProductID() {
        return productID;
    }

    /**
     * @param id the id to set
     */
    public void setProductID(int id) {
        this.productID = id;
    }

    /**
     * @return the stock
     */
    public int getStock() {
        return stock;
    }

    /**
     * @param stock the stock to set
     */
    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * @return the min
     */
    public int getMin() {
        return min;
    }

    /**
     * @param min the min to set
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * @return the max
     */
    public int getMax() {
        return max;
    }

    /**
     * @param max the max to set
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the price
     */
    public double getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setProductPrice(double price) {
        this.price = price;
    }

    /**
     * @param p adding parts to associated parts list
     */
    public void addAssociatedPart(ObservableList<Part> p){
        this.associatedParts.addAll(p);
    }

    /**
     * @return the associated Parts list
     */
    public ObservableList<Part> getAllAssociatedParts(){
        return associatedParts;
    }
}
