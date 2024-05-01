package assignments.assignment3.payment;
    //TODO: implementasikan class yang implement interface di sini
    // Anda dibebaskan untuk membuat method yang diperlukan

public class CreditCardPayment implements DepeFoodPaymentSystem {
    private static final double TRANSACTION_FEE_PERCENTAGE = 0.02;

    public CreditCardPayment() {
        //TODO Auto-generated constructor stub
    }

    //Method untuk mengembalikan total harga yang akan dibayarkan credit card
    public long processPayment(long amount) {
        double transactionFee = amount * TRANSACTION_FEE_PERCENTAGE;
        long totalAmount = (long) (amount + transactionFee);
        return totalAmount;
    }

    //Method untuk mengembalikan transaction fee
    public double countTransactionFee(long amount) {
        return amount * TRANSACTION_FEE_PERCENTAGE;
    }
}


