package com.ipiecoles.java.mdd050.controller;

import com.ipiecoles.java.mdd050.model.Commercial;
import com.ipiecoles.java.mdd050.model.Employe;
import com.ipiecoles.java.mdd050.repository.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value ="/employes")
public class EmployeController {

    @Autowired
    private EmployeRepository employeRepository;

    @RequestMapping(
            value = "/count",
            method = RequestMethod.GET)
    public Long countEmploye(){
        return employeRepository.count();
    }

    @RequestMapping(
            value = "/{Id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Employe getEmployeById(@PathVariable Long Id){
        Employe employeById = employeRepository.findById(Id).get();
        return employeById;
    }

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            params = "matricule"
    )
    public Employe getEmployeByMatricule(@RequestParam String matricule){
        return employeRepository.findByMatricule(matricule);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Page<Employe> getAllEmployeSorted(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "Id") String sortProperty,
            @RequestParam(defaultValue = "ASC") Sort.Direction sortDirection
    ){
        return employeRepository.findAll(PageRequest.of(page, size, sortDirection, sortProperty));
    }

    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public void insertEmploye(@RequestBody Employe employe){
        employeRepository.save(employe);
    }

    @RequestMapping(
            method = RequestMethod.PUT,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public void updateEmploye(@RequestBody Employe employe){
        employeRepository.save(employe);
    }
}
