package Main;

import java.util.Date;

public class MainStruct {
    private int id;
    private String fio;
    private String status;
    private String furn;
    private Date date_ord;
    private Date date_close;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFurn() {
        return furn;
    }

    public void setFurn(String furn) {
        this.furn = furn;
    }

    public Date getDate_ord() {
        return date_ord;
    }

    public void setDate_ord(Date date_ord) {
        this.date_ord = date_ord;
    }

    public Date getDate_close() {
        return date_close;
    }

    public void setDate_close(Date date_close) {
        this.date_close = date_close;
    }

    public MainStruct(int id, String fio, String status, String furn, Date date_ord, Date date_close) {
        this.id = id;
        this.fio = fio;
        this.status = status;
        this.furn = furn;
        this.date_ord = date_ord;
        this.date_close = date_close;
    }
}
