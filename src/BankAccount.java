package src;
import java.util.Objects;

public class BankAccount {

    private final String id;
    private final String owner;
    private Balance balance;
    private long withdrawalLimitKopecks;
    private boolean active;

    public BankAccount(String id, String owner, Balance balance, long withdrawalLimitKopecks) {
        this.id = Objects.requireNonNull(id, "id не может быть null");
        if (id.isBlank()) throw new IllegalArgumentException("id не может быть пустым");
        this.owner = Objects.requireNonNull(owner, "owner не может быть null");
        this.balance = Objects.requireNonNull(balance, "balance не может быть null");
        setWithdrawalLimitKopecks(withdrawalLimitKopecks);
        this.active = true;
    }

    public Balance getBalance() { return balance; }
    public long getWithdrawalLimitKopecks() { return withdrawalLimitKopecks; }
    public boolean isActive() { return active; }

    public void setWithdrawalLimitKopecks(long limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("Лимит не может быть отрицательным: " + limit);
        }
        this.withdrawalLimitKopecks = limit;
    }

    public void block() { this.active = false; }
    public void unblock() { this.active = true; }

    public void deposit(long amountKopecks) {
        ensureActive();
        if (amountKopecks <= 0) {
            throw new IllegalArgumentException("Сумма пополнения должна быть > 0: " + amountKopecks);
        }
        balance = balance.add(amountKopecks);
    }

    public void withdraw(long amountKopecks) {
        ensureActive();
        if (amountKopecks <= 0) {
            throw new IllegalArgumentException("Сумма снятия должна быть > 0: " + amountKopecks);
        }
        if (amountKopecks > withdrawalLimitKopecks) {
            throw new IllegalStateException("Превышен лимит на вывод: лимит "
                    + withdrawalLimitKopecks + ", запрошено " + amountKopecks);
        }
        balance = balance.subtract(amountKopecks);
    }

    public void transfer(BankAccount target, long amountKopecks) {
        Objects.requireNonNull(target, "target не может быть null");
        if (target.balance.getCurrency() != this.balance.getCurrency()) {
            throw new IllegalArgumentException(
                    "Валюты счетов не совпадают: " + this.balance.getCurrency()
                            + " vs " + target.balance.getCurrency());
        }
        this.withdraw(amountKopecks);
        try {
            target.deposit(amountKopecks);
        } catch (RuntimeException e) {
            this.balance = this.balance.add(amountKopecks);
            throw e;
        }
    }

    private void ensureActive() {
        if (!active) {
            throw new IllegalStateException("Счёт " + id + " заблокирован");
        }
    }

    @Override
    public String toString() {
        return String.format("BankAccount{id='%s', owner='%s', balance=%s, limit=%d коп., active=%s}",
                id, owner, balance, withdrawalLimitKopecks, active);
    }
}