package com.Utility;

import com.google.gson.Gson;
import com.Enums.ResponseStatus;
import com.Models.Entities.*;
import com.Models.TCP.Request;
import com.Models.TCP.Response;
import com.Services.*;

import javax.persistence.Persistence;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.List;

public class ClientThread implements Runnable {
    private Socket clientSocket;
    private Request request;
    private Response response;
    private Gson gson;
    private BufferedReader in;
    private PrintWriter out;


    private UserService userService = new UserService();

    private CompaniesService companiesService = new CompaniesService();

    private PersonDataService personDataService = new PersonDataService();

    private ResearchesService researchesService = new ResearchesService();

    private EmployeesService employeesService = new EmployeesService();


    public ClientThread(Socket clientSocket) throws IOException {
        response = new Response();
        request = new Request();
        this.clientSocket = clientSocket;
        gson = new Gson();
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = new PrintWriter(clientSocket.getOutputStream());
    }

    @Override
    public void run() {
        try {
            while (clientSocket.isConnected()) {
                String message = in.readLine();

                request = gson.fromJson(message, Request.class);
                switch (request.getRequestType()) {
                    case REGISTER: {
                        User user = gson.fromJson(request.getRequestMessage(), User.class);
                        if (userService.findAllEntities().stream().noneMatch(x -> x.getLogin().toLowerCase().equals(user.getLogin().toLowerCase()))) {
                            personDataService.saveEntity(user.getPersonData());
                            userService.saveEntity(user);
                            userService.findAllEntities();
                            User returnUser;
                            returnUser = userService.findEntity(user.getId());
                            returnUser.getPersonData().setUsers(null);
                            response = new Response(ResponseStatus.OK, "Готово!", gson.toJson(returnUser));
                        } else {
                            response = new Response(ResponseStatus.ERROR, "Такой пользователь уже существует!", "");
                        }
                        break;
                    }
                    case LOGIN: {
                        User requestUser = gson.fromJson(request.getRequestMessage(), User.class);
                        if (userService.findAllEntities().stream().anyMatch(x -> x.getLogin().toLowerCase().equals(requestUser.getLogin().toLowerCase())) && userService.findAllEntities().stream().anyMatch(x -> x.getPassword().equals(requestUser.getPassword()))) {
                            User user = userService.findAllEntities().stream().filter(x -> x.getLogin().toLowerCase().equals(requestUser.getLogin().toLowerCase())).findFirst().get();
                            user = userService.findEntity(user.getId());
                            user.getPersonData().setUsers(null);
                            for (Companies company : user.getCompanies()) {
                                company.setUser(null);
                                for (Researches research : company.getResearches()) {
                                    research.setCompany(null);
                                    for (Employees employee : research.getEmployees()) {
                                        employee.setResearch(null);
                                    }
                                }
                            }
                            response = new Response(ResponseStatus.OK, "Готово!", gson.toJson(user));
                        } else {
                            response = new Response(ResponseStatus.ERROR, "Такого пользователя не существует или неправильный пароль!", "");
                        }
                        break;
                    }
                    case READCOMPANIES:{
                        User requestUser = gson.fromJson(request.getRequestMessage(), User.class);
                        List<Companies> companies;
                        if(requestUser.getId() == 0){
                            companies =  companiesService.findAllEntities();
                        }
                        else {
                            companies = companiesService.findAllEntitiesByUser(requestUser.getId());
                        }
                        for (Companies company : companies) {
                            company.setUser(null);
                            for (Researches research : company.getResearches()) {
                                research.setCompany(null);
                                for (Employees employee : research.getEmployees()) {
                                    employee.setResearch(null);
                                }
                            }
                        }
                        response = new Response(ResponseStatus.OK, "Готово!", gson.toJson(companies));
                        break;
                    }
                    case CREATECOMPANIES: {
                        Companies requestCompanies = gson.fromJson(request.getRequestMessage(), Companies.class);
                        if (companiesService.findAllEntitiesByUser(requestCompanies.getUser().getId()).stream().noneMatch(x -> x.getCompanyName().toLowerCase().equals(requestCompanies.getCompanyName().toLowerCase()))) {
                            companiesService.saveEntity(requestCompanies);
                            response = new Response(ResponseStatus.OK, "Готово!", "");
                        } else {
                            response = new Response(ResponseStatus.ERROR, "Компания с таким названием у вас уже есть!", "");
                        }
                        break;
                    }
                    case DELETECOMPANIES: {
                        Companies requestCompanies = gson.fromJson(request.getRequestMessage(), Companies.class);
                        if (companiesService.findAllEntities().stream().anyMatch(x -> x.getId_company() == requestCompanies.getId_company())) {
                            companiesService.deleteEntity(requestCompanies);
                            response = new Response(ResponseStatus.OK, "Готово!", "");
                        } else {
                            response = new Response(ResponseStatus.ERROR, "Такой компании нет в БД(", "");
                        }
                        break;
                    }
                    case UPDATECOMPANIES: {
                        Companies requestCompanies = gson.fromJson(request.getRequestMessage(), Companies.class);
                        companiesService.updateEntity(requestCompanies);
                        response = new Response(ResponseStatus.OK, "Готово!", "");
                        break;
                    }
                    case READRESEARCHES:{
                        Companies requestCompanies = gson.fromJson(request.getRequestMessage(), Companies.class);
                        List<Researches> researches;
                        researches = researchesService.findAllEntitiesByUser(requestCompanies.getId_company());
                        for (Researches research : researches){
                            research.setCompany(null);
                            for (Employees employee : research.getEmployees()) {
                                employee.setResearch(null);
                            }
                        }
                        response = new Response(ResponseStatus.OK, "Готово!", gson.toJson(researches));
                        break;
                    }
                    case CREATERESEARCHES: {
                        Researches requestResearches = gson.fromJson(request.getRequestMessage(), Researches.class);
                        if (researchesService.findAllEntitiesByUser(requestResearches.getCompany().getId_company()).stream().noneMatch(x -> x.getResearchName().toLowerCase().equals(requestResearches.getResearchName().toLowerCase()))) {
                            researchesService.saveEntity(requestResearches);
                            response = new Response(ResponseStatus.OK, "Готово!", "");
                        } else {
                            response = new Response(ResponseStatus.ERROR, "Исслледование с таким названием у вас уже есть!", "");
                        }
                        break;
                    }
                    case DELETERESEARCHES: {
                        Researches requestResearches = gson.fromJson(request.getRequestMessage(), Researches.class);
                        if (researchesService.findAllEntities().stream().anyMatch(x -> x.getId_research() == requestResearches.getId_research())) {
                            researchesService.deleteEntity(requestResearches);
                            response = new Response(ResponseStatus.OK, "Готово!", "");
                        } else {
                            response = new Response(ResponseStatus.ERROR, "Такого исследования нет в БД(", "");
                        }
                        break;
                    }
                    case UPDATERESEARCHES: {
                        Researches requestResearches = gson.fromJson(request.getRequestMessage(), Researches.class);
                        researchesService.updateEntity(requestResearches);
                        response = new Response(ResponseStatus.OK, "Готово!", "");
                        break;
                    }
                    case READUSERS:{
                        List<User> users;
                        users = userService.findAllEntities();
                        for (User user : users){
                            user.setPersonData(null);
                            for (Companies company : user.getCompanies()) {
                                company.setUser(null);
                                for (Researches research : company.getResearches()) {
                                    research.setCompany(null);
                                    for (Employees employee : research.getEmployees()) {
                                        employee.setResearch(null);
                                    }
                                }
                            }
                        }
                        response = new Response(ResponseStatus.OK, "Готово!", gson.toJson(users));
                        break;
                    }
                    case CREATEUSERS: {
                        User requestUsers = gson.fromJson(request.getRequestMessage(), User.class);
                        if (userService.findAllEntities().stream().noneMatch(x -> x.getName().toLowerCase().equals(requestUsers.getName().toLowerCase()))) {
                            userService.saveEntity(requestUsers);
                            response = new Response(ResponseStatus.OK, "Готово!", "");
                        } else {
                            response = new Response(ResponseStatus.ERROR, "Пользователь с таким названием у вас уже есть!", "");
                        }
                        break;
                    }
                    case DELETEUSERS: {
                        User requestUsers = gson.fromJson(request.getRequestMessage(), User.class);
                        if (userService.findAllEntities().stream().anyMatch(x -> x.getName().toLowerCase().equals(requestUsers.getName().toLowerCase()))) {
                            userService.deleteEntity(requestUsers);
                            response = new Response(ResponseStatus.OK, "Готово!", "");
                        } else {
                            response = new Response(ResponseStatus.ERROR, "Такого пользователя нет в БД(", "");
                        }
                        break;
                    }
                    case UPDATEUSERS: {
                        User requestUsers = gson.fromJson(request.getRequestMessage(), User.class);
                        userService.updateEntity(requestUsers);
                        response = new Response(ResponseStatus.OK, "Готово!", "");
                        break;
                    }
                    case READEMPLOYEES:{
                        Researches requestResearches = gson.fromJson(request.getRequestMessage(), Researches.class);
                        List<Employees> employees;
                        employees = employeesService.findAllEntitiesByUser(requestResearches.getId_research());
                        for (Employees employee : employees) {
                            employee.setResearch(null);
                        }
                        response = new Response(ResponseStatus.OK, "Готово!", gson.toJson(employees));
                        break;
                    }
                    case CREATEEMPLOYEES: {
                        Employees requestEmployees = gson.fromJson(request.getRequestMessage(), Employees.class);
                        if (employeesService.findAllEntitiesByUser(requestEmployees.getResearch().getId_research()).stream().noneMatch(x -> x.getEmployeeName().toLowerCase().equals(requestEmployees.getEmployeeName().toLowerCase()))) {
                            employeesService.saveEntity(requestEmployees);
                            response = new Response(ResponseStatus.OK, "Готово!", "");
                        } else {
                            response = new Response(ResponseStatus.ERROR, "Сотрудник с таким именем у вас уже есть!", "");
                        }
                        break;
                    }
                    case DELETEEMPLOYEES: {
                        Employees requestEmployees = gson.fromJson(request.getRequestMessage(), Employees.class);
                        if (employeesService.findAllEntities().stream().anyMatch(x -> x.getId_employee() == requestEmployees.getId_employee())) {
                            employeesService.deleteEntity(requestEmployees);
                            response = new Response(ResponseStatus.OK, "Готово!", "");
                        } else {
                            response = new Response(ResponseStatus.ERROR, "Такого сотрудника нет в БД(!", "");
                        }
                        break;
                    }
                    case UPDATEEMPLOYEES: {
                        Employees requestEmployees = gson.fromJson(request.getRequestMessage(), Employees.class);
                        employeesService.updateEntity(requestEmployees);
                        response = new Response(ResponseStatus.OK, "Готово!", "");
                        break;
                    }
                    case SEXDIAGRAM:{
                        List<PersonData> personData;
                        personData =  personDataService.findAllEntities();
                        for(PersonData personData1 : personData) {
                            personData1.setUsers(null);
                        }
                        response = new Response(ResponseStatus.OK, "Готово!", gson.toJson(personData));
                        break;
                    }
                    case PRICEDIAGRAM:{
                        List<Researches> researches;
                        researches = researchesService.findAllEntities();
                        for (Researches research : researches){
                            research.setCompany(null);
                            for (Employees employee : research.getEmployees()) {
                                employee.setResearch(null);
                            }
                        }
                        response = new Response(ResponseStatus.OK, "Готово!", gson.toJson(researches));
                        break;
                    }
                    case SALARYDIAGRAM:{
                        List<Employees> employees;
                        employees = employeesService.findAllEntities();
                        for (Employees employee : employees) {
                            employee.setResearch(null);
                        }
                        response = new Response(ResponseStatus.OK, "Готово!", gson.toJson(employees));
                        break;
                    }
                    case COUNTRESEARCHES:{
                        Researches requestResearch = gson.fromJson(request.getRequestMessage(), Researches.class);
                        Researches research = researchesService.findEntity(requestResearch.getId_research());
                        List<Employees> employees;
                        employees = employeesService.findAllEntitiesByUser(requestResearch.getId_research());
                        int fullSalary = 0;
                        for (Employees employees1: employees){
                            fullSalary += employees1.getEmployeeSalary();
                        }
                        int result = fullSalary * research.getAmountOfDays() / 30 + research.getResearchCost();
                        requestResearch.setResearchCost(result);
                        response = new Response(ResponseStatus.OK, "Готово!", gson.toJson(requestResearch));
                        break;
                    }
                }
                out.println(gson.toJson(response));
                out.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Клиент " + clientSocket.getInetAddress() + ":" + clientSocket.getPort() + " закрыл соединение.");
            try {
                clientSocket.close();
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
