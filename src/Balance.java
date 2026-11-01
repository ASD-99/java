package src;
import java.util.Objects;

public final class Balance {

    private final long amountKopecks;
    private final Currency currency;

    public Balance(long amountKopecks, Currency currency) {
        if (amountKopecks < 0) {
            throw new IllegalArgumentException("Сумма не может быть отрицательной: " + amountKopecks);
        }
        this.currency = Objects.requireNonNull(currency, "currency не может быть null");
        this.amountKopecks = amountKopecks;
    }

    public long getAmountKopecks() { return amountKopecks; }
    public Currency getCurrency() { return currency; }

    public Balance add(long amountKopecks) {
        if (amountKopecks < 0) {
            throw new IllegalArgumentException("Нельзя прибавлять отрицательную сумму: " + amountKopecks);
        }
        return new Balance(this.amountKopecks + amountKopecks, currency);
    }

    public Balance subtract(long amountKopecks) {
        if (amountKopecks < 0) {
            throw new IllegalArgumentException("Нельзя вычитать отрицательную сумму: " + amountKopecks);
        }
        if (amountKopecks > this.amountKopecks) {
            throw new IllegalStateException("Недостаточно средств: есть " + this.amountKopecks
                    + ", нужно " + amountKopecks);
        }
        return new Balance(this.amountKopecks - amountKopecks, currency);
    }

    public Balance convertTo(Currency target) {
        Objects.requireNonNull(target, "target не может быть null");
        if (target == currency) {
            return this;
        }
        long priceRub = currency.getPriceInRublesKopecks();
        long priceTarget = target.getPriceInRublesKopecks();
        long rubKopecks = amountKopecks * priceRub;
        long converted = rubKopecks / priceTarget;
        return new Balance(converted, target);
    }

    @Override
    public String toString() {
        long major = amountKopecks / 100;
        long minor = amountKopecks % 100;
        return String.format("%d.%02d %s (%s)", major, minor, currency.getCode(), currency.getSymbol());
    }
}