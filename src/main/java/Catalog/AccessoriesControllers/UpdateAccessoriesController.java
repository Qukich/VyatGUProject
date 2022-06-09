package Catalog.AccessoriesControllers;

import WorkingFunctions.DBwork;
import WorkingFunctions.Fill;
import WorkingFunctions.ShowWork;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class UpdateAccessoriesController {
    @FXML
    private TextField nameSearch;
    @FXML
    private TextField price;
    @FXML
    private TextField countUnit;

    @FXML
    private Button update;

    @FXML
    void initialize() {
        DBwork db = new DBwork();
        Fill fill = new Fill();
        String headerError = "Ошибка в обновлении";

        Pattern p = Pattern.compile("(\\d+\\.?\\d*)?");
        price.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) price.setText(oldValue);
        });
        countUnit.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) countUnit.setText(oldValue);
        });

        nameSearch.setOnKeyPressed(actionEvent -> {
            if (!Objects.equals(nameSearch.getText(), "") && nameSearch.getLength() >= 2) {
                try {
                    ResultSet rs = db.Select("SELECT price_accessories, quantity_accessories FROM public.accessories\n" +
                            "WHERE description_accessories LIKE '" + nameSearch.getText() + "%';");
                    while (rs.next()) {
                        price.setText(String.valueOf(rs.getDouble(1)));
                        countUnit.setText(String.valueOf(rs.getInt(2)));
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else if (Objects.equals(nameSearch.getText(), "") || nameSearch.getLength() < 2) {
                price.setText("");
                countUnit.setText("");
            }
        });

        update.setOnAction(actionEvent -> {
            ShowWork sw = new ShowWork();
            StringBuilder sql = new StringBuilder();

            sql.append("UPDATE public.accessories SET ");

            try {
                List<String> tmp = fill.fillList("SELECT description_accessories from public.accessories");
                boolean flag = tmp.contains(nameSearch.getText());
                if(!flag) {
                    sw.showError(headerError, "Такой фурнитуры нет в базе"); return;}
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if(!Objects.equals(price.getText(), "")){
                sql.append("price_accessories = ").append(price.getText()).append(",");
            }

            if(!Objects.equals(countUnit.getText(), "")){
                sql.append("quantity_accessories = ").append(countUnit.getText()).append(",");
            }

            sql.setLength(sql.length() - 1);

            sql.append(" WHERE description_accessories = '").append(nameSearch.getText()).append("';");

            try {
                db.Update(sql.toString());

                sw.showInfo("Обновление фурнитуры произошло успешно!", "");
                Stage stage = (Stage) update.getScene().getWindow();
                stage.close();
            } catch (SQLException throwable){
                sw.showError(headerError, "Ошибка при обновлении значений в базе");
                throwable.printStackTrace();
            }
        });
    }
}
