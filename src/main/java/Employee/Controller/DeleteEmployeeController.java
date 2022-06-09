package Employee.Controller;

import WorkingFunctions.DBwork;
import WorkingFunctions.Fill;
import WorkingFunctions.ShowWork;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class DeleteEmployeeController implements EventHandler<ActionEvent> {
    @FXML
    private TextField fio;
    @FXML
    private Button delete;

    @FXML
    void initialize() {
        delete.setOnAction(this);
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        DBwork db = new DBwork();
        ShowWork showWork = new ShowWork();
        Fill fill = new Fill();
        String sql;
        String sqlAcc;
        String sqlName;
        int id = 0;
        String headerError = "Ошибка в удалении";

        try {
            List<String> FIO = fill.fillList("Select fio_employee from public.employee");
            boolean flag = FIO.contains(fio.getText());
            if(!flag) {
                showWork.showError(headerError, "Такого сотрудника нет в базе"); return;}
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        boolean start = showWork.showWarning("Вы точно хотите удалить этого сотрудника?", "");
        if (start){
            sql = "DELETE FROM public.employee WHERE fio_employee = '" + fio.getText() + "';";
            sqlName = "SELECT j0.id_login_employee FROM public.account_employee AS j0\n" +
                    "INNER JOIN public.employee AS j1 ON j0.id_login_employee = j1.id_account_employee\n" +
                    "WHERE j1.fio_employee = '" + fio.getText() + "';";
            try {
                ResultSet rs = db.Select(sqlName);
                while (rs.next()){
                    id = rs.getInt(1);
                }
                sqlAcc = "DELETE FROM public.account_employee WHERE id_login_employee = " + id + "";
                db.Delete(sql);
                db.Delete(sqlAcc);

                showWork.showInfo("Удаление сотрудника произошло успешно!", "");
                Stage stage = (Stage) delete.getScene().getWindow();
                stage.close();
            } catch (SQLException e) {
                showWork.showError(headerError, "Ошибка при удалении из базы");
                e.printStackTrace();
            }
        }
    }
}
