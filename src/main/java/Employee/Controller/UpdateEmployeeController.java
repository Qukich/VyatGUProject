package Employee.Controller;

import WorkingFunctions.DBwork;
import WorkingFunctions.Fill;
import WorkingFunctions.ShowWork;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class UpdateEmployeeController implements EventHandler<ActionEvent> {
    @FXML
    private TextField FIO;
    @FXML
    private TextField series;
    @FXML
    private TextField nomer;
    @FXML
    private TextField phone;

    @FXML
    private ComboBox<String> education;
    @FXML
    private ComboBox<String> Post;
    @FXML
    private ComboBox<String> workPlace;

    @FXML
    private DatePicker beginWork;

    @FXML
    private Button update;


    private DBwork db;
    private Fill fill;

    @FXML
    void initialize() throws SQLException {
        db = new DBwork();
        fill = new Fill();
        List<String> postList = fill.fillList("SELECT name_post FROM public.post");
        List<String> educationList = fill.fillList("SELECT degree_education FROM public.education");
        List<String> workAdress = fill.fillList("SELECT work_adress FROM public.work_adress");

        Post.setItems(FXCollections.observableArrayList(postList));
        workPlace.setItems(FXCollections.observableArrayList(workAdress));
        education.setItems(FXCollections.observableArrayList(educationList));

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

        update.setOnAction(this);
        FIO.setOnKeyPressed(actionEvent -> {
            if(!Objects.equals(FIO.getText(), "") && FIO.getLength() >= 2){
                try {
                    ResultSet rs = db.Select("SELECT fio_employee, pasport_series_employee, pasport_id_employee, j1.degree_education, " +
                            "date_getting_start, j2.name_post, phone_number_employee, j4.work_adress FROM public.employee as j0 \n" +
                            "INNER JOIN public.education as j1 on j1.id_education = j0.id_education\n" +
                            "INNER JOIN public.post as j2 on j0.post_employee = j2.id_post\n" +
                            "INNER JOIN public.work_adress as j4 on j4.id_work_adress = j0.id_work_adress\n" +
                            "WHERE fio_employee LIKE '" + FIO.getText() + "%'");
                    while (rs.next()){
                        series.setText(rs.getString(2));
                        nomer.setText(rs.getString(3));
                        education.getSelectionModel().select(rs.getString(4));
                        beginWork.setValue(rs.getDate(5).toLocalDate());
                        Post.getSelectionModel().select(rs.getString(6));
                        phone.setText(rs.getString(7));
                        workPlace.getSelectionModel().select(rs.getString(8));
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else if(Objects.equals(FIO.getText(), "") || FIO.getLength() < 2){
                series.setText("");
                nomer.setText("");
                education.getSelectionModel().clearSelection();
                education.setButtonCell(new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty) ;
                        if (empty || item == null) {
                            setText("Образование");
                        } else {
                            setText(item);
                        }
                    }
                });

                beginWork.setValue(null);

                Post.getSelectionModel().clearSelection();
                Post.setButtonCell(new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty) ;
                        if (empty || item == null) {
                            setText("Должность");
                        } else {
                            setText(item);
                        }
                    }
                });

                phone.setText("");
                workPlace.getSelectionModel().clearSelection();
                workPlace.setButtonCell(new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty) ;
                        if (empty || item == null) {
                            setText("Место работы");
                        } else {
                            setText(item);
                        }
                    }
                });
            }
        });
    }

    @Override
    public void handle(ActionEvent actionEvent) {
        db = new DBwork();
        ShowWork showWork = new ShowWork();
        StringBuilder sql = new StringBuilder();
        String headerError = "Ошибка в обновлении";

        int work2 = workPlace.getSelectionModel().getSelectedIndex() + 1;
        int post2 = Post.getSelectionModel().getSelectedIndex() + 1;
        int education2 = education.getSelectionModel().getSelectedIndex() + 1;

        sql.append("UPDATE public.employee SET ");

        try {
            List<String> fio = fill.fillList("Select fio_employee from public.employee");
            boolean flag = fio.contains(FIO.getText());
            if(!flag) {
                showWork.showError(headerError, "Такого сотрудника нет в базе"); return;}
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if(Objects.equals(FIO.getText(), "")) {
            showWork.showError(headerError, "Введите фамилию или имя"); return;
        }

        if((work2 == 0) && (post2  == 0) && (education2 == 0) && Objects.equals(series.getText(), "")
                && Objects.equals(nomer.getText(), "") && Objects.equals(phone.getText(), "") && Objects.equals(beginWork.getValue().toString(), "")) {
            showWork.showError(headerError, "Выберите хотя бы одно поле для обновления"); return;
        }

        if(education2 > 0)
            sql.append("id_education = ").append(education2).append(",");
        if(work2 > 0)
            sql.append("id_work_adress = ").append(work2).append(",");
        if(post2 > 0)
            sql.append("post_employee = ").append(post2).append(",");

        if(!Objects.equals(phone.getText(), "")){
            if (phone.getLength() == 11){
                sql.append("phone_number_employee = '").append(phone.getText()).append("',");
            }
            else {showWork.showError(headerError, "Неправильная длинна номера"); return;}
        }

        if(!Objects.equals(series.getText(), "")){
            if(series.getLength() == 4){
                sql.append("pasport_series_employee = '").append(series.getText()).append("',");
            }
            else {showWork.showError(headerError, "Неправильная длинна серии паспорта"); return;}
        }

        if(!Objects.equals(nomer.getText(), "")){
            if(nomer.getLength() == 6){
                sql.append("pasport_nomer_employee = '").append(nomer.getText()).append("',");
            }
            else {showWork.showError(headerError, "Неправильная длинна номера паспорта"); return;}
        }

        if(beginWork.getValue() != null)
            sql.append("date_getting_start = '").append(beginWork.getValue().toString()).append("',");

        sql.setLength(sql.length() - 1);
        sql.append(" WHERE fio_employee = '").append(FIO.getText()).append("';");
        try {
            db.Update(sql.toString());

            showWork.showInfo("Обновление сотрудника произошло успешно!", "");
            Stage stage = (Stage) update.getScene().getWindow();
            stage.close();
        } catch (SQLException throwable){
            showWork.showError(headerError, "Ошибка при обновлении значений в базе");
            throwable.printStackTrace();
        }
    }
}
