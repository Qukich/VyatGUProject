package Employee.Controller;

import WorkingFunctions.DBwork;
import Employee.Struct.EmloyeeClass;
import WorkingFunctions.Fill;
import WorkingFunctions.ShowWork;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class MainEmployeeController {
    @FXML
    private TableView<EmloyeeClass> table;

    @FXML
    private TableColumn<EmloyeeClass, String> fio;

    @FXML
    private TableColumn<EmloyeeClass, Date> birth;

    @FXML
    private TableColumn<EmloyeeClass, Integer> series;

    @FXML
    private TableColumn<EmloyeeClass, Integer> nomer;

    @FXML
    private TableColumn<EmloyeeClass, String> education;

    @FXML
    private TableColumn<EmloyeeClass, String> post;

    @FXML
    private TableColumn<EmloyeeClass, Integer> phone;

    @FXML
    private TableColumn<EmloyeeClass, String> place;

    @FXML
    private TableColumn<EmloyeeClass, Integer> beginWork;

    @FXML
    private Button add;

    @FXML
    private Button refresh;

    @FXML
    private Button delete;

    @FXML
    private Button update;

    @FXML
    private Button search;

    @FXML
    private Button filter;

    @FXML
    private Button clearFilter;

    @FXML
    private ComboBox<String> comboEducationFilter;

    @FXML
    private ComboBox<String> comboPostFilter;

    @FXML
    private ComboBox<String> comboAddressWorkFilter;

    @FXML
    private TextField FildSerach;

    ObservableList<EmloyeeClass> EmployeeList = FXCollections.observableArrayList();
    private DBwork db;
    private ShowWork showWork;
    private Fill fill;

    @FXML
    void initialize() throws SQLException{
        comboAddressWorkFilter.visibleProperty().setValue(false);
        comboEducationFilter.visibleProperty().setValue(false);
        comboPostFilter.visibleProperty().setValue(false);
        String sqlSelect = """
                SELECT fio_employee, dob_employee, pasport_series_employee, pasport_id_employee, j1.degree_education, date_getting_start, j2.name_post, phone_number_employee, j4.work_adress FROM public.employee as j0\s
                INNER JOIN public.education as j1 on j1.id_education = j0.id_education
                INNER JOIN public.post as j2 on j0.post_employee = j2.id_post
                INNER JOIN public.work_adress as j4 on j4.id_work_adress = j0.id_work_adress""";

        db = new DBwork();
        showWork = new ShowWork();
        fill = new Fill();

        clearFilter.setOnAction(actionEvent -> {
            comboAddressWorkFilter.getSelectionModel().clearSelection();
            comboEducationFilter.getSelectionModel().clearSelection();
            comboPostFilter.getSelectionModel().clearSelection();
            comboPostFilter.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText("Должность");
                    } else {
                        setText(item);
                    }
                }
            });
            comboEducationFilter.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText("Образование");
                    } else {
                        setText(item);
                    }
                }
            });
            comboAddressWorkFilter.setButtonCell(new ListCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText("Адрес работы");
                    } else {
                        setText(item);
                    }
                }
            });
        });

        refresh.setOnAction(actionEvent -> {
            try {
                table.getItems().clear();
                ResultSet rs = db.Select(sqlSelect);
                while (rs.next()){
                    EmployeeList.add(new EmloyeeClass(
                            rs.getString(1),
                            rs.getDate(2),
                            rs.getInt(3),
                            rs.getInt(4),
                            rs.getString(5),
                            rs.getDate(6),
                            rs.getString(7),
                            rs.getString(8),
                            rs.getString(9)
                    ));
                    table.setItems(EmployeeList);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        add.setOnAction(actionEvent ->
                showWork.showWindow("/Employee/AddEmployeeWindow.fxml", "Добавление"));

        search.setOnAction(actionEvent -> {
            StringBuilder filter = new StringBuilder();
            int work2 = comboAddressWorkFilter.getSelectionModel().getSelectedIndex() + 1;
            int post2 = comboPostFilter.getSelectionModel().getSelectedIndex() + 1;
            int education2 = comboEducationFilter.getSelectionModel().getSelectedIndex() + 1;
            String header = "Ошибка в поиске";

            if ((Objects.equals(FildSerach.getText(), "")) && (education2 == 0) && (work2 == 0) && (post2 == 0)){
                showWork.showError(header, "Не заполнено ни одно поле для поиска");
                return;
            }


            filter.append("\nWHERE ");
            if(FildSerach.getLength() == 4) {
                filter.append("pasport_series_employee ='").append(FildSerach.getText()).append("' and ");
            }
            else if(FildSerach.getLength() == 6) {
                filter.append("pasport_id_employee ='").append(FildSerach.getText()).append("' and ");
            }
            else if(FildSerach.getLength() > 6){
                try {
                    List<String> fio = fill.fillList("Select fio_employee from public.employee");
                    boolean flag = fio.contains(FildSerach.getText());
                    if(!flag) {showWork.showError(header, "Такого сотрудника нет в базе"); return;}
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                filter.append("fio_employee ='").append(FildSerach.getText()).append("' and ");
            }
            if(work2 > 0) {
                filter.append("j4.id_work_adress =").append(work2).append(" and ");
            }
            if(post2 > 0) {
                filter.append("j2.id_post =").append(post2).append(" and ");
            }
            if(education2 > 0) {
                filter.append("j1.id_education =").append(education2).append(" and ");
            }
            filter.setLength(filter.length() - 4);
            String sqlTemp = sqlSelect + filter + "\nORDER BY fio_employee";
            try {
                table.getItems().clear();
                ResultSet rs = db.Select(sqlTemp);
                while (rs.next()){
                    EmployeeList.add(new EmloyeeClass(
                            rs.getString(1),
                            rs.getDate(2),
                            rs.getInt(3),
                            rs.getInt(4),
                            rs.getString(5),
                            rs.getDate(6),
                            rs.getString(7),
                            rs.getString(8),
                            rs.getString(9)
                    ));
                    table.setItems(EmployeeList);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
        delete.setOnAction(actionEvent ->
                showWork.showWindow("/Employee/DeleteEmployeeWindow.fxml", "Удаление"));
        update.setOnAction(actionEvent ->
                showWork.showWindow("/Employee/UpdateEmployeeWindow.fxml", "Обновление"));

        filter.setOnAction(actionEvent -> {
            if (!comboAddressWorkFilter.visibleProperty().getValue()){
                comboAddressWorkFilter.visibleProperty().setValue(true);
                comboEducationFilter.visibleProperty().setValue(true);
                comboPostFilter.visibleProperty().setValue(true);
            } else {
                comboAddressWorkFilter.visibleProperty().setValue(false);
                comboEducationFilter.visibleProperty().setValue(false);
                comboPostFilter.visibleProperty().setValue(false);
            }
        });

        List<String> postList = fill.fillList("SELECT name_post FROM public.post");
        List<String> educationList = fill.fillList("SELECT degree_education FROM public.education");
        List<String> workAdress = fill.fillList("SELECT work_adress FROM public.work_adress");

        comboPostFilter.setItems(FXCollections.observableArrayList(postList));
        comboAddressWorkFilter.setItems(FXCollections.observableArrayList(workAdress));
        comboEducationFilter.setItems(FXCollections.observableArrayList(educationList));

        fio.setCellValueFactory(new PropertyValueFactory<>("FIO"));
        birth.setCellValueFactory(new PropertyValueFactory<>("Birth"));
        beginWork.setCellValueFactory(new PropertyValueFactory<>("BeginWork"));
        education.setCellValueFactory(new PropertyValueFactory<>("Education"));
        series.setCellValueFactory(new PropertyValueFactory<>("Series"));
        nomer.setCellValueFactory(new PropertyValueFactory<>("Nomer"));
        post.setCellValueFactory(new PropertyValueFactory<>("Post"));
        phone.setCellValueFactory(new PropertyValueFactory<>("PhoneNumber"));
        place.setCellValueFactory(new PropertyValueFactory<>("WorkPlace"));
        ResultSet rs = db.Select(sqlSelect);
        while (rs.next()){
            EmployeeList.add(new EmloyeeClass(
                    rs.getString(1),
                    rs.getDate(2),
                    rs.getInt(3),
                    rs.getInt(4),
                    rs.getString(5),
                    rs.getDate(6),
                    rs.getString(7),
                    rs.getString(8),
                    rs.getString(9)
            ));
            table.setItems(EmployeeList);
        }
    }
}
