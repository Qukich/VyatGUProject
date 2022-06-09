package Catalog.AccessoriesControllers;

import Catalog.Struct.GeneralStruct;
import Login.LoginController;
import WorkingFunctions.ConstructName;
import WorkingFunctions.DBwork;
import WorkingFunctions.ShowWork;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MainAccessoriesController {
    @FXML
    private TableView<GeneralStruct> table;
    @FXML
    private TableColumn<GeneralStruct, String> name;
    @FXML
    private TableColumn<GeneralStruct, Double> price;
    @FXML
    private TableColumn<GeneralStruct, String> stock;

    @FXML
    private Button add;
    @FXML
    private Button delete;
    @FXML
    private Button update;
    @FXML
    private Button refresh;

    ObservableList<GeneralStruct> AccessoriesList = FXCollections.observableArrayList();
    LoginController lg = ConstructName.getInstance().getLogin();

    @FXML
    void initialize() throws SQLException {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        DBwork db = new DBwork();
        ShowWork sw = new ShowWork();
        ConstructName.getInstance().setAccs(this);

        String sqlSelect = "SELECT description_Accessories, price_Accessories, quantity_Accessories FROM public.accessories";

        if(lg.id_role == 1){
            add.setVisible(false);
            update.setVisible(false);
            delete.setVisible(false);
        }

        ResultSet rs = db.Select(sqlSelect);
        while (rs.next()){
            AccessoriesList.add(new GeneralStruct(
                    rs.getString(1),
                    rs.getDouble(2),
                    String.valueOf(rs.getInt(3))
            ));
            table.setItems(AccessoriesList);
        }

        refresh.setOnAction(actionEvent -> {
            try {
                table.getItems().clear();
                ResultSet rs1 = db.Select(sqlSelect);
                while (rs1.next()){
                    AccessoriesList.add(new GeneralStruct(
                            rs1.getString(1),
                            rs1.getDouble(2),
                            String.valueOf(rs1.getInt(3))
                    ));
                    table.setItems(AccessoriesList);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        add.setOnAction(actionEvent ->
                sw.showWindow("/Catalog/AccessoriesControllers/AddAccessoriesWindow.fxml", "Добавление наполнителя"));

        update.setOnAction(actionEvent ->
                sw.showWindow("/Catalog/AccessoriesControllers/UpdateAccessoriesWindow.fxml", "Изменение наполнителя"));

        delete.setOnAction(actionEvent ->
                sw.showWindow("/Catalog/AccessoriesControllers/DeleteAccessoriesWindow.fxml", "Удаление наполнителя"));
    }
}
