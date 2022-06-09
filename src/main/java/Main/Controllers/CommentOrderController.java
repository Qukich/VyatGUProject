package Main.Controllers;

import WorkingFunctions.Fill;
import WorkingFunctions.ShowWork;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class CommentOrderController {
    @FXML
    private TextArea comment;

    @FXML
    private ComboBox<String> filler;
    @FXML
    private ComboBox<String> unposh;
    @FXML
    private ComboBox<String> accs;

    @FXML
    private Button add;
    @FXML
    private Button prompt;

    public String textComment;

    @FXML
    void initialize() throws SQLException {
        Fill fill = new Fill();

        List<String> fillerList = fill.fillList("SELECT name_filler FROM public.filler");
        List<String> unposhList = fill.fillList("SELECT name_upholstery_material FROM public.upholstery");
        List<String> accsList = fill.fillList("SELECT description_accessories FROM public.accessories");

        filler.setItems(FXCollections.observableArrayList(fillerList));
        unposh.setItems(FXCollections.observableArrayList(unposhList));
        accs.setItems(FXCollections.observableArrayList(accsList));

        prompt.setOnAction(arg0 -> ShowWork.showOneTimeTooltip(prompt, "Выпадающие меню нужны для легкого заполнения комментариев\nОсновную информацию надо заполнять без них"));

        filler.setOnAction(actionEvent ->
                comment.setText(comment.getText() + "Наполнитель: " + filler.getValue() + "\n"));
        unposh.setOnAction(actionEvent ->
                comment.setText(comment.getText() + "Обивка: " + unposh.getValue() + "\n"));
        accs.setOnAction(actionEvent ->
                comment.setText(comment.getText() + "Аксессуар: " + accs.getValue() + "\n"));

        add.setOnAction(actionEvent ->
                textComment = comment.getText());
        Stage stage = (Stage) add.getScene().getWindow();
        stage.close();
    }
}
