package com.nokia.task.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.nokia.task.model.Person;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private static List<Person> persons = new ArrayList<Person>();

    public List<Person> getAll(){
        return persons;
    }

    public List<Person> findByName(String name){
        return persons.stream().filter(p-> p.getName().equals(name))
                               .collect(Collectors.toList());
    }

    public int deleteByName(String name){

        List<Person> listPersons = findByName(name);

        if (listPersons.size()>0){
            persons.removeAll(listPersons);
        }

        return listPersons.size();       
    }

    public boolean addPerson(Person person){
       
        boolean isExists= persons.stream()
                                 .filter(p-> p.getId().equals(person.getId()))
                                 .findFirst().isPresent();
        
        if (isExists == false){
            persons.add(person);
            return true;        
        } else {
            return false;
        }
    }    
}
