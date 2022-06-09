package Catalog.Furniture;

import WorkingFunctions.DBwork;
import WorkingFunctions.Fill;
import WorkingFunctions.ShowWork;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class UpdateFurnitureController {
    @FXML
    private TextField nameSearch;
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
    private Button update;
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

    @FXML
    void initialize() throws SQLException {
        db = new DBwork();
        fill = new Fill();
        ShowWork sw1 = new ShowWork();

        FillFurniture.fillFurniture(filler, layout, upholstery, accessories, FillerUnit, upholsteryUnit,
                FillerCount, UpholsteryCount, AccessoriesCount, fill, tip, height, width, length, filler_show,
                layout_show, upholstery_show, accessories_show, sw1);

        nameSearch.setOnKeyPressed(actionEvent -> {
            if (!Objects.equals(nameSearch.getText(), "") && nameSearch.getLength() >= 2) {
                try {
                    String sql = "SELECT id_type_furniture, id_layout, id_upholstery_material, height, width, length, id_accessories, id_filler, price_furniture,\n" +
                            "j1.count_filler, j1.id_unit_filler, j1.count_upholstery, j1.id_unit_upholstery, j1.count_accessories\n" +
                            "FROM public.furniture AS j0\n" +
                            "JOIN public.to_manufacture AS j1 ON j0.id_to_manufacture = j1.id_to_manufacture\n" +
                            "WHERE name_furniture LIKE '%" + nameSearch.getText() + "%';";
                    ResultSet rs = db.Select(sql);
                    while (rs.next()) {
                        tip.getSelectionModel().select(rs.getInt(1));
                        layout.getSelectionModel().select(rs.getInt(2) - 1);
                        upholstery.getSelectionModel().select(rs.getInt(3) - 1);
                        height.setText(rs.getString(4));
                        width.setText(rs.getString(5));
                        length.setText(rs.getString(6));
                        accessories.getSelectionModel().select(rs.getInt(7) - 1);
                        filler.getSelectionModel().select(rs.getInt(8) - 1);
                        price.setText(String.valueOf(rs.getDouble(9)));
                        FillerCount.setText(String.valueOf(rs.getInt(10)));
                        FillerUnit.getSelectionModel().select(rs.getInt(11) - 1);
                        UpholsteryCount.setText(String.valueOf(rs.getInt(12)));
                        upholsteryUnit.getSelectionModel().select(rs.getInt(13) - 1);
                        AccessoriesCount.setText(String.valueOf(rs.getInt(14)));
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else if (Objects.equals(nameSearch.getText(), "") || nameSearch.getLength() < 2) {
                tip.getSelectionModel().clearSelection();
                layout.getSelectionModel().clearSelection();
                upholstery.getSelectionModel().clearSelection();
                FillerUnit.getSelectionModel().clearSelection();
                upholsteryUnit.getSelectionModel().clearSelection();
                accessories.getSelectionModel().clearSelection();
                filler.getSelectionModel().clearSelection();
                height.setText("");
                width.setText("");
                length.setText("");
                price.setText("");
                FillerCount.setText("");
                UpholsteryCount.setText("");
                AccessoriesCount.setText("");

                tip.setButtonCell(new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText("Тип мебели");
                        } else {
                            setText(item);
                        }
                    }
                });

                layout.setButtonCell(new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText("Макет");
                        } else {
                            setText(item);
                        }
                    }
                });

                upholstery.setButtonCell(new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText("Обивка");
                        } else {
                            setText(item);
                        }
                    }
                });

                accessories.setButtonCell(new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText("Акссесуары");
                        } else {
                            setText(item);
                        }
                    }
                });

                filler.setButtonCell(new ListCell<>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText("Наполнитель");
                        } else {
                            setText(item);
                        }
                    }
                });
            }
        });

        FillFurniture.RasschetFurniture(rasschet, filler, layout, upholstery, accessories,
                FillerUnit, upholsteryUnit, price, db, FillerCount, UpholsteryCount, AccessoriesCount);

        update.setOnAction(actionEvent -> {
            db = new DBwork();
            ShowWork sw = new ShowWork();
            StringBuilder sql = new StringBuilder();
            StringBuilder sqlManuf = new StringBuilder();
            int indexManuf = 0;
            String headerError = "Ошибка в обновлении";

            boolean start = sw.showWarning("Вы пересчитали цену мебели?", "");
            if (start){
                int filler2 = filler.getSelectionModel().getSelectedIndex() + 1;
                int layout2 = layout.getSelectionModel().getSelectedIndex() + 1;
                int tip2 = tip.getSelectionModel().getSelectedIndex() + 1;
                int upholstery2 = upholstery.getSelectionModel().getSelectedIndex() + 1;
                int accessories2 = accessories.getSelectionModel().getSelectedIndex() + 1;
                int fillerUnit2 = FillerUnit.getSelectionModel().getSelectedIndex() + 1;
                int upholsteryUnit2 = upholsteryUnit.getSelectionModel().getSelectedIndex() + 1;

                sql.append("UPDATE public.furniture SET ");
                sqlManuf.append("UPDATE public.to_manufacture SET ");

                try {
                    List<String> tmp = fill.fillList("SELECT name_furniture from public.furniture");
                    boolean flag = tmp.contains(nameSearch.getText());
                    if(!flag) {
                        sw.showError(headerError, "Такой мебели нет в базе"); return;}
                    ResultSet rs = db.Select("SELECT id_to_manufacture FROM public.furniture WHERE name_furniture = '" + nameSearch.getText() + "';");
                    while (rs.next()){
                        indexManuf = rs.getInt(1);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                if(!Objects.equals(FillerCount.getText(), "")){
                    sqlManuf.append("count_filler = ").append(FillerCount.getText()).append(",");
                }

                if(fillerUnit2 > 0){
                    sqlManuf.append("id_unit_filler = ").append(fillerUnit2).append(",");
                }

                if(!Objects.equals(UpholsteryCount.getText(), "")){
                    sqlManuf.append("count_upholstery = ").append(UpholsteryCount.getText()).append(",");
                }

                if(upholsteryUnit2 > 0){
                    sqlManuf.append("id_unit_upholstery = ").append(upholsteryUnit2).append(",");
                }

                if(!Objects.equals(AccessoriesCount.getText(), "")){
                    sqlManuf.append("count_accessories = ").append(AccessoriesCount.getText()).append(",");
                }

                if (tip2 > 0){
                    sql.append("id_type_furniture = ").append(tip2).append(",");
                }

                if (layout2 > 0){
                    sql.append("id_layout = ").append(layout2).append(",");
                }

                if (upholstery2 > 0){
                    sql.append("id_upholstery_material = ").append(upholstery2).append(",");
                }

                if(!Objects.equals(height.getText(), "")){
                    sql.append("height = '").append(height.getText()).append("',");
                }

                if (!Objects.equals(width.getText(), "")){
                    sql.append("width = '").append(width.getText()).append("',");
                }

                if (!Objects.equals(length.getText(), "")){
                    sql.append("length = '").append(length.getText()).append("',");
                }

                if(accessories2 > 0){
                    sql.append("id_accessories = ").append(accessories2).append(",");
                }

                if(filler2 > 0){
                    sql.append("id_filler = ").append(filler2).append(",");
                }
                if (!Objects.equals(price.getText(), "")){
                    sql.append("price_furniture = ").append(price.getText()).append(",");
                }

                sql.setLength(sql.length() - 1);
                sqlManuf.setLength(sqlManuf.length() - 1);

                sql.append(" WHERE name_furniture = '").append(nameSearch.getText()).append("';");
                sqlManuf.append(" WHERE id_to_manufacture = '").append(indexManuf).append("';");

                try {
                    db.Update(sql.toString());
                    db.Update(sqlManuf.toString());

                    sw.showInfo("Обновление мебели произошло успешно!", "");
                    Stage stage = (Stage) update.getScene().getWindow();
                    stage.close();
                } catch (SQLException throwable){
                    sw.showError(headerError, "Ошибка при обновлении значений в базе");
                    throwable.printStackTrace();
                }
            }
        });
    }
}
