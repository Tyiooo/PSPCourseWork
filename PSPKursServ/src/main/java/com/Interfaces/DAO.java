package com.Interfaces;

import com.Models.Entities.Companies;

import java.util.List;

public interface DAO<T> {
    void save(T obj);
    void update(T obj);
    void delete(T obj);
    T findById(int id);
    List<Companies> findAllByUser(int userID);
    List<T> findAll();
}
