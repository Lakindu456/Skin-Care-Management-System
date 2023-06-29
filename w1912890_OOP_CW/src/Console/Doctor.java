package Console;

public class Doctor extends Person {
    private String medLiceNo;
    private String specialization;


    //Getters and setters
    public String getMedLiceNo() {
        return medLiceNo;
    }

    public void setMedLiceNo(String medLiceNo) {
        this.medLiceNo = medLiceNo;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
}
