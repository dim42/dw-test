package pack.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
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
    private static final int NORMAL_TERMINATION = 0;

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    private Long id;
    private String number;

    public static void main(String[] args) {
        SessionFactory sessionFactory;
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory so destroy it manually.
            StandardServiceRegistryBuilder.destroy(registry);
            sessionFactory = new Configuration().configure().buildSessionFactory();
        }
        Session session = sessionFactory.openSession();
        Account account = new Account(42L, "1234");
        Serializable generatedIdentifier = session.save(account);
        System.out.println(generatedIdentifier);
        System.exit(NORMAL_TERMINATION);
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
