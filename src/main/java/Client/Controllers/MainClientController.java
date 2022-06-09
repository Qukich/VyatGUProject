package Client.Controllers;

import Client.Struct.ClientStruct;
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

public class MainClientController {
    @FXML
    private Button add;
    @FXML
    private Button refresh;
    @FXML
    private Button delete;
    @FXML
    private Button update;

    @FXML
    private TableView<ClientStruct> table;
    @FXML
    private TableColumn<ClientStruct, String> name;
    @FXML
    private TableColumn<ClientStruct, Integer> card;
    @FXML
    private TableColumn<ClientStruct, Integer> bonusAmount;
    @FXML
    private TableColumn<ClientStruct, String> phone;

    ObservableList<ClientStruct> ClientList = FXCollections.observableArrayList();
    private DBwork db;
    private ShowWork showWork;

    @FXML
    void initialize() throws SQLException {
        db = new DBwork();
        showWork = new ShowWork();

        String sqlSelect = "SELECT fio_client, phone_client, j1.number_discount_card, j1.bonus_amount_card FROM public.client AS j0\n" +
                "INNER JOIN public.discount_card AS j1 ON j0.id_discount_card = j1.id_discount_card";

        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        card.setCellValueFactory(new PropertyValueFactory<>("card"));
        bonusAmount.setCellValueFactory(new PropertyValueFactory<>("bonusAmount"));
        phone.setCellValueFactory(new PropertyValueFactory<>("phone"));

        ResultSet rs = db.Select(sqlSelect);
        while (rs.next()){
            ClientList.add(new ClientStruct(
                    rs.getString(1),
                    rs.getString(2),
                    rs.getInt(3),
                    rs.getInt(4)
            ));
            table.setItems(ClientList);
        }

        refresh.setOnAction(actionEvent -> {
            try {
                table.getItems().clear();
                ResultSet rs2 = db.Select(sqlSelect);
                while (rs2.next()){
                    ClientList.add(new ClientStruct(
                            rs2.getString(1),
                            rs2.getString(2),
                            rs2.getInt(3),
                            rs2.getInt(4)
                    ));
                    table.setItems(ClientList);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        add.setOnAction(actionEvent ->
                showWork.showWindow("/Client/AddClientWindow.fxml", "Добавить клиента"));
        update.setOnAction(actionEvent ->
                showWork.showWindow("/Client/UpdateClientWindow.fxml", "Изменить клиента"));
        delete.setOnAction(actionEvent ->
                showWork.showWindow("/Client/DeleteClientWindow.fxml", "Удалить клиента"));
    }
}
