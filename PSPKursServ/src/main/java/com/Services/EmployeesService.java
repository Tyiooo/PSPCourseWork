package com.Services;

import com.DAO.EmployeesDAO;
import com.DAO.ResearchesDAO;
import com.Interfaces.DAO;
import com.Interfaces.Service;
import com.Models.Entities.Employees;
import com.Models.Entities.Researches;

import java.util.List;

public class EmployeesService implements Service<Employees> {
    DAO daoService = new EmployeesDAO();
    @Override
    public Employees findEntity(int id) {
        Employees entity = (Employees) daoService.findById(id);
        return entity;
    }

    @Override
    public void saveEntity(Employees entity) {
        daoService.save(entity);
    }

    @Override
    public void deleteEntity(Employees entity) {
        daoService.delete(entity);
    }

    @Override
    public void updateEntity(Employees entity) {
        daoService.update(entity);
    }

    public List<Employees> findAllEntitiesByUser(int researchID) {
        return daoService.findAllByUser(researchID);
    }

    @Override
    public List<Employees> findAllEntities() {
        return daoService.findAll();
    }
}
