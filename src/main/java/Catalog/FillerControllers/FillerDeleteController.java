package Catalog.FillerControllers;

import WorkingFunctions.DBwork;
import WorkingFunctions.Fill;
import WorkingFunctions.ShowWork;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class FillerDeleteController {
    @FXML
    private TextField name;
    @FXML
    private Button delete;

    @FXML
    void initialize(){
        delete.setOnAction(actionEvent -> {
            DBwork db = new DBwork();
            ShowWork showWork = new ShowWork();
            Fill fill = new Fill();
            String headerError = "Ошибка в удалении";

            try {
                List<String> NAME = fill.fillList("SELECT name_filler FROM public.filler");
                boolean flag = NAME.contains(name.getText());
                if(!flag) {
                    showWork.showError(headerError, "Такого наполнителя нет в базе"); return;}
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            boolean start = showWork.showWarning("Вы точно хотите удалить этот наполнитель?", "");
            if (start){
                String sql = "DELETE FROM public.filler WHERE name_filler = '" + name.getText() + "';";
                try {
                    db.Delete(sql);

                    showWork.showInfo("Удаление наполнителя произошло успешно!", "");
                    Stage stage = (Stage) delete.getScene().getWindow();
                    stage.close();
                } catch (SQLException e) {
                    showWork.showError(headerError, "Ошибка при удалении из базы");
                    e.printStackTrace();
                }
            }
        });
    }
}
