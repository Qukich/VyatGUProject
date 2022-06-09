package Client.Struct;

public class ClientStruct {
    public String name;
    public String phone;
    public int card;
    public int bonusAmount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCard() {
        return card;
    }

    public void setCard(int card) {
        this.card = card;
    }

    public int getBonusAmount() {
        return bonusAmount;
    }

    public void setBonusAmount(int bonusAmount) {
        this.bonusAmount = bonusAmount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ClientStruct(String name, String phone, int card, int bonusAmount) {
        this.name = name;
        this.phone = phone;
        this.card = card;
        this.bonusAmount = bonusAmount;
    }
}
