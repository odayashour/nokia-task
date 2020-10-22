package com.nokia.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Random;

import com.nokia.task.service.PersonService;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PersonServiceTest {
      
    private PersonService personService = new PersonService();
    private Random random = new Random();

    @Test
    public void addTest()throws Exception{

        String randomId = "P" + random.nextInt();
        assertTrue(personService.addPerson(randomId,"Person"));   
    }

    @Test
    public void addWithExistsIdTest()throws Exception{

        String randomId = "P" + random.nextInt();

        personService.addPerson(randomId,"Person1");
        assertFalse(personService.addPerson(randomId,"Person2"));  
    }

    @Test
    public void getAllTest()throws Exception{
       
        personService.addPerson("P" + random.nextInt(),"Person1");
        personService.addPerson("P" + random.nextInt(),"Person2");
        personService.addPerson("P" + random.nextInt(),"Person3");
        assertEquals(3, personService.getAll().size());
    }

    @Test
    public void searchTest()throws Exception{
       
        personService.addPerson("P" + random.nextInt(),"Person1");
        personService.addPerson("P" + random.nextInt(),"Person1");
        personService.addPerson("P" + random.nextInt(),"Person2");
        assertEquals(2, personService.findByName("Person1").size());
    }

    @Test
    public void searchEmptyTest()throws Exception{
       
        personService.addPerson("P" + random.nextInt(),"Person1");
        assertEquals(0, personService.findByName("Person2").size());
    }

    @Test
    public void deleteTest()throws Exception{

        personService.addPerson("P" + random.nextInt(),"Person1");
        personService.addPerson("P" + random.nextInt(),"Person1");
        personService.addPerson("P" + random.nextInt(),"Person2");
        assertEquals(2, personService.deleteByName("Person1"));
    }

    @Test
    public void emptyDeleteTest()throws Exception{

        PersonService pService = new PersonService(); 
        assertEquals(0, pService.deleteByName("Person"));
    }
}
