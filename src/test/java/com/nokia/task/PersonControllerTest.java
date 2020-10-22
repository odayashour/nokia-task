package com.nokia.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nokia.task.controller.PersonController;
import com.nokia.task.model.Person;
import com.nokia.task.service.PersonService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@RunWith(SpringRunner.class)
@WebMvcTest(value = PersonController.class)
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Test
    public void addTest()throws Exception{

        PersonService personServ = new PersonService();
        Person person = new Person("123","Person");

        assertTrue(personServ.addPerson(person));   
    }

    @Test
    public void addWithExistIdTest()throws Exception{

        PersonService personServ = new PersonService();
        Person person1 = new Person("111","Person1");
        Person person2 = new Person("111","Person2");

        personServ.addPerson(person1);
        assertFalse(personServ.addPerson(person2));   
    }

    @Test
    public void searchTest()throws Exception{

        PersonService personServ = new PersonService();
        Person person1 = new Person("111","Person1");
        Person person2 = new Person("112","Person1");
        Person person3 = new Person("113","Person2");

        List<Person> pList = new ArrayList<Person>();
        pList.add(person1);
        pList.add(person2);

        personServ.addPerson(person1);
        personServ.addPerson(person2);
        personServ.addPerson(person3);

        assertEquals(pList, personServ.findByName("Person1"));
    }

    @Test
    public void deleteTest()throws Exception{

        PersonService personServ = new PersonService();
        Person person1 = new Person("111","Person1");
        Person person2 = new Person("112","Person1");
        Person person3 = new Person("113","Person2");

        personServ.addPerson(person1);
        personServ.addPerson(person2);
        personServ.addPerson(person3);

        assertEquals(2, personServ.deleteByName("Person1"));
    }

    @Test
    public void addIntTest() throws Exception {

        Mockito.when(personService.addPerson(Mockito.any(Person.class)))
        .thenReturn(true);

        RequestBuilder requestBuilder =MockMvcRequestBuilders.post("/persons")
                                                            .param("id", "111")
                                                            .param("name","person1");
        MvcResult result= mockMvc.perform(requestBuilder).andReturn();

        assertTrue(Boolean.parseBoolean(result.getResponse().getContentAsString()));
    }

    @Test
    public void SearchIntTest() throws Exception {

        String name = "oday";
        List<Person> tempPersons = Stream.of(new Person("111","oday"),
                                             new Person("112","oday")).collect(Collectors.toList());

        when(personService.findByName(name)).thenReturn(tempPersons);

            RequestBuilder requestBuilder =MockMvcRequestBuilders.get("/persons").param("name", name);
            MvcResult result= mockMvc.perform(requestBuilder).andReturn();
            ObjectMapper objectMapper = new ObjectMapper();
            String expectedStr = objectMapper.writeValueAsString(tempPersons);  

            JSONAssert.assertEquals(expectedStr, result.getResponse().getContentAsString(),false);
    }

    @Test
    public void deleteIntTest() throws Exception {

        Mockito.when(personService.deleteByName("person1"))
        .thenReturn(1);

        RequestBuilder requestBuilder =MockMvcRequestBuilders.delete("/persons")
                                                            .param("name","person1");
        MvcResult result= mockMvc.perform(requestBuilder).andReturn();

        assertEquals(1, Integer.parseInt(result.getResponse().getContentAsString()));
    }
    
    @Test
    public void getAllIntTest() throws Exception {

        List<Person> tempPersons = Stream.of(new Person("111","p1"),
                                             new Person("112","p2"),
                                             new Person("113","p3")).collect(Collectors.toList());

        when(personService.getAll()).thenReturn(tempPersons);

        RequestBuilder requestBuilder =MockMvcRequestBuilders.get("/persons/all");
        MvcResult result= mockMvc.perform(requestBuilder).andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedStr = objectMapper.writeValueAsString(tempPersons);      
        
        JSONAssert.assertEquals(expectedStr, result.getResponse().getContentAsString(),false);
    }
}
