package task.com.assccl;

public class TransactionModel {

    private String date;

    public TransactionModel(){

    }

    public TransactionModel(String date){
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
