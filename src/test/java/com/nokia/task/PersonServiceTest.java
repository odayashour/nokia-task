package com.nokia.task;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import com.nokia.task.model.Person;
import com.nokia.task.service.PersonService;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PersonServiceTest {

    private PersonService personService = new PersonService();
    private Random random = new Random();

    @Test
    public void addTest() throws Exception {

        String randomId = "P" + random.nextInt();
        assertTrue(personService.addPerson(randomId, "Person"));
    }

    @Test
    public void addWithExistsIdTest() throws Exception {

        String randomId = "P" + random.nextInt();

        personService.addPerson(randomId, "Person1");
        assertFalse(personService.addPerson(randomId, "Person2"));
    }

    @Test
    public void getAllTest() throws Exception {

        personService.addPerson("P" + random.nextInt(), "Person1");
        personService.addPerson("P" + random.nextInt(), "Person2");
        personService.addPerson("P" + random.nextInt(), "Person3");
        assertEquals(3, personService.getAll().size());
    }

    @Test
    public void searchTest() throws Exception {

        personService.addPerson("P" + random.nextInt(), "Person1");
        personService.addPerson("P" + random.nextInt(), "Person1");
        personService.addPerson("P" + random.nextInt(), "Person2");
        assertEquals(2, personService.findByName("Person1").size());
    }

    @Test
    public void searchEmptyTest() throws Exception {

        personService.addPerson("P" + random.nextInt(), "Person1");
        assertEquals(0, personService.findByName("Person2").size());
    }

    @Test
    public void deleteTest() throws Exception {

        personService.addPerson("P" + random.nextInt(), "Person1");
        personService.addPerson("P" + random.nextInt(), "Person1");
        personService.addPerson("P" + random.nextInt(), "Person2");
        assertEquals(2, personService.deleteByName("Person1"));
    }

    @Test
    public void emptyDeleteTest() throws Exception {
        assertEquals(0, personService.deleteByName("Person"));
    }

    @Test
    public void concurencyAddTest() throws Exception {
        Thread thread1 = new Thread(() -> {
        

            for (int i = 0; i < 100; i++) {
                personService.addPerson("p" + i, "name" + i);
            }
        });

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                personService.addPerson("p" + i, "name" + i);
            }
        });
        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        assertEquals(100, personService.getAll().size());
    }

    @Test
    public void concurencyTest() throws Exception {

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        executorService.execute(()->{
            for (int i = 0; i < 10; i++) {
                personService.addPerson("p" + i, "name" + i);
            }
        });

        executorService.execute(()->{
            for (int i = 0; i < 7; i++) {
                personService.deleteByName("name" + i);
            }
        });
        executorService.awaitTermination(1,TimeUnit.SECONDS);
        Callable<List<Person>> taskRead = () -> {
                List<Person> persons = new ArrayList<Person>();
                for (int i = 7; i < 10; i++) {
                    persons.addAll(personService.findByName("name" + i));
                }
                 return persons;
        };

        Future<List<Person>> future = executorService.submit(taskRead);
        List<Person> persons = future.get();
        assertEquals(3, persons.size());
    }

    @Test
    public void concurencyAddSearchTest() throws Exception {

        Thread thread1 = new Thread(() -> {   
            for (int i = 0; i < 100; i++) {
                personService.addPerson("p" + i, "name" + i);
            }
        });

        thread1.start();
        ExecutorService executorService = Executors.newSingleThreadExecutor();      
        Callable<List<Person>> taskRead = () -> {
            List<Person> persons = new ArrayList<Person>();
            for (int i = 0; i < 100; i++) {
                persons.addAll(personService.findByName("name" + i));
            }
             return persons;
         };

        Future<List<Person>> future = executorService.submit(taskRead);
        List<Person> personList = future.get();

        assertEquals(100, personList.size());
    }
}
