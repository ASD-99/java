package src;
public class Main {

    public static void main(String[] args) {
        BankAccount alice = new BankAccount(
                "A-001", "Alice",
                new Balance(100_000, Currency.RUB),
                50_000);

        BankAccount bob = new BankAccount(
                "B-002", "Bob",
                new Balance(0, Currency.RUB),
                100_000);

        System.out.println("Начало:");
        System.out.println(alice);
        System.out.println(bob);

        alice.deposit(20_000);
        System.out.println("\nПосле deposit(200 ₽): " + alice);

        alice.transfer(bob, 30_000);
        System.out.println("\nПосле transfer(300 ₽) A->B:");
        System.out.println(alice);
        System.out.println(bob);

        try {
            alice.withdraw(60_000);
        } catch (RuntimeException e) {
            System.out.println("\nОжидаемо: " + e.getMessage());
        }

        try {
            bob.withdraw(40_000);
        } catch (RuntimeException e) {
            System.out.println("Ожидаемо: " + e.getMessage());
        }

        try {
            alice.deposit(-100);
        } catch (RuntimeException e) {
            System.out.println("Ожидаемо: " + e.getMessage());
        }

        Balance usd = alice.getBalance().convertTo(Currency.USD);
        System.out.println("\nБаланс Alice в USD: " + usd);
        System.out.println("Обратно в RUB: " + usd.convertTo(Currency.RUB));

        BankAccount carol = new BankAccount(
                "C-003", "Carol",
                new Balance(0, Currency.USD),
                1_000_000);
        try {
            alice.transfer(carol, 10_000);
        } catch (RuntimeException e) {
            System.out.println("\nОжидаемо: " + e.getMessage());
        }

        bob.block();
        try {
            alice.transfer(bob, 10_000);
        } catch (RuntimeException e) {
            System.out.println("\nОжидаемо: " + e.getMessage());
        }
        System.out.println("\nПосле неудачного перевода баланс Alice откатан: " + alice);
    }
}