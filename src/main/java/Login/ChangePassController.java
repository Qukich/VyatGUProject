package Login;

import WorkingFunctions.DBwork;
import WorkingFunctions.Fill;
import WorkingFunctions.ShowWork;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.security.SecureRandom;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class ChangePassController {
    @FXML
    private Button change;

    @FXML
    private TextField login;
    @FXML
    private TextField controlQuest;
    @FXML
    private TextField newPass;
    @FXML
    private Label lblControl;

    private ShowWork show;
    private Fill fill;
    private DBwork db;
    static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    static SecureRandom rnd = new SecureRandom();

    @FXML
    void initialize(){
        show = new ShowWork();
        fill = new Fill();
        db = new DBwork();
        newPass.setEditable(false);
        change.setOnAction(actionEvent -> {
            try {
                List<String> loginList = fill.fillList("SELECT login_employee FROM public.account_employee");
                String loginTemp = login.getText();
                if(!loginList.contains(loginTemp)) {show.showError("Неправильный логин", "Проверьте правильность ввода логина"); return;}
                ResultSet controlTemp = db.Select("SELECT control_question FROM public.account_employee WHERE login_employee = '" + loginTemp + "';");
                while (controlTemp.next()){
                    if (Objects.equals(controlTemp.getString(1), controlQuest.getText())){
                        StringBuilder sb = new StringBuilder(10);
                        for(int i = 0; i < 10; i++)
                            sb.append(AB.charAt(rnd.nextInt(AB.length())));
                        db.Update("UPDATE public.account_employee SET password_employee = '" + sb + "' WHERE login_employee = '" + loginTemp + "';");
                        newPass.setText(sb.toString());
                    } else {show.showError("Неправильный контрольный вопрос", "Проверьте правильность ввода контрольного вопроса"); return;}
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            show.showInfo("Успех!", "Пароль успешно поменян");
        });
        controlQuest.setOnKeyPressed(actionEvent -> {
            try {
                List<String> loginList = fill.fillList("SELECT login_employee FROM public.account_employee");
                String loginTemp = login.getText();
                if(!loginList.contains(loginTemp)) {lblControl.setText("Неправильный логин\nпроверьте правильность ввода логина"); return;}
                ResultSet rs = db.Select("SELECT j0.control_question_employee FROM public.control_question AS j0\n" +
                        "INNER JOIN public.account_employee as j1 ON j0.id_control_question = j1.id_control_question\n" +
                        "WHERE j1.login_employee = '" + loginTemp + "';");
                while (rs.next()){
                    lblControl.setText("Подсказка\n" + rs.getString(1));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
