package respository;

import model.Book;
import model.BorrowedBook;
import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import util.HibernateUtils;

import java.util.List;

public class BoorrowedBookRepository {
    public BorrowedBook get(Long id) {
        SessionFactory sessionFactory = HibernateUtils.getSessionFactory();
        Session session = sessionFactory.openSession();
        BorrowedBook book = session.find(BorrowedBook.class, id);
        session.close();
        return book;
    }

    public BorrowedBook add(BorrowedBook object) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        session.save(object);
        transaction.commit();
        session.close();
        return object;
    }

    public void delete(Long id) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        BorrowedBook o = get(id);
        session.delete(o);
        transaction.commit();
        session.close();
    }

    public List<BorrowedBook> getUserBorrowings(Long userId){
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        Query query = session.createQuery("select u from BorrowedBook u where u.user.id = :userId");
        query.setParameter("userId", userId);
        List<BorrowedBook> list = (List<BorrowedBook>)query.list();
        transaction.commit();
        session.close();
        return list;
    }
}
