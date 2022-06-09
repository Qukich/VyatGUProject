package Catalog.Furniture;

import WorkingFunctions.DBwork;
import WorkingFunctions.Fill;
import WorkingFunctions.ShowWork;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class AddFurnitureController {
    @FXML
    private TextField name;
    @FXML
    private TextField height;
    @FXML
    private TextField width;
    @FXML
    private TextField length;
    @FXML
    private TextField price;
    @FXML
    private TextField FillerCount;
    @FXML
    private TextField UpholsteryCount;
    @FXML
    private TextField AccessoriesCount;

    @FXML
    private Button add;
    @FXML
    private Button layout_show;
    @FXML
    private Button filler_show;
    @FXML
    private Button upholstery_show;
    @FXML
    private Button accessories_show;
    @FXML
    private Button rasschet;

    @FXML
    private ComboBox<String> tip;
    @FXML
    private ComboBox<String> layout;
    @FXML
    private ComboBox<String> filler;
    @FXML
    private ComboBox<String> upholstery;
    @FXML
    private ComboBox<String> accessories;
    @FXML
    private ComboBox<String> FillerUnit;
    @FXML
    private ComboBox<String> upholsteryUnit;

    private DBwork db;
    private Fill fill;
    private ShowWork sw;

    @FXML
    void initialize() throws SQLException {
        db = new DBwork();
        fill = new Fill();
        sw = new ShowWork();
        price.setEditable(false);
        price.setText("0");

        FillFurniture.fillFurniture(filler, layout, upholstery, accessories, FillerUnit, upholsteryUnit,
                FillerCount, UpholsteryCount, AccessoriesCount, fill, tip, height, width, length, filler_show,
                layout_show, upholstery_show, accessories_show, sw);

        FillFurniture.RasschetFurniture(rasschet, filler, layout, upholstery, accessories, FillerUnit,
                upholsteryUnit, price, db, FillerCount, UpholsteryCount, AccessoriesCount);

        add.setOnAction(actionEvent ->{
            db = new DBwork();
            ShowWork showWork = new ShowWork();
            String headerError = "Ошибка в добавлении";

            int filler2 = filler.getSelectionModel().getSelectedIndex() + 1;
            int layout2 = layout.getSelectionModel().getSelectedIndex() + 1;
            int tip2 = tip.getSelectionModel().getSelectedIndex() + 1;
            int upholstery2 = upholstery.getSelectionModel().getSelectedIndex() + 1;
            int accessories2 = accessories.getSelectionModel().getSelectedIndex() + 1;
            int fillerUnit2 = FillerUnit.getSelectionModel().getSelectedIndex() + 1;
            int upholsteryUnit2 = upholsteryUnit.getSelectionModel().getSelectedIndex() + 1;

            List<Integer> tmp_index;
            try {
                tmp_index = fill.fillListInt("SELECT MAX(id_furniture) FROM public.furniture");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            int index = tmp_index.get(0) + 1;

            List<Integer> tmp_index_manufacture;
            try {
                tmp_index_manufacture = fill.fillListInt("SELECT MAX(id_to_manufacture) FROM public.to_manufacture");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            int index_manufacture = tmp_index_manufacture.get(0) + 1;

            if (Objects.equals(name.getText(), "")) {
                showWork.showError(headerError, "Заполните поле с именем мебели");
                return;
            }

            if (tip2 <= 0){
                showWork.showError(headerError, "Выберите тип мебели");
                return;
            }

            if (layout2 <= 0){
                showWork.showError(headerError, "Выберите макет мебели");
                return;
            }

            if (upholstery2 <= 0){
                showWork.showError(headerError, "Выберите материал обивки мебели");
                return;
            }

            if (Objects.equals(height.getText(), "")){
                showWork.showError(headerError, "Введите высоту мебели");
                return;
            }

            if (Objects.equals(width.getText(), "")){
                showWork.showError(headerError, "Введите ширину мебели");
                return;
            }

            if (Objects.equals(length.getText(), "")){
                showWork.showError(headerError, "Введите длинну мебели");
                return;
            }

            if (accessories2 <= 0){
                showWork.showError(headerError, "Выберите акксесуары мебели");
                return;
            }

            if (filler2 <= 0){
                showWork.showError(headerError, "Выберите наполнитель мебели");
                return;
            }

            if (Double.parseDouble(price.getText()) == 0){
                showWork.showError(headerError, "Рассчитайте цену мебели");
                return;
            }

            if (Objects.equals(FillerCount.getText(), "")){
                showWork.showError(headerError, "Введите количетсво наполнителя");
                return;
            }
            if (fillerUnit2 <= 0){
                showWork.showError(headerError, "Выберите единицу измерения наполнителя");
                return;
            }

            if (Objects.equals(UpholsteryCount.getText(), "")){
                showWork.showError(headerError, "Введите количетсво обивки");
                return;
            }
            if (upholsteryUnit2 <= 0){
                showWork.showError(headerError, "Выберите единицу измерения обивки");
                return;
            }

            if (Objects.equals(AccessoriesCount.getText(), "")){
                showWork.showError(headerError, "Введите количетсво акксесуаров"); return;
            }

            String sql = "INSERT INTO public.furniture VALUES(" + index + ", " + "'" + name.getText() + "', " +
                    "" + tip2 + ", " + "" + layout2 + ", " + "" + upholstery2 + ", " +
                    "" + height.getText() + ", " + "" + width.getText() + ", " + "" + length.getText() + ", " +
                    "" + accessories2 + ", " + "" + filler2 + ", " + "" + price.getText() + ", " + index_manufacture + ");";

            String sqlManuf = "INSERT INTO public.to_manufacture VALUES(" + index_manufacture + ", " + "" + FillerCount.getText() + ", " +
                    "" + fillerUnit2 + ", " + "" + UpholsteryCount.getText() + ", " + "" + upholsteryUnit2 + ", " +
                    "" + AccessoriesCount.getText() + ");";

            try {
                db.Insert(sqlManuf);
                db.Insert(sql);

                showWork.showInfo("Добавление мебели произошло успешно!", "");
                Stage stage = (Stage) add.getScene().getWindow();
                stage.close();
            } catch (SQLException throwable){
                sw.showError(headerError, "Ошибка при добавлении в базу");
                throwable.printStackTrace();
            }
        });
    }
}
