package pack.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.cfg.Configuration;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

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
    private String number;

    public static void main(String[] args) {
//        Connection connection = DriverManager.getConnection("jdbc:sqlite::memory:");
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session session = sessionFactory.openSession();
        Account account = new Account(42L, "1234");
        Serializable generatedIdentifier = session.save(account);
        System.out.println(generatedIdentifier);
    }

    public Account() {
        // is used by Hibernate
    }

    public Account(Long id, String number) {
        this.id = id;
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
