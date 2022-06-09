package WorkingFunctions;

import Employee.Struct.EmloyeeClass;
import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Optional;

public class ShowWork {
    public void showError(String header, String content){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошбика");
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    public Boolean showWarning(String header, String content) {
        boolean flag = false;
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Предупреждение");
        alert.setHeaderText(header);
        alert.setContentText(content);

        ButtonType yes = new ButtonType("Да");
        ButtonType no = new ButtonType("Нет");

        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(yes, no);

        Optional<ButtonType> option = alert.showAndWait();

        if(option.get() == yes){
            flag = true;
        }
        else if (option.get() == no){
            flag = false;
        }

        return flag;
    }

    public void showInfo(String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Успех!");
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }

    public void showWindow(String fxml, String title) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(fxml));

        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = loader.getRoot();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
        stage.setTitle(title);
        stage.setResizable(false);
        stage.show();
    }
    public static void showOneTimeTooltip(Control control, String tooltipText) {

        Point2D p = control.localToScreen(5 , 5);

        final Tooltip customTooltip = new Tooltip(tooltipText);
        customTooltip.show(control,p.getX(),p.getY());
        customTooltip.setAutoHide(true);
    }

}
