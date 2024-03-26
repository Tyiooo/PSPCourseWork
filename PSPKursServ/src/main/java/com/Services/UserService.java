package com.Services;

import com.DAO.UserDAO;
import com.Interfaces.DAO;
import com.Interfaces.Service;
import com.Models.Entities.Companies;
import com.Models.Entities.Researches;
import com.Models.Entities.User;


import java.util.List;

public class UserService implements Service<User> {
    DAO daoService = new UserDAO();
    @Override
    public User findEntity(int id) {
        User entity = (User) daoService.findById(id);
        return entity;
    }

    @Override
    public void saveEntity(User entity) {
        daoService.save(entity);
    }

    @Override
    public void deleteEntity(User entity) {
        daoService.delete(entity);
    }

    @Override
    public void updateEntity(User entity) {
        daoService.update(entity);
    }

    public List<User> findAllEntitiesByUser(int persondataID) {
        return daoService.findAllByUser(persondataID);
    }

    @Override
    public List<User> findAllEntities() {
        return daoService.findAll();
    }
}
