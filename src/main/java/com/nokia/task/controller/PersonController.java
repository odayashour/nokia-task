package com.nokia.task.controller;

import java.util.List;
import com.nokia.task.model.Person;
import com.nokia.task.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping
    public boolean add(@RequestParam String id, @RequestParam String name){                
        return personService.addPerson(id,name);
    }

    @DeleteMapping
    public int delete(@RequestParam String name){
       return personService.deleteByName(name);
    }

    @GetMapping("/all")
    public List<Person> getAll(){
        return personService.getAll();     
    }
}
