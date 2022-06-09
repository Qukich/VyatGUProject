package Client.Controllers;

import Login.LoginController;
import WorkingFunctions.ConstructName;
import WorkingFunctions.DBwork;
import WorkingFunctions.ShowWork;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.regex.Pattern;

public class UpdateClientController {
    @FXML
    private TextField nameSearch;
    @FXML
    private TextField bonusAmount;
    @FXML
    private TextField phone;

    @FXML
    private Button update;

    @FXML
    private Label countText;

    private DBwork db;
    LoginController lg = ConstructName.getInstance().getLogin();

    @FXML
    void initialize(){
        db = new DBwork();
        String headerError = "Ошибка в обновлении";
        ConstructName.getInstance().setUpClient(this);

        if(lg.id_role != 3){
            countText.setVisible(false);
            bonusAmount.setVisible(false);
        }

        Pattern p = Pattern.compile("(\\d+\\.?\\d*)?");
        phone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches())
                phone.setText(oldValue);
        });
        bonusAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches())
                bonusAmount.setText(oldValue);
        });

        nameSearch.setOnKeyPressed(actionEvent -> {
            if (!Objects.equals(nameSearch.getText(), "") && nameSearch.getLength() >= 2) {
                try {
                    ResultSet rs = db.Select("SELECT j1.bonus_amount_card, phone_client FROM public.client AS j0\n" +
                            "INNER JOIN public.discount_card AS j1 ON j1.id_discount_card = j0.id_discount_card\n" +
                            "WHERE fio_client LIKE '" + nameSearch.getText() + "%';");
                    while (rs.next()) {
                        bonusAmount.setText(String.valueOf(rs.getInt(1)));
                        phone.setText(String.valueOf(rs.getString(2)));
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else if (Objects.equals(nameSearch.getText(), "") || nameSearch.getLength() < 2) {
                bonusAmount.setText("");
                phone.setText("");
            }
        });

        update.setOnAction(actionEvent -> {
            ShowWork sw = new ShowWork();
            StringBuilder sql = new StringBuilder();
            StringBuilder sql2 = new StringBuilder();

            sql.append("UPDATE public.client SET ");
            sql2.append("UPDATE public.discount_card SET ");

            if(!Objects.equals(phone.getText(), "") && phone.getLength() == 11){
                sql.append("phone_client = '").append(phone.getText()).append("'\n");
            } else {
                sw.showError("Длинна номера телефона должна составлять 11 символов", "");
            }

            if(!Objects.equals(bonusAmount.getText(), "")){
                sql2.append("bonus_amount_card = ").append(bonusAmount.getText()).append("\n");
            }

            int tmp_index = 0;
            try {
                ResultSet rs = db.Select("SELECT id_discount_card FROM public.client WHERE fio_client LIKE '" + nameSearch.getText() + "%'");
                while(rs.next()){
                    tmp_index = rs.getInt(1);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            sql.append("WHERE fio_client LIKE '").append(nameSearch.getText()).append("';");
            sql2.append("WHERE id_discount_card = ").append(tmp_index).append(";");

            try {
                db.Update(sql.toString());
                db.Update(sql2.toString());

                sw.showInfo("Обновление наполнителя произошло успешно!", "");
                Stage stage = (Stage) update.getScene().getWindow();
                stage.close();
            } catch (SQLException throwable){
                sw.showError(headerError, "Ошибка при обновлении значений в базе");
                throwable.printStackTrace();
            }
        });
    }

}
