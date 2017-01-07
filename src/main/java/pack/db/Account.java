package pack.db;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import static pack.db.Account.ACCOUNT_ENTITY_NAME;
import static pack.db.Account.ACCOUNT_TABLE_NAME;

@Entity(name = ACCOUNT_ENTITY_NAME)
@Table(name = ACCOUNT_TABLE_NAME)
public class Account {
    static final String ACCOUNT_ENTITY_NAME = "AccountEntity";
    static final String ACCOUNT_TABLE_NAME = "accounts";

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;
    @Column(nullable = false, unique = true)
    private String number;

    public Account() {
        // is used by Hibernate
    }

    public Account(String number) {
        this.number = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
