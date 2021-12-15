package com.ipiecoles.java.mdd050.controller;

import com.ipiecoles.java.mdd050.model.Manager;
import com.ipiecoles.java.mdd050.model.Technicien;
import com.ipiecoles.java.mdd050.repository.ManagerRepository;
import com.ipiecoles.java.mdd050.repository.TechnicienRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value ="/managers")
public class ManagerController {

    @Autowired
    private TechnicienRepository technicienRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @RequestMapping(
            value = "/{managerId}/equipe/{techId}/remove",
            method = RequestMethod.GET,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeTechFromManager(@PathVariable Long managerId, @PathVariable Long techId){
        Technicien technicien = technicienRepository.findById(techId).get();
        technicien.setManager(null);
        technicienRepository.save(technicien);
    }

    @RequestMapping(
            value = "/{managerId}/equipe/{techMatricule}/add",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Technicien addTechToEquipe(@PathVariable Long managerId, @PathVariable String techMatricule){
        Technicien tech = technicienRepository.findByMatricule(techMatricule);
        Manager manager = managerRepository.findById(managerId).get();
        tech.setManager(manager);
        return technicienRepository.save(tech);
    }
}
