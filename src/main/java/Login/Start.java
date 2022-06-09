package Login;

import Main.Controllers.MainOrderController;
import javafx.application.Application;
import javafx.stage.Stage;
import WorkingFunctions.ShowWork;

public class Start extends Application {
    @Override
    public void start(Stage stage) {
        ShowWork show = new ShowWork();
        show.showWindow("/Login/LoginApp.fxml", "Вход");
        //show.showWindow("/Main/MainOrderWindow.fxml", "12 кресел");
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop(){
        MainOrderController.stop = true;
    }
}
