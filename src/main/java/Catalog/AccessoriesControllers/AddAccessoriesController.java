package Catalog.AccessoriesControllers;

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
import java.util.regex.Pattern;

public class AddAccessoriesController {
    @FXML
    private Button add;

    @FXML
    private TextField name;
    @FXML
    private TextField price;
    @FXML
    private TextField countUnit;

    @FXML
    void initialize() {
        String headerError = "Ошибка в добавлении";

        Pattern p = Pattern.compile("(\\d+\\.?\\d*)?");
        price.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) price.setText(oldValue);
        });
        countUnit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) countUnit.setText(oldValue);
        });

        add.setOnAction(actionEvent -> {
            Fill fill = new Fill();
            DBwork db = new DBwork();
            ShowWork sw = new ShowWork();

            List<Integer> tmp_index;
            try {
                tmp_index = fill.fillListInt("SELECT MAX(id_accessories) FROM public.accessories");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            int index = tmp_index.get(0) + 1;

            if(Objects.equals(price.getText(), "")){
                sw.showError(headerError, "Заполните поле с ценой фурнитуры"); return;
            }

            if(Objects.equals(name.getText(), "")){
                sw.showError(headerError, "Заполните поле с названием фурнитуры"); return;
            }

            if(Objects.equals(countUnit.getText(), "")){
                sw.showError(headerError, "Заполните поле с количеством фурнитуры"); return;
            }

            String sql = "INSERT INTO public.accessories VALUES(" + index + ", " + "'" + name.getText() + "', " +
                    "" + price.getText() + ", " + "" + countUnit.getText() + ");";

            try {
                db.Insert(sql);

                sw.showInfo("Добавление фурнитуры произошло успешно!", "");
                Stage stage = (Stage) add.getScene().getWindow();
                stage.close();
            } catch (SQLException throwable){
                sw.showError(headerError, "Ошибка при добавлении в базу");
                throwable.printStackTrace();
            }
        });
    }
}
