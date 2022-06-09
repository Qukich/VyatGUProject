package Catalog.UpholsteryControllers;

import WorkingFunctions.DBwork;
import WorkingFunctions.Fill;
import WorkingFunctions.ShowWork;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class UpholsteryDeleteController {
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
                List<String> NAME = fill.fillList("SELECT name_upholstery_material FROM public.upholstery");
                boolean flag = NAME.contains(name.getText());
                if(!flag) {
                    showWork.showError(headerError, "Такой обивки нет в базе"); return;}
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            boolean start = showWork.showWarning("Вы точно хотите удалить эту обивку?", "");
            if (start){
                String sql = "DELETE FROM public.upholstery WHERE name_upholstery_material = '" + name.getText() + "';";
                try {
                    db.Delete(sql);

                    showWork.showInfo("Удаление обивки произошло успешно!", "");
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
