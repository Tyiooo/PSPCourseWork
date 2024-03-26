package com.Services;

import com.DAO.PersonDataDAO;
import com.Interfaces.DAO;
import com.Interfaces.Service;
import com.Models.Entities.PersonData;
import com.Models.Entities.User;

import java.util.List;

public class PersonDataService implements Service<PersonData> {
    DAO daoService = new PersonDataDAO();

    @Override
    public PersonData findEntity(int id) {
        PersonData entity = (PersonData) daoService.findById(id);
        return entity;
    }

    @Override
    public void saveEntity(PersonData entity) {
        daoService.save(entity);
    }

    @Override
    public void deleteEntity(PersonData entity) {
        daoService.delete(entity);
    }

    @Override
    public void updateEntity(PersonData entity) {
        daoService.update(entity);
    }

    @Override
    public List<PersonData> findAllEntities() {
        return daoService.findAll();
    }
}
