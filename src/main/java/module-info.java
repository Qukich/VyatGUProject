module Login {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    exports Employee.Controller;
    opens Employee.Controller to javafx.fxml;

    exports WorkingFunctions;
    opens WorkingFunctions to javafx.fxml;

    exports Employee.Struct;
    opens Employee.Struct to javafx.fxml;

    exports Login;
    opens Login to javafx.fxml;

    exports Main;
    opens Main to javafx.fxml;

    exports Catalog.UpholsteryControllers;
    opens Catalog.UpholsteryControllers to javafx.fxml;

    exports Catalog.Struct;
    opens Catalog.Struct to javafx.fxml;

    exports Catalog.FillerControllers;
    opens Catalog.FillerControllers to javafx.fxml;

    exports Catalog.AccessoriesControllers;
    opens Catalog.AccessoriesControllers to javafx.fxml;

    exports Catalog.Layout;
    opens Catalog.Layout to javafx.fxml;

    exports Catalog.Furniture;
    opens Catalog.Furniture to javafx.fxml;

    exports Client.Controllers;
    opens Client.Controllers to javafx.fxml;

    opens Client.Struct;
    exports Client.Struct to javafx.fxml;

    opens Main.Controllers;
    exports Main.Controllers to javafx.fxml;
}