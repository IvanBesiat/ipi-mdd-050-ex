package com.ipiecoles.java.mdd050.controller;

import com.ipiecoles.java.mdd050.model.Manager;
import com.ipiecoles.java.mdd050.model.Technicien;
import com.ipiecoles.java.mdd050.repository.ManagerRepository;
import com.ipiecoles.java.mdd050.repository.TechnicienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value ="/techniciens")
public class TechnicienController {

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private TechnicienRepository technicienRepository;

    @RequestMapping(
            value = "/{techId}}/equipe/{managerMatricule}/add",
            method = RequestMethod.GET
    )
    public Manager addManagerToTech(@PathVariable Long techId, @PathVariable String managerMatricule){
        Technicien tech = technicienRepository.findById(techId).get();
        Manager manager = managerRepository.findByMatricule(managerMatricule);
        tech.setManager(manager);
        technicienRepository.save(tech);
        return manager;
    }
}
