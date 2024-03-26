package com.Services;

import com.DAO.CompaniesDAO;
import com.Interfaces.DAO;
import com.Interfaces.Service;
import com.Models.Entities.Companies;


import java.util.List;

public class CompaniesService implements Service<Companies> {
    DAO daoService = new CompaniesDAO();
    //@Override
    public Companies findEntity(int id) {
        Companies entity = (Companies) daoService.findById(id);
        return entity;
    }

    @Override
    public void saveEntity(Companies entity) {
        daoService.save(entity);
    }

    @Override
    public void deleteEntity(Companies entity) {
        daoService.delete(entity);
    }

    @Override
    public void updateEntity(Companies entity) {
        daoService.update(entity);
    }

    public List<Companies> findAllEntitiesByUser(int userID) {
        return daoService.findAllByUser(userID);
    }

    @Override
    public List<Companies> findAllEntities() {
        return daoService.findAll();
    }
}
