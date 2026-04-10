package be.jsilkens.devbooks.shopping.api.controller;

import be.jsilkens.devbooks.common.domain.validation.Outcome;
import be.jsilkens.devbooks.shopping.domain.Book;
import be.jsilkens.devbooks.shopping.domain.identifier.Isbn13;
import be.jsilkens.devbooks.shopping.domain.money.Currency;
import be.jsilkens.devbooks.shopping.domain.money.Money;
import be.jsilkens.devbooks.shopping.usecase.GetBookByISBNUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    private MockMvc mockMvc;

    @Mock
    private GetBookByISBNUseCase getBookByISBNUseCase;

    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("GET /book/{isbn} should return 200 OK when book is found")
    void getBookByIsbn_shouldReturn200_whenBookFound() throws Exception {
        // GIVEN
        var isbn = "9780132350884";
        var book = Book.builder()
                .withTitle("Clean Code")
                .withAuthor("Robert C. Martin")
                .withPublicationYear(2008)
                .withIsbn(new Isbn13(isbn))
                .withPrice(new Money(new BigDecimal("50.00"), Currency.EUR))
                .build();

        when(getBookByISBNUseCase.execute(isbn)).thenReturn(new Outcome.Success<>(Optional.of(book)));

        // WHEN & THEN
        mockMvc.perform(get("/book/{isbn}", isbn)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Clean Code"))
                .andExpect(jsonPath("$.author").value("Robert C. Martin"))
                .andExpect(jsonPath("$.isbn").value(isbn))
                .andExpect(jsonPath("$.price.value").value(50.00))
                .andExpect(jsonPath("$.price.currency").value("EUR"));
    }

    @Test
    @DisplayName("GET /book/{isbn} should return 404 NOT FOUND when book is not found")
    void getBookByIsbn_shouldReturn404_whenBookNotFound() throws Exception {
        // GIVEN
        var isbn = "9780132350884";
        when(getBookByISBNUseCase.execute(isbn)).thenReturn(new Outcome.Success<>(Optional.empty()));

        // WHEN & THEN
        mockMvc.perform(get("/book/{isbn}", isbn)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Book not found"));
    }

    @Test
    @DisplayName("GET /book/{isbn} should return 400 BAD REQUEST when ISBN validation fails")
    void getBookByIsbn_shouldReturn400_whenIsbnValidationFails() throws Exception {
        // GIVEN
        String isbn = "invalid-isbn";
        when(getBookByISBNUseCase.execute(isbn)).thenReturn(new Outcome.Failure<>("Invalid ISBN13"));

        // WHEN & THEN
        mockMvc.perform(get("/book/{isbn}", isbn)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Invalid ISBN13"));
    }
}
