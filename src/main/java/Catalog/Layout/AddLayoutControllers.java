package Catalog.Layout;

import WorkingFunctions.DBwork;
import WorkingFunctions.FileWork;
import WorkingFunctions.Fill;
import WorkingFunctions.ShowWork;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class AddLayoutControllers {
    @FXML
    private Button add;
    @FXML
    private Button choosePhoto;

    @FXML
    private TextField name;
    @FXML
    private TextField price;
    @FXML
    private TextField filedPath;
    private Fill fill;

    private File filePath;

    @FXML
    void initialize(){
        fill = new Fill();
        String headerError = "Ошибка в добавлении";
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Фото макета");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Картинки", "*.jpg", "*.png", "*.bmp");
        fileChooser.getExtensionFilters().add(filter);
        filedPath.setEditable(false);

        Pattern p = Pattern.compile("(\\d+\\.?\\d*)?");
        price.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) price.setText(oldValue);
        });

        choosePhoto.setOnAction(actionEvent -> {
            Stage stage = (Stage) add.getScene().getWindow();
            File file = fileChooser.showOpenDialog(stage);
            if(file != null){
                filePath = file;
                filedPath.setText(file.getPath());
            }
        });

        add.setOnAction(actionEvent -> {
            fill = new Fill();
            DBwork db = new DBwork();
            ShowWork sw = new ShowWork();

            List<Integer> tmp_index;
            try {
                tmp_index = fill.fillListInt("SELECT MAX(id_layout) FROM public.layout");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            int index = tmp_index.get(0) + 1;

            if(Objects.equals(name.getText(), "")){
                sw.showError(headerError, "Заполните поле с названием наполнителя");
                return;
            }

            if(Objects.equals(price.getText(), "")){
                sw.showError(headerError, "Заполните поле с ценой наполнителя");
                return;
            }

            if(Objects.equals(filedPath.getText(), "")){
                sw.showError(headerError, "Выберите фотографию на компьютере");
                return;
            }

            FileWork fw = new FileWork();
            String file_name = name.getText() + ".jpg";
            String path = "src/main/resources/Catalog/Layout/LayoutImg/" + file_name;
            File file_tmp = new File(path);
            try {
                fw.copyFileUsingStream(filePath, file_tmp);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            String sql = "INSERT INTO public.layout VALUES(" + index + ", " + "'" + name.getText() + "', " +
                    "" + price.getText() + ", " + "'" + path + "');";

            try {
                db.Insert(sql);

                sw.showInfo("Добавление макета произошло успешно!", "");
                Stage stage = (Stage) add.getScene().getWindow();
                stage.close();
            } catch (SQLException throwable){
                sw.showError(headerError, "Ошибка при добавлении в базу");
                throwable.printStackTrace();
            }
        });
    }
}
