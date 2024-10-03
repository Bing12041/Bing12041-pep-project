package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    public Account register(Account account) {
        if (account.getUsername() == null || account.getUsername().isEmpty() ||
                account.getPassword() == null || account.getPassword().isEmpty() || account.getPassword().length() < 4) {
            return null;
        }
        return accountDAO.createAccount(account);
    }

    public Account login(String username, String password) {
        Account account = accountDAO.getAccountByUsername(username);
        if(account == null || !account.getPassword().equals(password)){
            return null;
        }
        return account;
    }

    public Account getAccountByID(int accountid) {
        return accountDAO.getAccountByID(accountid);
    }

}
