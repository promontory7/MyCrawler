package utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import model.Project;

public class HibernateUtil {
	public static void save2Hibernate(Project project) {
		SessionFactory sf = SessionFactoryUtil.getInstance();
		Session s = null;
		Transaction t = null;

		try {
			s = sf.openSession();
			t = s.beginTransaction();
			s.save(project);
			t.commit();
		} catch (Exception err) {
			t.rollback();
			err.printStackTrace();
		} finally {
			s.close();
		}
	}

}
