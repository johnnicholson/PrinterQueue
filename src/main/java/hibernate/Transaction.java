package hibernate;

import static hibernate.HibernateUtil.getFactory;

import org.hibernate.JDBCException;
import org.hibernate.Session;

public abstract class Transaction<T> {
  
  public static final int MAX_RETRIES = 10;
  
  public abstract T action();

  public final T run() {
    Session session = getFactory().getCurrentSession();
    int counter = MAX_RETRIES;
    boolean done = false;
    T val = null;
    while (!done && counter > 0) {
      try {
        session.beginTransaction();
        val = action();
        session.getTransaction().commit();
        done = true;
      } catch (JDBCException e) {
        e.printStackTrace();
        session.getTransaction().rollback();
        counter--;
      }
    }
    return val;
  }

}
