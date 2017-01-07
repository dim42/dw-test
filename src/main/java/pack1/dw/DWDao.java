package pack1.dw;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pack.db.Account;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Optional;

import static java.lang.String.format;

public class DWDao {
    private static final Logger log = LoggerFactory.getLogger(DWDao.class);
    private final SessionFactory sessionFactory;

    public DWDao() {
        sessionFactory = initSessionFactory();
    }

    private SessionFactory initSessionFactory() {
        SessionFactory sessionFactory = null;
        // configures settings from hibernate.cfg.xml
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
        try {
            sessionFactory = new MetadataSources(registry).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory so destroy it manually.
            StandardServiceRegistryBuilder.destroy(registry);
        }
        if (sessionFactory == null) {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        }
        return sessionFactory;
    }

    public AccountDto newAccount(String number) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            // for sqlite Hibernate adds constraint unique with error, so it's needed to check for uniqueness manually
            Optional<Account> accountOpt = getUniqueAccountOptional(session, number);
            if (accountOpt.isPresent()) {
                Account account = accountOpt.get();
                return new AccountDto(account.getId(), account.getNumber());
            }
            Account account = new Account(number);
            Serializable generatedIdentifier = session.save(account);
            session.getTransaction().commit();
            log.info("generatedIdentifier: {}", generatedIdentifier);
            return new AccountDto(generatedIdentifier, account.getNumber());
        }
    }

    public Account getAccount(String name) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Account account = getUniqueAccountOptional(session, name).orElseThrow(() -> new NoSuchElementException(format("Account %s is not found", name)));
            session.getTransaction().commit();
            return account;
        }
    }

    private Optional<Account> getUniqueAccountOptional(Session session, String name) {
        CriteriaBuilder builder = session.getEntityManagerFactory().getCriteriaBuilder();
        CriteriaQuery<Account> criteriaQuery = builder.createQuery(Account.class);
        Root<Account> root = criteriaQuery.from(Account.class);
        criteriaQuery = criteriaQuery.where(builder.equal(root.get("number"), name));
        Query<Account> query = session.createQuery(criteriaQuery);
        return query.uniqueResultOptional();
    }
}
