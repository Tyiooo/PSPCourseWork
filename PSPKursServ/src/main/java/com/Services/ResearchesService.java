package com.Services;

import com.DAO.ResearchesDAO;
import com.Interfaces.DAO;
import com.Interfaces.Service;
import com.Models.Entities.Researches;

import java.util.List;

public class ResearchesService implements Service<Researches> {
    DAO daoService = new ResearchesDAO();
    @Override
    public Researches findEntity(int id) {
        Researches entity = (Researches) daoService.findById(id);
        return entity;
    }

    @Override
    public void saveEntity(Researches entity) {
        daoService.save(entity);
    }

    @Override
    public void deleteEntity(Researches entity) {
        daoService.delete(entity);
    }

    @Override
    public void updateEntity(Researches entity) {
        daoService.update(entity);
    }

    public List<Researches> findAllEntitiesByUser(int companyID) {
        return daoService.findAllByUser(companyID);
    }

    @Override
    public List<Researches> findAllEntities() {
        return daoService.findAll();
    }
}
