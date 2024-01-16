package com.libraryservice.controller;

import com.libraryservice.dto.BookDto;
import com.libraryservice.repository.BooksRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Testcontainers
@AutoConfigureMockMvc
@SpringBootTest
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BooksControllerTest {


    private String baseUrl = "/api/lib/books/";

    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BooksRepository booksRepository;

    @Container
    static MySQLContainer mySQLContainer = new MySQLContainer("mysql:latest").withDatabaseName("library_service_test");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", mySQLContainer::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    @BeforeAll
    static void beforeAll() {
        mySQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        mySQLContainer.stop();
    }

    private BookDto getBookRequest() {
        return BookDto.builder()
                .title("Made Familiar Maths")
                .level("Form 3")
                .genre("Maths")
                .available(true)
                .build();
    }

    private BookDto getUpdatedBookRequest() {
        return BookDto.builder()
                .title("Updated Title")
                .level("Updated Level")
                .genre("Updated Genre")
                .available(true)
                .build();
    }

    @Test
    @Order(1)
    void shouldAddANewBookToLibrary() throws Exception {
        BookDto bookDto = getBookRequest();
        String bookDtoString = objectMapper.writeValueAsString(bookDto);
        mockMvc.perform(MockMvcRequestBuilders.post(baseUrl + "create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookDtoString)
        ).andExpect(status().isCreated());
        Assertions.assertEquals(1, booksRepository.findAll().size());
    }

    @Test
    @Order(2)
    void shouldRetrieveBookInformationByBookId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "one/1"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Made Familiar Maths")) // Adjust with your expected title
                .andExpect(MockMvcResultMatchers.jsonPath("$.level").value("Form 3")) // Adjust with your expected level
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value("Maths")); // Adjust with your expected genre
    }

    @Test
    @Order(3)
    void shouldGetAllBooks() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "all"))
                .andExpect(status().isOk()) // Expect HTTP 200 OK
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray()) // Expect a JSON array in the response
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Made Familiar Maths")) // Adjust with your expected title for the first book
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].level").value("Form 3")) // Adjust with your expected level for the first book
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].genre").value("Maths")); // Adjust with your expected genre for the first book
    }


    @Test
    @Order(4)
    void getAvailableBooks() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "available"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray()) // Expect a JSON array in the response
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Made Familiar Maths"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].level").value("Form 3"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].genre").value("Maths"));

    }

    @Test
    @Order(5)
    void searchBooks() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get(baseUrl + "search")
                        .param("title", "YourSearchTitle")
                        .param("level", "YourSearchLevel")
                        .param("genre", "YourSearchGenre"))
                .andExpect(status().isOk()); // Expect HTTP 200 OK or the appropriate status code

//         Assertions.assertTrue(response.getContentAsString().contains("ExpectedResult"));
    }

    @Test
    @Order(6)
    void deleteBook() throws Exception {
        String bookIdToDelete = "delete/1";
        mockMvc.perform(MockMvcRequestBuilders.delete(baseUrl + bookIdToDelete))
                .andExpect(status().isNoContent());
        Assertions.assertEquals(0, booksRepository.findAll().size());
    }

    //    @Test
//    @Order(7)
    void updateBook() throws Exception {
        Long bookIdToUpdate = 1L;
        BookDto updatedBookDto = getUpdatedBookRequest();

        mockMvc.perform(MockMvcRequestBuilders.put(baseUrl + "update/" + bookIdToUpdate)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedBookDto)))
                .andExpect(status().isOk());
        Assertions.assertEquals(updatedBookDto.getTitle(), booksRepository.findById(bookIdToUpdate).orElseThrow().getTitle());
    }

}