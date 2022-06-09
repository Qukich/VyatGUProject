package Main.Controllers;

import WorkingFunctions.DBwork;
import WorkingFunctions.Fill;
import WorkingFunctions.ShowWork;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class UpdateOrderController {
    @FXML
    private TextField numberSearch;

    @FXML
    private Button update;
    @FXML
    private Button furniture_show;

    @FXML
    private ComboBox<String> fio;
    @FXML
    private ComboBox<String> status;
    @FXML
    private ComboBox<String> furniture;

    @FXML
    private DatePicker openOrd;
    @FXML
    private DatePicker closedOrd;

    private DBwork db;

    @FXML
    void initialize() throws SQLException{
        db = new DBwork();
        Fill fill = new Fill();
        ShowWork sw = new ShowWork();
        List<String> furnitureList = fill.fillList("SELECT name_furniture FROM public.furniture");
        List<String> statusList = fill.fillList("SELECT status_order FROM public.status_order");
        List<String> fioList = fill.fillList("SELECT fio_client FROM public.client");

        furniture.setItems(FXCollections.observableArrayList(furnitureList));
        status.setItems(FXCollections.observableArrayList(statusList));
        fio.setItems(FXCollections.observableArrayList(fioList));

        Pattern p = Pattern.compile("(\\d+\\.?\\d*)?");
        numberSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) numberSearch.setText(oldValue);
        });

        furniture_show.setOnAction(actionEvent ->
                sw.showWindow("/Catalog/Furniture/MainWindowFurniture.fxml", "Мебель"));

        numberSearch.setOnKeyTyped(actionEvent -> {
            if(!Objects.equals(numberSearch.getText(), "")){
                String sql = "SELECT j1.fio_client, j2.status_order, data_order, data_closed_order, j3.name_furniture\n" +
                        "FROM public.ordering AS j0\n" +
                        "JOIN public.client AS j1 ON j1.id_client = j0.id_client\n" +
                        "JOIN public.status_order AS j2 ON j0.id_status_order = j2.id_status_order\n" +
                        "JOIN public.furniture AS j3 ON j0.id_furniture = j3.id_furniture\n" +
                        "WHERE id_order = " + numberSearch.getText() + ";";
                try {
                    ResultSet rs = db.Select(sql);
                    while (rs.next()){
                        fio.getSelectionModel().select(rs.getString(1));
                        status.getSelectionModel().select(rs.getString(2));
                        openOrd.setValue(rs.getDate(3).toLocalDate());
                        closedOrd.setValue(rs.getDate(4).toLocalDate());
                        furniture.getSelectionModel().select(rs.getString(5));
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else{
                fio.getSelectionModel().clearSelection();
                fio.setButtonCell(new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty) ;
                        if (empty || item == null) {
                            setText("Образование");
                        } else {
                            setText(item);
                        }
                    }
                });

                status.getSelectionModel().clearSelection();
                status.setButtonCell(new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty) ;
                        if (empty || item == null) {
                            setText("Образование");
                        } else {
                            setText(item);
                        }
                    }
                });

                furniture.getSelectionModel().clearSelection();
                furniture.setButtonCell(new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty) ;
                        if (empty || item == null) {
                            setText("Образование");
                        } else {
                            setText(item);
                        }
                    }
                });

                openOrd.setValue(null);
                closedOrd.setValue(null);
            }
        });

        update.setOnAction(actionEvent -> {
            db = new DBwork();
            ShowWork showWork = new ShowWork();
            StringBuilder sql = new StringBuilder();

            int furniture2 = furniture.getSelectionModel().getSelectedIndex() + 1;
            int status2 = status.getSelectionModel().getSelectedIndex() + 1;
            int fio2 = fio.getSelectionModel().getSelectedIndex() + 1;

            sql.append("UPDATE public.ordering SET ");

            if(fio2 > 0){
                sql.append("id_client = ").append(fio2).append(",");
            }

            if(status2 > 0){
                sql.append("id_status_order = ").append(status2).append(",");
            }

            if(openOrd.getValue() != null){
                sql.append("data_order = '").append(openOrd.getValue().toString()).append("',");
            }

            if(closedOrd.getValue() != null){
                sql.append("data_closed_order = '").append(closedOrd.getValue().toString()).append("',");
            }

            if(furniture2 > 0){
                sql.append("id_furniture = ").append(furniture2).append(",");
            }

            sql.setLength(sql.length() - 1);
            sql.append("WHERE id_order = ").append(numberSearch.getText()).append(";");

            try {
                db.Update(sql.toString());
            } catch (SQLException throwable){
                throwable.printStackTrace();
            }
            showWork.showInfo("Обновление заказа произошло успешно!", "");
            Stage stage = (Stage) update.getScene().getWindow();
            stage.close();
        });
    }
}
