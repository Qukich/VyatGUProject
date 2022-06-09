package Client.Controllers;

import WorkingFunctions.DBwork;
import WorkingFunctions.Fill;
import WorkingFunctions.ShowWork;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;

public class AddClientController {
    @FXML
    private Button add;
    @FXML
    private Button generate;

    @FXML
    private TextField fio;
    @FXML
    private TextField number;
    @FXML
    private TextField phone;

    @FXML
    void initialize(){
        String headerError = "Ошибка в добавлении";
        number.setEditable(false);

        Pattern p = Pattern.compile("(\\d+\\.?\\d*)?");
        phone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) phone.setText(oldValue);
        });

        generate.setOnAction(actionEvent ->{
            StringBuilder code = new StringBuilder();
            Random rnd = new Random();
            for(int i = 0; i < 6; i++){
                code.append(rnd.nextInt(0,9));
            }
            number.setText(code.toString());
        });

        add.setOnAction(actionEvent ->{
            Fill fill = new Fill();
            DBwork db = new DBwork();
            ShowWork sw = new ShowWork();

            List<Integer> tmp_index;
            try {
                tmp_index = fill.fillListInt("SELECT MAX(id_client) FROM public.client");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            int index = tmp_index.get(0) + 1;

            List<Integer> tmp_index2;
            try {
                tmp_index2 = fill.fillListInt("SELECT MAX(id_discount_card) FROM public.discount_card");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            int index2 = tmp_index2.get(0) + 1;

            if(Objects.equals(fio.getText(), "")){
                sw.showError(headerError, "Заполните поле с ФИО клиента");
                return;
            }

            if(Objects.equals(phone.getText(), "") || phone.getLength() != 11){
                sw.showError(headerError, "Заполнител поле с номером телефона, цифр в номере телефона должно быть 11");
                return;
            }

            if(Objects.equals(number.getText(), "")){
                sw.showError(headerError, "Сгенирируйте номер дисконтной карты для клиента");
                return;
            }

            String sqlCard = "INSERT INTO public.discount_card VALUES(" + index2 + ", " + "" + number.getText() + ", 0);";

            String sql = "INSERT INTO public.client VALUES(" + index + ", " + "'" + fio.getText() + "', " +
                    "'" + phone.getText() + "', " + "" + index2 + ");";

            try {
                db.Insert(sqlCard);
                db.Insert(sql);

                sw.showInfo("Добавление клиента произошло успешно!", "");
                Stage stage = (Stage) add.getScene().getWindow();
                stage.close();
            } catch (SQLException e){
                sw.showError(headerError, "Ошибка при добавлении в базу");
                e.printStackTrace();
            }
        });
    }
}
