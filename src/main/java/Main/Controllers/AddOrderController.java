package Main.Controllers;

import WorkingFunctions.ConstructName;
import WorkingFunctions.DBwork;
import WorkingFunctions.Fill;
import WorkingFunctions.ShowWork;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;

public class AddOrderController {
    @FXML
    private ComboBox<String> fio;

    @FXML
    private Label bonus;

    @FXML
    private Button add;
    @FXML
    private Button furniture_show;
    @FXML
    private Button addComment;

    @FXML
    private DatePicker openOrd;
    @FXML
    private DatePicker closedOrd;

    @FXML
    private ComboBox<String> furniture;

    @FXML
    private TextField price;

    private final String name_bonus = "Количество бонусов за покупку:";
    private int count_bonus = 0;

    CommentOrderController comment = ConstructName.getInstance().getComment();

    @FXML
    void initialize() throws SQLException {
        Fill fill = new Fill();
        ShowWork sw = new ShowWork();
        DBwork db = new DBwork();
        price.setEditable(false);
        ConstructName.getInstance().setAddOrd(this);

        List<String> furList = fill.fillList("SELECT name_furniture FROM public.furniture");
        List<String> fioList = fill.fillList("SELECT fio_client FROM public.client");
        furniture.setItems(FXCollections.observableList(furList));
        fio.setItems(FXCollections.observableList(fioList));

        furniture_show.setOnAction(actionEvent ->
                sw.showWindow("/Catalog/Furniture/MainWindowFurniture.fxml", "Мебель"));

        furniture.setOnAction(actionEvent -> {
            bonus.setText(name_bonus);
            try {
                ResultSet rs = db.Select("SELECT price_furniture/10 FROM public.furniture WHERE name_furniture = '" + furniture.getSelectionModel().getSelectedItem() + "';");
                while (rs.next()){
                    bonus.setText(bonus.getText() + " " + new DecimalFormat("#.##").format(rs.getInt(1)));
                    count_bonus = rs.getInt(1);
                    price.setText(String.valueOf(rs.getInt(1) * 10));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });

        addComment.setOnAction(actionEvent ->
                sw.showWindow("/Main/CommentOrderWindow.fxml", "Комментарий"));

        add.setOnAction(actionEvent -> {
            ShowWork showWork = new ShowWork();
            String headerError = "Ошибка в добавлении";

            int fio2 = fio.getSelectionModel().getSelectedIndex() + 1;
            int furniture2 = furniture.getSelectionModel().getSelectedIndex() + 1;

            List<Integer> tmp_index;
            try {
                tmp_index = fill.fillListInt("SELECT id_order FROM public.ordering");

                ResultSet rs = db.Select("SELECT id_discount_card FROM public.client WHERE fio_client = '" + fio.getSelectionModel().getSelectedItem() + "';");
                while (rs.next()){
                    db.Update("UPDATE public.discount_card SET bonus_amount_card = bonus_amount_card + " + count_bonus + "" +
                            "WHERE id_discount_card = " + rs.getInt(1) + ";");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            int index = tmp_index.size() + 1;

            if(openOrd.getValue() == null){
                showWork.showError(headerError, "Заполните поле с датой создания заказа");
                return;
            }

            if(fio2 <= 0){
                showWork.showError(headerError, "Выберите ФИО клиента");
                return;
            }

            if(closedOrd.getValue() == null){
                showWork.showError(headerError, "Заполните поле с датой закрытия заказа");
                return;
            }

            if(furniture2 <= 0){
                showWork.showError(headerError, "Выберите мебель в заказе");
                return;
            }

            String sql = "INSERT INTO public.ordering VALUES (" + index + ", " + "'" + openOrd.getValue().toString() + "'," +
                    "" + fio2 + ", 1, " + "'" + closedOrd.getValue().toString() + "', " + "" + furniture2 + ");";

            boolean start = showWork.showWarning("Вы добавили комментарий к заказу?", "");
            if (start){
                try {
                    db.Insert(sql);
                    sw.showInfo("Добавление заказа произошло успешно!", "");
                    Stage stage = (Stage) add.getScene().getWindow();
                    stage.close();
                } catch (SQLException e) {
                    sw.showError(headerError, "Ошибка при добавлении в базу");
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
