package Client.Controllers;

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

public class DeleteClientController {
    @FXML
    private Button delete;
    @FXML
    private TextField name;
    @FXML
    void initialize() {
        delete.setOnAction(actionEvent -> {
            DBwork db = new DBwork();
            ShowWork showWork = new ShowWork();
            Fill fill = new Fill();
            String headerError = "Ошибка в удалении";

            try {
                List<String> NAME = fill.fillList("SELECT fio_client FROM public.client");
                boolean flag = NAME.contains(name.getText());
                if(!flag) {
                    showWork.showError(headerError, "Такого клиента нет в базе"); return;}
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            boolean start = showWork.showWarning("Вы точно хотите удалить этого клиента?", "");
            if (start) {
                String sql = "DELETE FROM public.client WHERE fio_client = '" + name.getText() + "';";
                String sql2 = "";
                try {
                    int tmp_index = 0;
                    ResultSet rs = db.Select("SELECT id_discount_card FROM public.client WHERE fio_client = '" + name.getText() + "'");
                    while(rs.next()){
                        tmp_index = rs.getInt(1);
                    }
                    sql2 = "DELETE FROM public.discount_card WHERE id_discount_card = " + tmp_index + ";";
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    db.Delete(sql2);
                    db.Delete(sql);

                    showWork.showInfo("Удаление клиента произошло успешно!", "");
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
