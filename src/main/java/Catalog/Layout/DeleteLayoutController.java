package Catalog.Layout;

import WorkingFunctions.DBwork;
import WorkingFunctions.Fill;
import WorkingFunctions.ShowWork;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DeleteLayoutController {
    @FXML
    private Button delete;
    @FXML
    private ComboBox<String> name;

    @FXML
    void initialize() throws SQLException{
        Fill fill = new Fill();
        List<String> nameLay = fill.fillList("SELECT name_layout FROM public.layout");
        name.setItems(FXCollections.observableList(nameLay));

        delete.setOnAction(actionEvent -> {
            DBwork db = new DBwork();
            ShowWork showWork = new ShowWork();

            boolean start = showWork.showWarning("Вы точно хотите удалить этот макет?", "");
            if (start){
                String sql = "DELETE FROM public.layout WHERE name_layout = '" + name.getValue() + "';";
                try {
                    ResultSet rs = db.Select("SELECT path_to_image_layout FROM public.layout\n" +
                            "WHERE name_layout = '" + name.getValue() + "'");
                    while (rs.next()){
                        File file = new File(rs.getString(1));
                        boolean f = file.delete();
                        if(!f){
                            return;
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    db.Delete(sql);

                    showWork.showInfo("Удаление макета произошло успешно!", "");
                    Stage stage = (Stage) delete.getScene().getWindow();
                    stage.close();
                } catch (SQLException e) {
                    showWork.showError("Ошибка в удалении", "Ошибка при удалении из базы");
                    e.printStackTrace();
                }
            }
        });
    }
}
