package utils;

import java.util.List;

import org.hibernate.Query;
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

	public static List<String> getAllUrlFromHibernate() {

		SessionFactory sf = SessionFactoryUtil.getInstance();
		Session s = null;
		Transaction t = null;

		try {
			s = sf.openSession();
			t = s.beginTransaction();

			String hql = "select url from Project";
			Query query = s.createQuery(hql);
			List<String> list = query.list();
			System.out.println("数据库已经抓取了    "+list.size()+"  条数据");
			return list;

		} catch (Exception err) {
			err.printStackTrace();
		} finally {
			s.close();
		}
		return null;

	}

}
