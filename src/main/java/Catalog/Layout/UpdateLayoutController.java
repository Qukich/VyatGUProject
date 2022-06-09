package Catalog.Layout;

import WorkingFunctions.DBwork;
import WorkingFunctions.FileWork;
import WorkingFunctions.Fill;
import WorkingFunctions.ShowWork;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class UpdateLayoutController {
    @FXML
    private TextField price;
    @FXML
    private TextField pathFile;

    @FXML
    private Button update;
    @FXML
    private Button addFile;

    @FXML
    private ComboBox<String> nameLayout;

    private DBwork db;
    private File filePath;

    @FXML
    void initialize() throws SQLException{
        Fill fill = new Fill();
        db = new DBwork();
        List<String> nameLay = fill.fillList("SELECT name_layout FROM public.layout");
        nameLayout.setItems(FXCollections.observableList(nameLay));
        pathFile.setEditable(false);

        Pattern p = Pattern.compile("(\\d+\\.?\\d*)?");
        price.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches())
                price.setText(oldValue);
        });

        nameLayout.setOnAction(actionEvent ->{
            String name_tmp = nameLayout.getValue();
            try {
                ResultSet rs = db.Select("SELECT price_layout, path_to_image_layout FROM public.layout\n" +
                        "WHERE name_layout = '" + name_tmp + "'");
                while(rs.next()){
                    price.setText(rs.getString(1));
                    pathFile.setText(rs.getString(2));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        addFile.setOnAction(actionEvent ->{
            Stage stage = (Stage) update.getScene().getWindow();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Выбрать фото");
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Картинки", "*.jpg", "*.png", "*.bmp");
            fileChooser.getExtensionFilters().add(filter);
            File file = fileChooser.showOpenDialog(stage);
            if(file != null){
                filePath = file;
                pathFile.setText(file.getPath());
            }
        });

        update.setOnAction(actionEvent ->{
            ShowWork sw = new ShowWork();
            StringBuilder sql = new StringBuilder();

            sql.append("UPDATE public.layout SET ");

            if(!Objects.equals(price.getText(), "")){
                sql.append("price_layout = ").append(price.getText()).append(",");
            }
            if(!Objects.equals(pathFile.getText(), "")){
                try {
                    ResultSet rs = db.Select("SELECT path_to_image_layout FROM public.layout WHERE name_layout = '" + nameLayout.getValue()  +"';");
                    while (rs.next()){
                        if(!Objects.equals(pathFile.getText(), rs.getString(1))){
                            FileWork fw = new FileWork();
                            String file_name = nameLayout.getValue() + ".jpg";
                            String path = "src/main/resources/Catalog/Layout/LayoutImg/" + file_name;
                            File file_tmp = new File(path);
                            try {
                                fw.copyFileUsingStream(filePath, file_tmp);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            sql.append("path_to_image_layout = '").append(path).append("',");
                        }
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            sql.setLength(sql.length() - 1);

            sql.append(" WHERE name_layout = '").append(nameLayout.getValue()).append("';");

            try {
                db.Update(sql.toString());

                sw.showInfo("Обновление макета произошло успешно!", "");
                Stage stage = (Stage) update.getScene().getWindow();
                stage.close();
            } catch (SQLException throwable){
                sw.showError("Ошибка в обновлении", "Ошибка при обновлении значений в базе");
                throwable.printStackTrace();
            }
        });
    }
}
