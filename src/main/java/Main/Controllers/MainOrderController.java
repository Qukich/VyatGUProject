package Main.Controllers;

import Login.LoginController;
import Main.MainStruct;
import WorkingFunctions.CheckTextField;
import WorkingFunctions.ConstructName;
import WorkingFunctions.DBwork;
import WorkingFunctions.ShowWork;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class MainOrderController {
    @FXML
    private Label time;
    @FXML
    private Label date;

    @FXML
    private Menu menuEmployee;

    @FXML
    private Menu accessories;
    @FXML
    private Menu furniture;

    @FXML
    private Menu upholstery;

    @FXML
    private Menu filler;

    @FXML
    private Menu layout;

    @FXML
    private MenuItem openClient;
    @FXML
    private MenuItem openEmployee;

    @FXML
    private MenuItem changeUser;
    @FXML
    private MenuItem exit;

    @FXML
    private TableView<MainStruct> table;
    @FXML
    private TableColumn<MainStruct, Integer> id;
    @FXML
    private TableColumn<MainStruct, String> fio;
    @FXML
    private TableColumn<MainStruct, String> status;
    @FXML
    private TableColumn<MainStruct, String> furn;
    @FXML
    private TableColumn<MainStruct, Date> date_ord;
    @FXML
    private TableColumn<MainStruct, Date> date_close;

    @FXML
    private Button search;
    @FXML
    private Button refresh;
    @FXML
    private Button addOrd;
    @FXML
    private Button updateOrd;
    @FXML
    private Button deleteOrd;

    @FXML
    private TextField searchFild;

    private ShowWork sw;
    private DBwork db;

    LoginController lg = ConstructName.getInstance().getLogin();

    ObservableList<MainStruct> MainList = FXCollections.observableArrayList();

    public static boolean stop = false;

    @FXML
    void initialize() throws SQLException {
        sw = new ShowWork();
        db = new DBwork();
        ConstructName.getInstance().setMainOrderController(this);


        if(lg.id_role == 1 || lg.id_role == 4){
            menuEmployee.setVisible(false);
        }

        if(lg.id_role == 4){
            addOrd.setVisible(false);
            updateOrd.setVisible(false);
            deleteOrd.setVisible(false);
            openClient.setVisible(false);
        }

        TimeNow();

        openEmployee.setOnAction(actionEvent ->
                sw.showWindow("/Employee/MainWindowEmployee.fxml", "Сотрудники"));

        upholstery.setOnAction(actionEvent ->
                sw.showWindow("/Catalog/Upholstery/UpholsteryMainWindow.fxml", "Обивка"));

        filler.setOnAction(actionEvent ->
                sw.showWindow("/Catalog/Filler/FillerMainWindow.fxml", "Наполнитель"));

        accessories.setOnAction(actionEvent ->
                sw.showWindow("/Catalog/AccessoriesControllers/MainAccessoriesWindow.fxml", "Фурнитура"));

        layout.setOnAction(actionEvent ->
                sw.showWindow("/Catalog/Layout/MainLayoutWindow.fxml", "Макеты"));

        furniture.setOnAction(actionEvent ->
                sw.showWindow("/Catalog/Furniture/MainWindowFurniture.fxml", "Мебель"));

        openClient.setOnAction(actionEvent ->
                sw.showWindow("/Client/MainClientWindow.fxml", "Клиент"));

        changeUser.setOnAction(actionEvent ->{
            sw.showWindow("/Login/LoginApp.fxml", "Вход");
            Stage stage = (Stage) search.getScene().getWindow();
            stage.close();
        });

        exit.setOnAction(actionEvent ->{
            Stage stage = (Stage) search.getScene().getWindow();
            stage.close();
            stop = true;
        });


        String sqlSelect = """
                SELECT id_order, j1.fio_client, j3.status_order,\s
                j2.name_furniture, data_order, data_closed_order FROM public.ordering AS j0
                JOIN public.client AS j1 ON j0.id_client = j1.id_client
                JOIN public.furniture AS j2 ON j0.id_furniture = j2.id_furniture
                JOIN public.status_order AS j3 ON j0.id_status_order = j3.id_status_order""";


        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        fio.setCellValueFactory(new PropertyValueFactory<>("fio"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
        furn.setCellValueFactory(new PropertyValueFactory<>("furn"));
        date_ord.setCellValueFactory(new PropertyValueFactory<>("date_ord"));
        date_close.setCellValueFactory(new PropertyValueFactory<>("date_close"));

        ResultSet rs2 = db.Select(sqlSelect);
        while (rs2.next()){
            MainList.add(new MainStruct(
                    rs2.getInt(1),
                    rs2.getString(2),
                    rs2.getString(3),
                    rs2.getString(4),
                    rs2.getDate(5),
                    rs2.getDate(6)
            ));
            table.setItems(MainList);
        }

        search.setOnAction(actionEvent -> {
            if(!Objects.equals(searchFild.getText(), "")){
                CheckTextField ctf = new CheckTextField();
                if(ctf.checkNumb(searchFild.getText(), "поиске")){
                    String filter = sqlSelect + "\nWHERE id_order = " + searchFild.getText() + ";";
                    try {
                        table.getItems().clear();
                        ResultSet rs3 = db.Select(filter);
                        while (rs3.next()){
                            MainList.add(new MainStruct(
                                    rs3.getInt(1),
                                    rs3.getString(2),
                                    rs3.getString(3),
                                    rs3.getString(4),
                                    rs3.getDate(5),
                                    rs3.getDate(6)
                            ));
                            table.setItems(MainList);
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }

            } else{
                sw.showError("Введите номер заказа для поиска", "");
            }
        });

        refresh.setOnAction(actionEvent -> {
            try {
                table.getItems().clear();
                ResultSet rs3 = db.Select(sqlSelect);
                while (rs3.next()){
                    MainList.add(new MainStruct(
                            rs3.getInt(1),
                            rs3.getString(2),
                            rs3.getString(3),
                            rs3.getString(4),
                            rs3.getDate(5),
                            rs3.getDate(6)
                    ));
                    table.setItems(MainList);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        addOrd.setOnAction(actionEvent ->
                sw.showWindow("/Main/AddOrderWindow.fxml", "Добавить заказ"));

        deleteOrd.setOnAction(actionEvent ->
                sw.showWindow("/Main/DeleteOrderWindow.fxml", "Удалить заказ"));

        updateOrd.setOnAction(actionEvent ->
                sw.showWindow("/Main/UpdateOrderWindow.fxml", "Добавить заказ"));
    }
    public void TimeNow() {
        Thread thread = new Thread(() -> {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            while (!stop){
                final String timezone = sdf.format(new Date());
                Platform.runLater(() ->
                        time.setText(timezone));
                try {
                    Thread.sleep(1000);
                } catch (Exception e){
                    System.out.println(e);
                }
            }
        });
        Thread thread2 = new Thread(() -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            while (!stop){
                final String timezone = sdf.format(new Date());
                Platform.runLater(() ->{
                    date.setText(timezone);
                });
                try {
                    Thread.sleep(1000);
                } catch (Exception e){
                    System.out.println(e);
                }
            }
        });
        thread.start();
        thread2.start();
    }
}
