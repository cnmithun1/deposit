package task.com.assccl;

public class CurrencyModel {

    private String INR;

    public String getINR() {
        return INR;
    }

    public void setINR(String INR) {
        this.INR = INR;
    }


    public class CurrencyModelResponse{
        private CurrencyModel rates;

        public CurrencyModel getRates() {
            return rates;
        }

        public void setRates(CurrencyModel rates) {
            this.rates = rates;
        }
    }
}
