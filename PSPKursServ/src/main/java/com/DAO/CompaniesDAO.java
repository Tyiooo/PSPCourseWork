package com.DAO;

import com.Interfaces.DAO;
import com.Models.Entities.Companies;
import com.Utility.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class CompaniesDAO implements DAO {
    @Override
    public void save(Object obj) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(obj);
        tx1.commit();
        session.close();
    }

    @Override
    public void update(Object obj) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.saveOrUpdate(obj);
        tx1.commit();
        session.close();
    }

    @Override
    public void delete(Object obj) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(obj);
        tx1.commit();
        session.close();
    }

    @Override
    public Object findById(int id) {
        Session session = HibernateSessionFactory.getSessionFactory().openSession();
        Companies companies = session.get(Companies.class, id);
        session.close();
        return companies;
    }

    @Override
    public List findAll() {
        Session session =   HibernateSessionFactory.getSessionFactory().openSession();
        List<Object> companies = (List<Object>)session.createQuery("From Companies").list();
        session.close();
        return companies;
    }

    public List findAllByUser(int userID){
        Session session =   HibernateSessionFactory.getSessionFactory().openSession();
        List<Object> companies = (List<Object>)session.createQuery("From Companies WHERE User.id = :userID")
                .setParameter("userID", userID).list();
        session.close();
        return companies;
    }
//    public List findAllEntitiesByOwner() {
//        Session session =   HibernateSessionFactory.getSessionFactory().openSession();
//        List<Object> personData = (List<Object>)session.createQuery("From Companies").list();
//        session.close();
//        return personData;
//    }
}
