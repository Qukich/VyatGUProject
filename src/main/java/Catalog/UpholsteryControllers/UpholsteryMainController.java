package Catalog.UpholsteryControllers;

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

public class UpholsteryMainController {

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

    private DBwork db;
    private ShowWork sw;

    ObservableList<GeneralStruct> UnposhList = FXCollections.observableArrayList();

    LoginController lg = ConstructName.getInstance().getLogin();

    @FXML
    void initialize() throws SQLException {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        stock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        db = new DBwork();
        sw = new ShowWork();
        ConstructName.getInstance().setUph(this);

        String sqlSelect = "SELECT name_upholstery_material, price_upholstery_material, j1.name_unit, count_unit FROM public.upholstery AS j0\n" +
                "INNER JOIN public.Unit_Area AS j1 ON j0.id_unit = j1.id_unit;";

        ResultSet rs = db.Select(sqlSelect);
        while (rs.next()){
            UnposhList.add(new GeneralStruct(
                    rs.getString(1),
                    rs.getDouble(2),
                    "" + rs.getString(4) + " " + rs.getString(3)
            ));
            table.setItems(UnposhList);
        }

        if(lg.id_role == 1){
            add.setVisible(false);
            update.setVisible(false);
            delete.setVisible(false);
        }

        refresh.setOnAction(actionEvent -> {
            try {
                table.getItems().clear();
                ResultSet rs1 = db.Select(sqlSelect);
                while (rs1.next()){
                    UnposhList.add(new GeneralStruct(
                            rs1.getString(1),
                            rs1.getDouble(2),
                            "" + rs1.getString(4) + " " + rs1.getString(3)
                    ));
                    table.setItems(UnposhList);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        add.setOnAction(actionEvent ->
                sw.showWindow("/Catalog/Upholstery/AddUpholsteryWindow.fxml", "Добавление обивки"));

        update.setOnAction(actionEvent ->
                sw.showWindow("/Catalog/Upholstery/UpdateUpholsteryWindow.fxml", "Изменение обивки"));

        delete.setOnAction(actionEvent ->
                sw.showWindow("/Catalog/Upholstery/DeleteUpholsteryWindow.fxml", "Удаление обивки"));
    }

}
