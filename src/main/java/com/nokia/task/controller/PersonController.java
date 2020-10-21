package com.nokia.task.controller;

import java.util.List;
import com.nokia.task.model.Person;
import com.nokia.task.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/persons")
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping
    public List<Person> search(@RequestParam String name){

        return personService.findByName(name);     
    }   

    @DeleteMapping
    public int delete(@RequestParam String name){

       return personService.deleteByName(name);
    }

    @PostMapping
    public  boolean add(@RequestParam String id, @RequestParam String name) throws OutOfMemoryError{
       
        Person person = new Person(id,name);
        return personService.addPerson(person);
    }

    @GetMapping("/all")
    public List<Person> getAll(){

        return personService.getAll();     
    }

    @PostMapping("/frombody")
    public  boolean addFromBody(@RequestBody Person person) throws OutOfMemoryError{
       
        return personService.addPerson(person);

    }
     
}
