package WorkingFunctions;

import Catalog.AccessoriesControllers.MainAccessoriesController;
import Catalog.FillerControllers.FillerMainController;
import Catalog.Furniture.MainFurnitureController;
import Catalog.Layout.MainLayoutController;
import Catalog.UpholsteryControllers.UpholsteryMainController;
import Client.Controllers.UpdateClientController;
import Employee.Controller.AddEmployeeController;
import Login.LoginController;
import Main.Controllers.AddOrderController;
import Main.Controllers.CommentOrderController;
import Main.Controllers.MainOrderController;

public class ConstructName {
    private final static ConstructName instance = new ConstructName();
    public static ConstructName getInstance() {
        return instance;
    }

    private MainOrderController mainOrderController;
    public MainOrderController getMainOrderController() {
        return mainOrderController;
    }

    public void setMainOrderController(MainOrderController mainOrderController) {
        this.mainOrderController = mainOrderController;
    }

    private LoginController login;

    public LoginController getLogin() {
        return login;
    }

    public void setLogin(LoginController login) {
        this.login = login;
    }

    private FillerMainController fill;

    public FillerMainController getFill() {
        return fill;
    }

    public void setFill(FillerMainController fill) {
        this.fill = fill;
    }

    private MainAccessoriesController accs;

    public MainAccessoriesController getAccs() {
        return accs;
    }

    public void setAccs(MainAccessoriesController accs) {
        this.accs = accs;
    }

    private MainFurnitureController furn;

    public MainFurnitureController getFurn() {
        return furn;
    }

    public void setFurn(MainFurnitureController furn) {
        this.furn = furn;
    }

    private MainLayoutController lay;

    public MainLayoutController getLay() {
        return lay;
    }

    public void setLay(MainLayoutController lay) {
        this.lay = lay;
    }

    private UpholsteryMainController uph;

    public UpholsteryMainController getUph() {
        return uph;
    }

    public void setUph(UpholsteryMainController uph) {
        this.uph = uph;
    }

    private UpdateClientController upClient;

    public UpdateClientController getUpClient() {
        return upClient;
    }

    public void setUpClient(UpdateClientController upClient) {
        this.upClient = upClient;
    }

    private AddEmployeeController addEmp;

    public AddEmployeeController getAddEmp() {
        return addEmp;
    }

    public void setAddEmp(AddEmployeeController addEmp) {
        this.addEmp = addEmp;
    }

    private CommentOrderController comment;

    public CommentOrderController getComment() {
        return comment;
    }

    public void setComment(CommentOrderController comment) {
        this.comment = comment;
    }

    private AddOrderController addOrd;

    public AddOrderController getAddOrd() {
        return addOrd;
    }

    public void setAddOrd(AddOrderController addOrd) {
        this.addOrd = addOrd;
    }
}
