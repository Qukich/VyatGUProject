package Catalog.Furniture;

import Catalog.Struct.FurnitureStruct;
import Login.LoginController;
import WorkingFunctions.ConstructName;
import WorkingFunctions.DBwork;
import WorkingFunctions.Fill;
import WorkingFunctions.ShowWork;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class MainFurnitureController {

    @FXML
    private TableView<FurnitureStruct> table;
    @FXML
    private TableColumn<FurnitureStruct, String> name_Tabl;
    @FXML
    private TableColumn<FurnitureStruct, String> tip_Tabl;
    @FXML
    private TableColumn<FurnitureStruct, String> layout_Tabl;
    @FXML
    private TableColumn<FurnitureStruct, Integer> height_Tabl;
    @FXML
    private TableColumn<FurnitureStruct, Integer> width_Tabl;
    @FXML
    private TableColumn<FurnitureStruct, Integer> length_Tabl;
    @FXML
    private TableColumn<FurnitureStruct, String> filler_Tabl;
    @FXML
    private TableColumn<FurnitureStruct, String> upholstery_Tabl;
    @FXML
    private TableColumn<FurnitureStruct, String> accessories_Tabl;
    @FXML
    private TableColumn<FurnitureStruct, Double> price_Tabl;

    @FXML
    private Button add;
    @FXML
    private Button refresh;
    @FXML
    private Button delete;
    @FXML
    private Button update;

    @FXML
    private Button search;
    @FXML
    private Button filter;
    @FXML
    private Button clearFilter;

    @FXML
    private TextField FildSearch;

    @FXML
    private ComboBox<String> comboUnposh;
    @FXML
    private ComboBox<String> comboTip;
    @FXML
    private ComboBox<String> comboLayout;

    ObservableList<FurnitureStruct> FurnitureList = FXCollections.observableArrayList();

    LoginController lg = ConstructName.getInstance().getLogin();

    private DBwork db;
    private ShowWork sw;

    @FXML
    void initialize() throws SQLException{
        db = new DBwork();
        Fill fill = new Fill();
        sw = new ShowWork();
        ConstructName.getInstance().setFurn(this);

        comboLayout.visibleProperty().setValue(false);
        comboTip.visibleProperty().setValue(false);
        comboUnposh.visibleProperty().setValue(false);

        String sqlSelect = """
                SELECT name_furniture, j1.description_type_furniture, j2.name_layout, height, width, length,\s
                j3.name_upholstery_material, j4.name_filler, j5.description_accessories, price_furniture
                FROM public.furniture AS j0
                INNER JOIN public.type_furniture AS j1 ON j0.id_type_furniture = j1.id_type_furniture
                INNER JOIN public.layout AS j2 ON j0.id_layout = j2.id_layout
                INNER JOIN public.upholstery AS j3 ON j0.id_upholstery_material = j3.id_upholstery_material
                INNER JOIN public.filler AS j4 ON j0.id_filler = j4.id_filler
                INNER JOIN public.accessories AS j5 ON j0.id_accessories = j5.id_accessories""";

        List<String> unposh = fill.fillList("SELECT name_upholstery_material FROM public.upholstery");
        List<String> layoutList = fill.fillList("SELECT name_layout FROM public.layout");
        List<String> tipList = fill.fillList("SELECT description_type_furniture FROM public.type_furniture");

        comboLayout.setItems(FXCollections.observableArrayList(layoutList));
        comboUnposh.setItems(FXCollections.observableArrayList(unposh));
        comboTip.setItems(FXCollections.observableArrayList(tipList));

        name_Tabl.setCellValueFactory(new PropertyValueFactory<>("name_Tabl"));
        tip_Tabl.setCellValueFactory(new PropertyValueFactory<>("tip_Tabl"));
        layout_Tabl.setCellValueFactory(new PropertyValueFactory<>("layout_Tabl"));
        height_Tabl.setCellValueFactory(new PropertyValueFactory<>("height_Tabl"));
        width_Tabl.setCellValueFactory(new PropertyValueFactory<>("width_Tabl"));
        length_Tabl.setCellValueFactory(new PropertyValueFactory<>("length_Tabl"));
        filler_Tabl.setCellValueFactory(new PropertyValueFactory<>("filler_Tabl"));
        upholstery_Tabl.setCellValueFactory(new PropertyValueFactory<>("upholstery_Tabl"));
        accessories_Tabl.setCellValueFactory(new PropertyValueFactory<>("accessories_Tabl"));
        price_Tabl.setCellValueFactory(new PropertyValueFactory<>("price_Tabl"));

        if(lg.id_role == 1){
            add.setVisible(false);
            update.setVisible(false);
            delete.setVisible(false);
        }

        ResultSet rs = db.Select(sqlSelect);
        while (rs.next()){
            FurnitureList.add(new FurnitureStruct(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getInt(4),
                    rs.getInt(5),
                    rs.getInt(6),
                    rs.getString(7),
                    rs.getString(8),
                    rs.getString(9),
                    rs.getDouble(10)

            ));
            table.setItems(FurnitureList);
        }

        refresh.setOnAction(actionEvent -> {
            try {
                table.getItems().clear();
                ResultSet rs2 = db.Select(sqlSelect);
                while (rs2.next()){
                    FurnitureList.add(new FurnitureStruct(
                            rs2.getString(1),
                            rs2.getString(2),
                            rs2.getString(3),
                            rs2.getInt(4),
                            rs2.getInt(5),
                            rs2.getInt(6),
                            rs2.getString(7),
                            rs2.getString(8),
                            rs2.getString(9),
                            rs2.getDouble(10)
                    ));
                    table.setItems(FurnitureList);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        filter.setOnAction(actionEvent -> {
            if (!comboLayout.visibleProperty().getValue()){
                comboLayout.visibleProperty().setValue(true);
                comboUnposh.visibleProperty().setValue(true);
                comboTip.visibleProperty().setValue(true);
            } else {
                comboLayout.visibleProperty().setValue(false);
                comboUnposh.visibleProperty().setValue(false);
                comboTip.visibleProperty().setValue(false);
            }
        });

        clearFilter.setOnAction(actionEvent -> {
            comboLayout.getSelectionModel().clearSelection();
            comboUnposh.getSelectionModel().clearSelection();
            comboTip.getSelectionModel().clearSelection();
            comboLayout.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText("Макет");
                    } else {
                        setText(item);
                    }
                }
            });
            comboTip.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty) ;
                    if (empty || item == null) {
                        setText("Тип мебели");
                    } else {
                        setText(item);
                    }
                }
            });
            comboUnposh.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty) ;
                    if (empty || item == null) {
                        setText("Обивка");
                    } else {
                        setText(item);
                    }
                }
            });
        });

        search.setOnAction(actionEvent -> {
            StringBuilder filter = new StringBuilder();
            int Unposh = comboUnposh.getSelectionModel().getSelectedIndex() + 1;
            int Tip = comboTip.getSelectionModel().getSelectedIndex() + 1;
            int Lay = comboLayout.getSelectionModel().getSelectedIndex() + 1;
            String header = "Ошибка в поиске";
            boolean flag2 = false;

            if ((Objects.equals(FildSearch.getText(), "")) && (Unposh == 0) && (Tip == 0) && (Lay == 0)){
                sw.showError(header, "Не заполнено ни одно поле для поиска");
                return;
            }

            filter.append("\nWHERE ");
            if(Unposh > 0) {
                filter.append("j3.id_upholstery_material =").append(Unposh).append(" and ");
                flag2 = true;
            }
            if(Tip > 0) {
                filter.append("j1.id_type_furniture =").append(Tip).append(" and ");
                flag2 = true;
            }
            if(Lay > 0) {
                filter.append("j2.id_layout =").append(Lay).append(" and ");
                flag2 = true;
            }
            if(flag2){
                filter.setLength(filter.length() - 4);
                if(FildSearch.getLength() != 0){
                    filter.append("\nUNION\n").append(sqlSelect).append("\nWHERE name_furniture LIKE '").append(FildSearch.getText()).append("%'");
                }
            }
            else{
                filter.append("name_furniture LIKE '").append(FildSearch.getText()).append("%'");
            }
            String sqlTemp = sqlSelect + filter;
            System.out.println(sqlTemp);
            try {
                table.getItems().clear();
                ResultSet rs2 = db.Select(sqlTemp);
                while (rs2.next()){
                    FurnitureList.add(new FurnitureStruct(
                            rs2.getString(1),
                            rs2.getString(2),
                            rs2.getString(3),
                            rs2.getInt(4),
                            rs2.getInt(5),
                            rs2.getInt(6),
                            rs2.getString(7),
                            rs2.getString(8),
                            rs2.getString(9),
                            rs2.getDouble(10)
                    ));
                    table.setItems(FurnitureList);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        add.setOnAction(actionEvent ->
                sw.showWindow("/Catalog/Furniture/AddFurnitureWindow.fxml", "Добавить мебель"));

        update.setOnAction(actionEvent ->
                sw.showWindow("/Catalog/Furniture/UpdateFurnitureWindow.fxml", "Обновить мебель"));

        delete.setOnAction(actionEvent ->
                sw.showWindow("/Catalog/Furniture/DeleteFurnitureWindow.fxml", "Обновить мебель"));
    }
}
