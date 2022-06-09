package Employee.Controller;

import Login.LoginController;
import WorkingFunctions.ConstructName;
import WorkingFunctions.DBwork;
import WorkingFunctions.Fill;
import WorkingFunctions.ShowWork;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;


public class AddEmployeeController implements EventHandler<ActionEvent> {
    @FXML
    private Button add;
    @FXML
    private Button hint;

    @FXML
    private TextField fio;
    @FXML
    private TextField series;
    @FXML
    private TextField nomer;
    @FXML
    private TextField phone;
    @FXML
    private TextField login;
    @FXML
    private TextField password;
    @FXML
    private TextField controlQuest;

    @FXML
    private ComboBox<String> post;
    @FXML
    private ComboBox<String> workPlace;
    @FXML
    private ComboBox<String> education;
    @FXML
    private ComboBox<String> controlQuestCombo;
    @FXML
    private ComboBox<String> comboRole;

    @FXML
    private DatePicker beginWork;

    @FXML
    private DatePicker birth;

    @FXML
    private Label textRole;

    private DBwork db;
    private Fill fill;

    LoginController lg = ConstructName.getInstance().getLogin();

    @FXML
    void initialize() throws SQLException{
        db = new DBwork();
        fill = new Fill();
        ConstructName.getInstance().setAddEmp(this);

        if(lg.id_role != 3){
            textRole.setVisible(false);
            comboRole.setVisible(false);
        }

        Pattern p = Pattern.compile("(\\d+\\.?\\d*)?");
        phone.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches())
                phone.setText(oldValue);
        });
        series.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches())
                series.setText(oldValue);
        });
        nomer.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches())
                nomer.setText(oldValue);
        });

        add.setOnAction(this);
        hint.setOnAction(arg0 -> ShowWork.showOneTimeTooltip(hint, "Контрольный вопрос нужен \n чтобы восстановить пароль."));

        List<String> postList = fill.fillList("SELECT name_post FROM public.post");
        List<String> educationList = fill.fillList("SELECT degree_education FROM public.education");
        List<String> workAddress = fill.fillList("SELECT work_adress FROM public.work_adress");
        List<String> controlQuestion = fill.fillList("SELECT control_question_employee FROM public.control_question");
        List<String> roleList = fill.fillList("SELECT name_role FROM public.account_role");

        post.setItems(FXCollections.observableArrayList(postList));
        workPlace.setItems(FXCollections.observableArrayList(workAddress));
        education.setItems(FXCollections.observableArrayList(educationList));
        controlQuestCombo.setItems(FXCollections.observableArrayList(controlQuestion));
        comboRole.setItems(FXCollections.observableArrayList(roleList));
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        db = new DBwork();
        ShowWork showWork = new ShowWork();
        String headerError = "Ошибка в добавлении";

        int work2 = workPlace.getSelectionModel().getSelectedIndex() + 1;
        int post2 = post.getSelectionModel().getSelectedIndex() + 1;
        int education2 = education.getSelectionModel().getSelectedIndex() + 1;
        int control2 = controlQuestCombo.getSelectionModel().getSelectedIndex() + 1;

        List<Integer> tmp_index;
        try {
            tmp_index = fill.fillListInt("SELECT MAX(id_employee) FROM public.employee");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int index = tmp_index.get(0) + 1;

        List<Integer> tmp_index_acc;
        try {
            tmp_index_acc = fill.fillListInt("SELECT MAX(id_login_employee) FROM public.account_employee");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        int index_acc = tmp_index_acc.get(0) + 1;

        if (Objects.equals(fio.getText(), "")){
            showWork.showError(headerError, "Заполните поле с ФИО");
            return;
        }

        if (birth.getValue() == null){
            showWork.showError(headerError, "Заполните поле с Датой рождения");
            return;
        }

        if (beginWork.getValue() == null){
            showWork.showError(headerError, "Заполните поле с Датой начала работы");
            return;
        }

        if (education2 <= 0) {
            showWork.showError(headerError, "Не выбрано образование");
            return;
        }


        if (Objects.equals(series.getText(), "") || series.getLength() != 4) {
            showWork.showError(headerError, "Заполните поле с серией паспорта\nДлинна должна быть 4 символа");
            return;
        }

        if (Objects.equals(nomer.getText(), "") || nomer.getLength() != 6){
            showWork.showError(headerError, "Заполните поле с номером паспорта\nДлинна должна быть 6 символов");
            return;
        }

        if (post2 <= 0) {
            showWork.showError(headerError, "Не выбрана должность");
            return;
        }

        if (Objects.equals(phone.getText(), "") || phone.getLength() != 11){
            showWork.showError(headerError, "Заполните поле с номером телефона\nДлинна должна быть 11 символов");
            return;
        }

        if (work2 <= 0){
            showWork.showError(headerError, "Не выбрано место работы");
            return;
        }

        if(Objects.equals(login.getText(), "")){
            showWork.showError(headerError, "Заполните поле с Логином");
            return;
        }

        if(Objects.equals(password.getText(), "")){
            showWork.showError(headerError, "Заполните поле с Паролем");
            return;
        }

        if(control2 <= 0){
            showWork.showError(headerError, "Заполните поле с выбором контрольного вопроса");
            return;
        }

        if(Objects.equals(controlQuest.getText(), "")){
            showWork.showError(headerError, "Заполните поле с контрольным вопросом");
            return;
        }

        int tmp_role = 1;
        if((comboRole.getSelectionModel().getSelectedIndex() + 1) > 0){
            tmp_role = comboRole.getSelectionModel().getSelectedIndex() + 1;
        }

        String sqlAcc = "INSERT INTO public.account_employee VALUES(" + index_acc + ", " + "'" + login.getText() + "', " +
                "'" + password.getText() + "', " + "" + control2 + ", " + "'" + controlQuest.getText() + "', " + tmp_role + ");";

        String sql = "INSERT INTO public.employee VALUES(" + index + ", " + "'" + fio.getText() + "', " +
                "'" + birth.getValue().toString() + "', " + "'" + beginWork.getValue().toString() + "', " +
                "" + education2 + ", " + "'" + series.getText() + "', " + "'" + nomer.getText() + "', " +
                "" + post2 + ", " + "" + phone.getText() + ", " + "" + work2 + ", " + "" + index_acc + ");";

        try {
            db.Insert(sqlAcc);
            db.Insert(sql);

            showWork.showInfo("Добавление сотрудника произошло успешно!", "");
            Stage stage = (Stage) add.getScene().getWindow();
            stage.close();
        } catch (SQLException throwable){
            showWork.showError(headerError, "Ошибка при добавлении в базу");
            throwable.printStackTrace();
        }
    }
}
