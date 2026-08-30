package mylab.bank.control;

import mylab.bank.entity.Bank;
import mylab.bank.entity.CheckingAccount;
import mylab.bank.entity.SavingsAccount;
import mylab.bank.exception.AccountNotFoundException;
import mylab.bank.exception.InsufficientBalanceException;

public class BankDemo {

    public static void main(String[] args) {
        Bank bank = new Bank();

        System.out.println("=== 계좌 생성 ===");
        SavingsAccount account1 = bank.createSavingsAccount("홍길동", 10000, 3.0);
        CheckingAccount account2 = bank.createCheckingAccount("김철수", 20000, 5000);
        SavingsAccount account3 = bank.createSavingsAccount("이영희", 30000, 2.0);

        System.out.println();
        bank.displayAllAccounts();

        System.out.println();
        System.out.println("=== 입금/출금 테스트 ===");
        try {
            bank.deposit(account1.getAccountNumber(), 5000);
            bank.withdraw(account2.getAccountNumber(), 3000);
        } catch (AccountNotFoundException | InsufficientBalanceException e) {
            System.out.println("예외 발생: " + e.getMessage());
        }

        System.out.println();
        System.out.println("=== 이자 적용 테스트 ===");
        try {
            bank.applyInterest(account1.getAccountNumber());
        } catch (AccountNotFoundException e) {
            System.out.println("예외 발생: " + e.getMessage());
        }

        System.out.println();
        System.out.println("=== 계좌 이체 테스트 ===");
        try {
            bank.transfer(account3.getAccountNumber(), account2.getAccountNumber(), 5000);
        } catch (AccountNotFoundException | InsufficientBalanceException e) {
            System.out.println("예외 발생: " + e.getMessage());
        }

        System.out.println();
        bank.displayAllAccounts();

        try {
            bank.withdraw(account2.getAccountNumber(), 6000);
        } catch (AccountNotFoundException | InsufficientBalanceException e) {
            System.out.println("예외 발생: " + e.getMessage());
        }

        try {
            bank.withdraw(account2.getAccountNumber(), 10000);
        } catch (AccountNotFoundException | InsufficientBalanceException e) {
            System.out.println("예외 발생: " + e.getMessage());
        }

        try {
            bank.findAccount("AC9999");
        } catch (AccountNotFoundException e) {
            System.out.println("예외 발생: " + e.getMessage());
        }
    }
}
