package WorkingFunctions;

public class CheckTextField {
    public boolean checkNumb(String text, String title){
        ShowWork sw = new ShowWork();
        try {
            Double.parseDouble(text);
            return true;
        } catch (NumberFormatException e){
            sw.showError("Введено не число в " + title, "");
            return false;
        }
    }
}
