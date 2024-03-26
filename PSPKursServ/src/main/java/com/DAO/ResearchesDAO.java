package com.DAO;

import com.Interfaces.DAO;
import com.Models.Entities.Companies;
import com.Models.Entities.Researches;
import com.Utility.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ResearchesDAO implements DAO {
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
        Researches researches = session.get(Researches.class, id);
        session.close();
        return researches;
    }

    @Override
    public List findAll() {
        Session session =   HibernateSessionFactory.getSessionFactory().openSession();
        List<Object> researches = (List<Object>)session.createQuery("From Researches").list();
        session.close();
        return researches;
    }

    public List findAllByUser(int companyID){
        Session session =   HibernateSessionFactory.getSessionFactory().openSession();
        List<Object> researches = (List<Object>)session.createQuery("From Researches WHERE Company.id = :companyID")
                .setParameter("companyID", companyID).list();
        session.close();
        return researches;
    }
//    public List findAllEntitiesByOwner() {
//        Session session =   HibernateSessionFactory.getSessionFactory().openSession();
//        List<Object> personData = (List<Object>)session.createQuery("From Companies").list();
//        session.close();
//        return personData;
//    }
}
