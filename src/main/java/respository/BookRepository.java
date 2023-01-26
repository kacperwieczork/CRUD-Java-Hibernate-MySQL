package respository;

import model.Book;
import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import util.HibernateUtils;

import java.util.ArrayList;
import java.util.List;

public class BookRepository{

    public List<Book> getAll() {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        Session session = sessionFactory.openSession();
        try {
            return session.createCriteria(Book.class).list();
        } catch (Exception e) {
            return new ArrayList<>();
        } finally {
            session.close();
        }
    }

    public Book get(Long id) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        Session session = sessionFactory.openSession();
        Book book = session.find(Book.class, id);
        session.close();
        return book;
    }

    public Book add(Book object) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(object);
        transaction.commit();
        session.close();
        return object;
    }

}
