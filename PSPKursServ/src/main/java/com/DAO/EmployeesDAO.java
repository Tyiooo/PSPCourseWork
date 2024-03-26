package com.DAO;

import com.Interfaces.DAO;
import com.Models.Entities.Companies;
import com.Models.Entities.Employees;
import com.Utility.HibernateSessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class EmployeesDAO implements DAO {
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
        Employees employees = session.get(Employees.class, id);
        session.close();
        return employees;
    }

    @Override
    public List findAll() {
        Session session =   HibernateSessionFactory.getSessionFactory().openSession();
        List<Object> employees = (List<Object>)session.createQuery("From Employees").list();
        session.close();
        return employees;
    }

    public List findAllByUser(int researchesID){
        Session session =   HibernateSessionFactory.getSessionFactory().openSession();
        List<Object> employees = (List<Object>)session.createQuery("From Employees WHERE Research.id = :researchesID")
                .setParameter("researchesID", researchesID).list();
        session.close();
        return employees;
    }
//    public List findAllEntitiesByOwner() {
//        Session session =   HibernateSessionFactory.getSessionFactory().openSession();
//        List<Object> personData = (List<Object>)session.createQuery("From Companies").list();
//        session.close();
//        return personData;
//    }
}
