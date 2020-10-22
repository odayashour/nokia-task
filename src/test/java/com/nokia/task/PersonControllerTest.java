package com.nokia.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Random;
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
    public void addIntTest() throws Exception {
       
        Mockito.when(personService.addPerson("111","Person"))
                .thenReturn(true);
        RequestBuilder requestBuilder =MockMvcRequestBuilders.post("/persons")
                                                            .param("id", "111")
                                                            .param("name","Person");
        MvcResult result= mockMvc.perform(requestBuilder).andReturn();
        assertTrue(Boolean.parseBoolean(result.getResponse().getContentAsString()));
    }

    @Test
    public void SearchIntTest() throws Exception {

        String name = "Person";
        Random random = new Random();
        List<Person> tempPersons = Stream.of(new Person("P" + random.nextInt(),name),
                                             new Person("P" + random.nextInt(),name)).collect(Collectors.toList());

        when(personService.findByName(name)).thenReturn(tempPersons);

        RequestBuilder requestBuilder =MockMvcRequestBuilders.get("/persons").param("name", name);
        MvcResult result= mockMvc.perform(requestBuilder).andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedStr = objectMapper.writeValueAsString(tempPersons);  
        JSONAssert.assertEquals(expectedStr, result.getResponse().getContentAsString(),false);
    }

    @Test
    public void deleteIntTest() throws Exception {
      
        String name = "Person";
        Mockito.when(personService.deleteByName(name))
        .thenReturn(1);

        RequestBuilder requestBuilder =MockMvcRequestBuilders.delete("/persons")
                                                            .param("name",name);
        MvcResult result= mockMvc.perform(requestBuilder).andReturn();
        assertEquals(1, Integer.parseInt(result.getResponse().getContentAsString()));
    }
    
    @Test
    public void getAllIntTest() throws Exception {
    
        Random random = new Random();
        List<Person> tempPersons = Stream.of(new Person("P" + random.nextInt(),"Person1"),
                                             new Person("P" + random.nextInt(),"Person2"),
                                             new Person("P" + random.nextInt(),"Person3")).collect(Collectors.toList());

        when(personService.getAll()).thenReturn(tempPersons);

        RequestBuilder requestBuilder =MockMvcRequestBuilders.get("/persons/all");
        MvcResult result= mockMvc.perform(requestBuilder).andReturn();
        ObjectMapper objectMapper = new ObjectMapper();
        String expectedStr = objectMapper.writeValueAsString(tempPersons);      
        JSONAssert.assertEquals(expectedStr, result.getResponse().getContentAsString(),false);
    }
}
