package Employee.Struct;

import java.util.Date;

public class EmloyeeClass {
    public String FIO;
    public Date Birth;
    public int Series;
    public int Nomer;
    public String Education;
    public Date BeginWork;

    public String Post;
    public String PhoneNumber;
    public String WorkPlace;

    public EmloyeeClass(String FIO, Date birth, int series, int nomer, String education, Date beginWork, String post, String phoneNumber, String workPlace) {
        this.FIO = FIO;
        this.Birth = birth;
        this.Series = series;
        this.Nomer = nomer;
        this.Education = education;
        this.BeginWork = beginWork;
        this.Post = post;
        this.PhoneNumber = phoneNumber;
        this.WorkPlace = workPlace;
    }

    public Date getBeginWork() {
        return BeginWork;
    }

    public void setBeginWork(Date BeginWork) {
        this.BeginWork = BeginWork;
    }

    public String getFIO() {
        return FIO;
    }

    public void setFIO(String FIO) {
        this.FIO = FIO;
    }

    public Date getBirth() {
        return Birth;
    }

    public void setBirth(Date birth) {
        Birth = birth;
    }

    public int getSeries() {
        return Series;
    }

    public void setSeries(int series) {
        Series = series;
    }

    public int getNomer() {
        return Nomer;
    }

    public void setNomer(int nomer) {
        Nomer = nomer;
    }

    public String getEducation() {
        return Education;
    }

    public void setEducation(String education) {
        Education = education;
    }

    public String getPost() {
        return Post;
    }

    public void setPost(String post) {
        Post = post;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getWorkPlace() {
        return WorkPlace;
    }

    public void setWorkPlace(String workPlace) {
        WorkPlace = workPlace;
    }
}
