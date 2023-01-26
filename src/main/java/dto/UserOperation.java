package dto;

import java.io.Serializable;

public class UserOperation implements Serializable {
    String operation;
    String login;
    String password;

    Long borrowBookId;
    Long borrowedBookUserId;

    public UserOperation() {
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public Long getBorrowBookId() {
        return borrowBookId;
    }

    public void setBorrowBookId(Long borrowBookId) {
        this.borrowBookId = borrowBookId;
    }

    public Long getBorrowedBookUserId() {
        return borrowedBookUserId;
    }

    public void setBorrowedBookUserId(Long borrowedBookUserId) {
        this.borrowedBookUserId = borrowedBookUserId;
    }
}
