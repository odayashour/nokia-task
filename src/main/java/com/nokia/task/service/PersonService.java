package com.nokia.task.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import com.nokia.task.model.Person;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private static List<Person> persons;

    public PersonService(){
        persons = new ArrayList<Person>();
    }
    public List<Person> getAll(){
        return persons;
    }

    public List<Person> findByName(String name){
        return persons.stream().filter(p-> p.getName().equals(name))
                               .collect(Collectors.toList());
    }

    public synchronized int deleteByName(String name){

        List<Person> listPersons = findByName(name);

        if (listPersons.size()>0){
            persons.removeAll(listPersons);
        }
        return listPersons.size();       
    }

    /* TaskExceptionHandler class handle the thrown OutOfMemoryError */
    public synchronized boolean addPerson(String id,String name) throws OutOfMemoryError {
    
        boolean isExists= persons.stream()
                                 .filter(p-> p.getId().equals(id))
                                 .findFirst().isPresent();                            
        if (isExists == false){                  
            int sizeOfId =  8 * (int) ((((id.length()) * 2) + 45) / 8);
            int sizeOfName =  8 * (int) ((((name.length()) * 2) + 45) / 8);
            int sizeofObject= sizeOfId + sizeOfName + 16 ;

            if(Runtime.getRuntime().freeMemory() > sizeofObject){
                Person person = new Person(id,name);     
                persons.add(person);
                return true;
            }     
        }
        return false;      
    }    
}
