package Catalog.FillerControllers;

import WorkingFunctions.DBwork;
import WorkingFunctions.Fill;
import WorkingFunctions.ShowWork;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class FillerAddController {
    @FXML
    private Button add;

    @FXML
    private TextField name;
    @FXML
    private TextField price;
    @FXML
    private TextField countUnit;

    @FXML
    private ComboBox<String> nameUnit;

    @FXML
    void initialize() throws SQLException {
        Fill fill = new Fill();
        List<String> nameUni = fill.fillList("SELECT name_unit FROM public.Unit_weight");
        nameUnit.setItems(FXCollections.observableArrayList(nameUni));
        String headerError = "Ошибка в добавлении";

        Pattern p = Pattern.compile("(\\d+\\.?\\d*)?");
        price.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) price.setText(oldValue);
        });
        countUnit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) countUnit.setText(oldValue);
        });

        add.setOnAction(actionEvent -> {
            DBwork db = new DBwork();
            ShowWork sw = new ShowWork();

            List<Integer> tmp_index;
            try {
                tmp_index = fill.fillListInt("SELECT MAX(id_filler) FROM public.filler");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            int index = tmp_index.get(0) + 1;

            int nameUn = nameUnit.getSelectionModel().getSelectedIndex() + 1;

            if(Objects.equals(price.getText(), "")){
                sw.showError(headerError, "Заполните поле с ценой наполнителя"); return;
            }

            if(Objects.equals(name.getText(), "")){
                sw.showError(headerError, "Заполните поле с названием наполнителя"); return;
            }

            if(nameUn <= 0){
                sw.showError(headerError, "Выберете единицу измерения наполнителя"); return;
            }

            if(Objects.equals(countUnit.getText(), "")){
                sw.showError(headerError, "Заполните поле с количеством наполнителя"); return;
            }

            String sql = "INSERT INTO public.filler VALUES(" + index + ", " + "'" + price.getText() + "', " +
                    "'" + name.getText() + "', " + "" + nameUn + ", " + "'" + countUnit.getText() + "');";

            try {
                db.Insert(sql);

                sw.showInfo("Добавление наполнителя произошло успешно!", "");
                Stage stage = (Stage) add.getScene().getWindow();
                stage.close();
            } catch (SQLException throwable){
                sw.showError(headerError, "Ошибка при добавлении в базу");
                throwable.printStackTrace();
            }
        });
    }
}
