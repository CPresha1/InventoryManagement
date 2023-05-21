module c482.inventory {
    requires javafx.controls;
    requires javafx.fxml;


    opens c482.inventory to javafx.fxml;
    exports c482.inventory;
}