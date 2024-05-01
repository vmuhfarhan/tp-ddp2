package assignments.assignment3.payment;

public class DebitPayment implements DepeFoodPaymentSystem {
    //TODO implementasikan class di sini
    // Anda dibebaskan untuk membuat method yang diperlukan
    private static final double MINIMUM_TOTAL_PRICE = 50000;
    private long saldo;

    public DebitPayment(long saldo) {
        this.saldo = saldo;
    }

    public DebitPayment() {
        //TODO Auto-generated constructor stub
    }

    public long processPayment(long amount) {
        if (amount < MINIMUM_TOTAL_PRICE) {
            System.out.println("Jumlah pesanan < Rp 50000, mohon menggunakan metode pembayaran yang lain\n");
            return 0;
        }
        if (amount > saldo) {
            System.out.println("Saldo tidak mencukupi, mohon menggunakan metode pembayaran yang lain\n");
            return 0;
        }

        saldo -= amount;
        return saldo;
    }
}

