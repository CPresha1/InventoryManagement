package c482.inventory;

/** This class is for Outsourced parts derived from Part */
public class Outsourced extends Part{
    private String companyName;
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);

        this.companyName = companyName;
    }
    /**
     * Will update the parts table with allParts
     * @return the companyName
     */
    public String getCompanyName(){
        return companyName;
    }
    /**
     * Will update the parts table with allParts
     * @param companyName to set
     */
    public void setCompanyName(String companyName){
        this.companyName = companyName;
    }
}
