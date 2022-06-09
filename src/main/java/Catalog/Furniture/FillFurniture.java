package Catalog.Furniture;

import WorkingFunctions.DBwork;
import WorkingFunctions.Fill;
import WorkingFunctions.ShowWork;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class FillFurniture {
    public static void fillFurniture(ComboBox<String> filler, ComboBox<String> layout, ComboBox<String> upholstery, ComboBox<String> accessories, ComboBox<String> FillerUnit,
                                     ComboBox<String> upholsteryUnit, TextField FillerCount, TextField UpholsteryCount, TextField AccessoriesCount, Fill fill,
                                     ComboBox<String> tip, TextField height, TextField width, TextField length,
                                     Button filler_show, Button layout_show, Button upholstery_show, Button accessories_show, ShowWork sw) throws SQLException {
        List<String> tipList = fill.fillList("SELECT description_type_furniture FROM public.type_furniture");
        List<String> layoutList = fill.fillList("SELECT name_layout FROM public.layout");
        List<String> fillerList = fill.fillList("SELECT name_filler FROM public.filler");
        List<String> upholsteryList = fill.fillList("SELECT name_upholstery_material FROM public.upholstery");
        List<String> accessoriesList = fill.fillList("SELECT description_accessories FROM public.accessories");
        List<String> unitFiller = fill.fillList("SELECT name_unit FROM public.unit_weight");
        List<String> unitUnposh = fill.fillList("SELECT name_unit FROM public.unit_area");

        tip.setItems(FXCollections.observableList(tipList));
        layout.setItems(FXCollections.observableList(layoutList));
        filler.setItems(FXCollections.observableList(fillerList));
        upholstery.setItems(FXCollections.observableList(upholsteryList));
        accessories.setItems(FXCollections.observableList(accessoriesList));
        FillerUnit.setItems(FXCollections.observableList(unitFiller));
        upholsteryUnit.setItems(FXCollections.observableList(unitUnposh));

        Pattern p = Pattern.compile("(\\d+\\.?\\d*)?");
        FillerCount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) FillerCount.setText(oldValue);
        });
        UpholsteryCount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) UpholsteryCount.setText(oldValue);
        });
        AccessoriesCount.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) AccessoriesCount.setText(oldValue);
        });
        height.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) height.setText(oldValue);
        });
        width.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) width.setText(oldValue);
        });
        length.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!p.matcher(newValue).matches()) length.setText(oldValue);
        });

        filler_show.setOnAction(actionEvent ->
                sw.showWindow("/Catalog/Filler/FillerMainWindow.fxml", "Макет"));
        layout_show.setOnAction(actionEvent ->
                sw.showWindow("/Catalog/Layout/MainLayoutWindow.fxml", "Макет"));
        upholstery_show.setOnAction(actionEvent ->
                sw.showWindow("/Catalog/Upholstery/UpholsteryMainWindow.fxml", "Макет"));
        accessories_show.setOnAction(actionEvent ->
                sw.showWindow("/Catalog/AccessoriesControllers/MainAccessoriesWindow.fxml", "Макет"));
    }

    public static void RasschetFurniture(Button rasschet, ComboBox<String> filler, ComboBox<String> layout, ComboBox<String> upholstery, ComboBox<String> accessories, ComboBox<String> fillerUnit, ComboBox<String> upholsteryUnit, TextField price, DBwork db, TextField fillerCount, TextField upholsteryCount, TextField accessoriesCount) {
        rasschet.setOnAction(actionEvent -> {
            int filler2 = filler.getSelectionModel().getSelectedIndex() + 1;
            int layout2 = layout.getSelectionModel().getSelectedIndex() + 1;
            int upholstery2 = upholstery.getSelectionModel().getSelectedIndex() + 1;
            int accessories2 = accessories.getSelectionModel().getSelectedIndex() + 1;
            int fillerUnit2 = fillerUnit.getSelectionModel().getSelectedIndex() + 1;
            int upholsteryUnit2 = upholsteryUnit.getSelectionModel().getSelectedIndex() + 1;
            String headerError = "Ошбика в рассчете цены";
            ShowWork showWork = new ShowWork();
            price.setText("0");

            double priceTMP = 0;

            if(layout2 > 0){
                try {
                    ResultSet rs = db.Select("SELECT price_layout FROM public.layout WHERE name_layout = '" + layout.getSelectionModel().getSelectedItem() + "';");
                    while (rs.next()){
                        priceTMP += rs.getDouble(1);
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                showWork.showError(headerError, "Выберите макет мебели"); return;
            }

            if (filler2 > 0 && !Objects.equals(fillerCount.getText(), "") && fillerUnit2 > 0){
                try {
                    ResultSet rs = db.Select("SELECT price_filler FROM public.filler WHERE name_filler = '" + filler.getSelectionModel().getSelectedItem() + "';");
                    double unit = 1;
                    while (rs.next()){
                        if(Objects.equals(fillerUnit.getSelectionModel().getSelectedItem(), "г")){
                            unit = 0.001;
                        }
                        double tmp = rs.getDouble(1) * Double.parseDouble(fillerCount.getText()) * unit;
                        priceTMP += tmp;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                showWork.showError(headerError, "Выберите наполнитель, и введите его количество, а также выберите еденицу измерения"); return;
            }

            if (upholsteryUnit2 > 0 && !Objects.equals(upholsteryCount.getText(), "") && upholstery2 > 0){
                try {
                    ResultSet rs = db.Select("SELECT price_upholstery_material FROM public.upholstery \n" +
                            "WHERE name_upholstery_material = '" + upholstery.getSelectionModel().getSelectedItem() + "';");
                    double unit = 1;
                    while (rs.next()){
                        if(Objects.equals(upholsteryUnit.getSelectionModel().getSelectedItem(), "см²")){
                            unit = 0.0001;
                        }
                        double tmp = rs.getDouble(1) * Double.parseDouble(upholsteryCount.getText()) * unit;
                        priceTMP += tmp;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                showWork.showError(headerError, "Выберите обивку, и введите её количество, а также выберите еденицу измерения"); return;
            }

            if (!Objects.equals(accessoriesCount.getText(), "") && accessories2 > 0){
                try {
                    ResultSet rs = db.Select("SELECT price_accessories FROM public.Accessories \n" +
                            "WHERE description_accessories = '" + accessories.getSelectionModel().getSelectedItem() + "';");
                    while (rs.next()){
                        double tmp = rs.getDouble(1) * Double.parseDouble(accessoriesCount.getText());
                        priceTMP += tmp;
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                showWork.showError(headerError, "Выберите обивку, и введите её количество, а также выберите еденицу измерения"); return;
            }

            price.setText(String.valueOf(priceTMP));
        });
    }
}
