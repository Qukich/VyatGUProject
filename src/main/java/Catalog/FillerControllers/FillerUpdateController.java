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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class FillerUpdateController {
    @FXML
    private TextField nameSearch;
    @FXML
    private TextField price;
    @FXML
    private TextField countUnit;

    @FXML
    private ComboBox<String> nameUnit;

    @FXML
    private Button update;

    @FXML
    void initialize() throws SQLException {
        DBwork db = new DBwork();
        Fill fill = new Fill();
        List<String> nameUni = fill.fillList("SELECT name_unit FROM public.Unit_Weight");
        nameUnit.setItems(FXCollections.observableArrayList(nameUni));
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
                    ResultSet rs = db.Select("SELECT price_filler, j1.id_unit, count_unit FROM public.filler AS j0\n" +
                            "INNER JOIN public.Unit_Weight AS j1 ON j0.id_unit = j1.id_unit\n" +
                            "WHERE name_filler LIKE '" + nameSearch.getText() + "%';");
                    while (rs.next()) {
                        price.setText(String.valueOf(rs.getDouble(1)));
                        countUnit.setText(String.valueOf(rs.getInt(3)));
                        nameUnit.getSelectionModel().select(rs.getInt(2) - 1);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else if (Objects.equals(nameSearch.getText(), "") || nameSearch.getLength() < 2) {
                price.setText("");
                countUnit.setText("");
                nameUnit.getSelectionModel().clearSelection();
            }
        });

        update.setOnAction(actionEvent -> {
            ShowWork sw = new ShowWork();
            StringBuilder sql = new StringBuilder();
            int name = nameUnit.getSelectionModel().getSelectedIndex() + 1;

            sql.append("UPDATE public.filler SET ");

            try {
                List<String> tmp = fill.fillList("SELECT name_filler from public.filler");
                boolean flag = tmp.contains(nameSearch.getText());
                if(!flag) {
                    sw.showError(headerError, "Такого наполнителя нет в базе"); return;}
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            if(!Objects.equals(price.getText(), "")){
                sql.append("price_filler = ").append(price.getText()).append(",");
            }

            if(name > 0){
                sql.append("id_unit = ").append(name).append(",");
            }

            if(!Objects.equals(countUnit.getText(), "")){
                sql.append("count_unit = ").append(countUnit.getText()).append(",");
            }

            sql.setLength(sql.length() - 1);

            sql.append(" WHERE name_filler = '").append(nameSearch.getText()).append("';");

            try {
                db.Update(sql.toString());

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
