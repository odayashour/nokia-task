package com.nokia.task.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import com.nokia.task.model.Person;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    private static List<Person> persons;

    private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock readLock = rwl.readLock();
    private final Lock writeLock = rwl.writeLock();

    public PersonService() {
        persons = new ArrayList<Person>();
    }

    public List<Person> getAll() {

        try {
            readLock.lock();
            return persons;
        } finally {
            readLock.unlock();
        }
    }

    public List<Person> findByName(String name) {

        try {
            readLock.lock();
            return persons.stream().filter(p -> p.getName().equals(name)).collect(Collectors.toList());
        } finally {
            readLock.unlock();
        }
    }

    public int deleteByName(String name) {

        List<Person> listPersons = findByName(name);
        try {
            writeLock.lock();

            if (listPersons.size() > 0) {
                persons.removeAll(listPersons);
            }
            return listPersons.size();
        } finally {
            writeLock.unlock();
        }
    }

    public boolean addPerson(String id, String name) throws OutOfMemoryError {

        try {
            writeLock.lock();
            boolean isExists = persons.stream().filter(p -> p.getId().equals(id)).findFirst().isPresent();
            if (isExists == false){                  
                Person person = new Person(id,name);     
                persons.add(person);
                return true;            
            }
            return false;   
        } finally{
            writeLock.unlock();
        }
    }    
}
