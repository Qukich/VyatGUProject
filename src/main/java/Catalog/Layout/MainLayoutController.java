package Catalog.Layout;

import Catalog.Struct.LayoutStruct;
import Login.LoginController;
import WorkingFunctions.ConstructName;
import WorkingFunctions.DBwork;
import WorkingFunctions.ShowWork;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainLayoutController {
    @FXML
    private ImageView image;

    @FXML
    private Button add;
    @FXML
    private Button delete;
    @FXML
    private Button update;
    @FXML
    private Button refresh;

    @FXML
    private Label nameLayout;

    @FXML
    private TableView<LayoutStruct> table;
    @FXML
    private TableColumn<LayoutStruct, String> name;

    private DBwork db;
    private ShowWork sw;

    ObservableList<LayoutStruct> LayoutList = FXCollections.observableArrayList();

    LoginController lg = ConstructName.getInstance().getLogin();

    @FXML
    void initialize() throws SQLException{
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        db = new DBwork();
        sw = new ShowWork();

        ConstructName.getInstance().setLay(this);

        String sqlSelect = "SELECT name_layout FROM public.layout";

        ResultSet rs = db.Select(sqlSelect);
        while (rs.next()){
            LayoutList.add(new LayoutStruct(
                    rs.getString(1)
            ));
            table.setItems(LayoutList);
        }
        addButtonToTable();

        if(lg.id_role == 1){
            add.setVisible(false);
            update.setVisible(false);
            delete.setVisible(false);
        }

        add.setOnAction(actionEvent ->
                sw.showWindow("/Catalog/Layout/AddLayoutWindow.fxml", "Добавление макета"));

        update.setOnAction(actionEvent ->
                sw.showWindow("/Catalog/Layout/UpdateLayoutWindow.fxml", "Изменение макета"));

        delete.setOnAction(actionEvent ->
                sw.showWindow("/Catalog/Layout/DeleteLayoutWindow.fxml", "Удаление макета"));

        refresh.setOnAction(actionEvent -> {
            try {
                table.getItems().clear();
                ResultSet rs1 = db.Select(sqlSelect);
                while (rs1.next()){
                    LayoutList.add(new LayoutStruct(
                            rs1.getString(1)
                    ));
                    table.setItems(LayoutList);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void addButtonToTable() {
        TableColumn colBtn = new TableColumn("Фотография");

        Callback<TableColumn<LayoutStruct, Void>, TableCell<LayoutStruct, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<LayoutStruct, Void> call(final TableColumn<LayoutStruct, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Показать");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            db = new DBwork();
                            LayoutStruct data = getTableView().getItems().get(getIndex());
                            String name_tmp = data.getName();
                            nameLayout.setText(name_tmp);
                            String path = "";
                            try {
                                ResultSet rs = db.Select("SELECT path_to_image_layout FROM public.layout WHERE name_layout = '" + name_tmp + "';");
                                while (rs.next()) {
                                    path = rs.getString(1);
                                }
                                File file = new File(path);
                                image.setImage(new Image(file.toURI().toString()));
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };

        colBtn.setCellFactory(cellFactory);

        table.getColumns().add(colBtn);
    }
}
