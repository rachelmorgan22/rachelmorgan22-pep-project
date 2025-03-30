package Repository;

import Model.Account;

public class AccountRepository {

    public Account createAccount(Account newAccount) {
        return newAccount;  
    }

    public Account getAccountById(int id) {
        return new Account(id, "justanexample", "justanexample@mail.com");  
    }
}
