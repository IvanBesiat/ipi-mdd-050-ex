package com.ipiecoles.java.mdd050.controller;

import com.ipiecoles.java.mdd050.exception.EmployeException;
import com.ipiecoles.java.mdd050.model.Employe;
import com.ipiecoles.java.mdd050.repository.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(value ="/employes")
public class EmployeController {

    @Autowired
    private EmployeRepository employeRepository;

    @RequestMapping(
            value = "/count",
            method = RequestMethod.GET)
    public Long countEmploye(){ return employeRepository.count(); }

    @RequestMapping(
            value = "/{Id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Optional<Employe> getEmployeById(@PathVariable Long Id) {
        if (employeRepository.findById(Id).isEmpty()) {
            throw new EntityNotFoundException("L'employé d'identifiant " + Id + " n'a pas été trouvé.");
        }
        return employeRepository.findById(Id);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE,
            params = "matricule"
    )
    public Employe getEmployeByMatricule(@RequestParam String matricule) throws EmployeException {
        if(!matricule.matches("(^[M,T,C][0-9]{5}$)")){
            throw new IllegalArgumentException("Le matricule doit contenir une lettre en majuscule (T,C ou M) et 5 chiffres.");
        }
        Employe employe = employeRepository.findByMatricule(matricule);
        if(employe != null){
            return employe;
        }
        throw new EntityNotFoundException("L'employé de matricule " + matricule + " n'a pas été trouvé.");
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
        //sortProperty n'est pas un attribut d'Employé => 400 BAD REQUEST
        List<String> properties = Arrays.asList("id","matricule","nom","prenom","salaire","dateEmbauche");
        if(!properties.contains(sortProperty))
            throw new IllegalArgumentException("La propriété de tri " + sortProperty + "est incorrecte.");
        //Valeurs négatives pour page et size => 400 BAD REQUEST
        if(page < 0 || size <= 0)
            throw new IllegalArgumentException("Les arguments page et size doivent être positif.");
        //contraindre size <= 50 => 400 BAD REQUEST
        if(size <50)
            throw new IllegalArgumentException("L'argument size doit être inférieur ou égale à 50.");
        //page et size cohérents par rapport au nombre de lignes de la table => 400 BAD REQUEST
        if(page*size > employeRepository.count())
            throw new IllegalArgumentException("Les arguments page et size doivent représenter des valeurs existantes.");
        return employeRepository.findAll(PageRequest.of(page, size, sortDirection, sortProperty));
    }

    @RequestMapping(
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Employe insertEmploye(@RequestBody Employe employe){
        //valeurs incompatibles avec le type de l'attribut => 400 BAD REQUEST
        //valeurs incorrectes (fonctionnel) => 400 BAD REQUEST
        //excède les limites de la base (ex : nom > 50 caractères) => 400 BAD REQUEST

        //Employé existe déjà (id, matricule existant) => 409 CONFLICT
        if(employeRepository.existsByMatricule(employe.getMatricule())
                || employeRepository.existsById(employe.getId()) && employe.getId() != null){
            throw new EntityExistsException("Un employé existe déjà avec cet identifiant ou ce matricule.");
        }
        return employeRepository.save(employe);
    }

    @RequestMapping(
            method = RequestMethod.PUT,
            value = "/{Id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Employe updateEmploye(@RequestBody Employe employe){
        //matricule modifié correspondant à un autre employé existant => 409 CONFLICT
        //valeurs incompatibles avec le type de l'attribut => 400 BAD REQUEST
        //valeurs incorrectes (fonctionnel) => 400 BAD REQUEST
        //excède les limites de la base (ex : nom > 50 caractères) => 400 BAD REQUEST
        //Vérifier que l'id de l'url correspond à l'id dans l'employé => 400 BAD REQUEST
        return employeRepository.save(employe);
    }

    @RequestMapping(
            method = RequestMethod.DELETE,
            value = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEmploye(@PathVariable Long id){

        employeRepository.deleteById(id);
    }
}
