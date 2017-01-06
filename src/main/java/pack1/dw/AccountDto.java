package pack1.dw;

import java.io.Serializable;

public class AccountDto {
    private Serializable id;
    private String number;

    public AccountDto(Serializable id, String number) {
        this.id = id;
        this.number = number;
    }

    public Serializable getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }
}
