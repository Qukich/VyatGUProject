package Login;

import WorkingFunctions.ConstructName;
import WorkingFunctions.DBwork;
import WorkingFunctions.Fill;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import WorkingFunctions.ShowWork;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class LoginController {
    @FXML
    private Button enter;
    @FXML
    private Button changePass;

    @FXML
    private TextField login;
    @FXML
    private TextField password;

    private ShowWork show;
    private Fill fill;
    private DBwork db;

    public int id_role;

    @FXML
    public void initialize() {
        show = new ShowWork();
        fill = new Fill();
        db = new DBwork();
        ConstructName.getInstance().setLogin(this);
        enter.setOnAction(actionEvent -> {
            try {
                List<String> loginList = fill.fillList("SELECT login_employee FROM public.account_employee");
                String loginTemp = login.getText();
                if(!loginList.contains(loginTemp)) {show.showError("Неправильный логин", "Проверьте правильность ввода логина"); return;}
                ResultSet passTemp = db.Select("SELECT password_employee FROM public.account_employee WHERE login_employee = '" + loginTemp + "';");

                ResultSet rs = db.Select("SELECT j0.id_role FROM public.account_role as j0\n" +
                        "INNER JOIN public.account_employee as j1 on j0.id_role = j1.id_role\n" +
                        "WHERE j1.login_employee = '" + login.getText() + "'");
                while (rs.next()){
                    id_role = rs.getInt(1);
                }

                while (passTemp.next()){
                    if (Objects.equals(passTemp.getString(1), password.getText())){
                        if (id_role == 2){
                            show.showWindow("/Employee/MainWindowEmployee.fxml", "Сотрудники");
                        } else {
                            show.showWindow("/Main/MainOrderWindow.fxml", "12 кресел");
                        }
                    } else {
                        show.showError("Неправильный пароль", "Проверьте правильность ввода пароля");
                        return;
                    }
                }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            Stage stage = (Stage) enter.getScene().getWindow();

            stage.close();
        });
        changePass.setOnAction(actionEvent ->
                show.showWindow("/Login/LoginAppError.fxml", "Обновление пароля"));
    }
}
