package be.jsilkens.devbooks.shopping.api.controller;

import be.jsilkens.devbooks.common.domain.validation.Outcome;
import be.jsilkens.devbooks.shopping.api.model.AddCartItemRequestDTO;
import be.jsilkens.devbooks.shopping.domain.Book;
import be.jsilkens.devbooks.shopping.domain.CartItem;
import be.jsilkens.devbooks.shopping.domain.ShoppingCart;
import be.jsilkens.devbooks.shopping.domain.identifier.Isbn13;
import be.jsilkens.devbooks.shopping.domain.money.Currency;
import be.jsilkens.devbooks.shopping.domain.money.Money;
import be.jsilkens.devbooks.shopping.usecase.AddBookToCartUseCase;
import be.jsilkens.devbooks.shopping.usecase.RemoveBookFromCartUseCase;
import be.jsilkens.devbooks.shopping.usecase.ViewCartUseCase;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private AddBookToCartUseCase addBookToCartUseCase;

    @Mock
    private RemoveBookFromCartUseCase removeBookFromCartUseCase;

    @Mock
    private ViewCartUseCase viewCartUseCase;

    @InjectMocks
    private CartController cartController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cartController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("POST /api/cart/add should return 200 OK with updated cart")
    void addBookToCart_shouldReturn200_whenSuccessful() throws Exception {
        // GIVEN
        var isbn = "9780132350884";
        var request = new AddCartItemRequestDTO();
        request.setIsbn(isbn);

        var book = Book.builder()
                .withTitle("Clean Code")
                .withAuthor("Robert C. Martin")
                .withPublicationYear(2008)
                .withIsbn(new Isbn13(isbn))
                .withPrice(new Money(new BigDecimal("50.00"), Currency.EUR))
                .build();

        var cartItem = CartItem.builder()
                .withBook(book)
                .withQuantity(1)
                .build();

        var cart = ShoppingCart.builder()
                .withItems(new ArrayList<>(List.of(cartItem)))
                .build();

        when(addBookToCartUseCase.execute(isbn)).thenReturn(new Outcome.Success<>(cart));

        // WHEN & THEN
        mockMvc.perform(post("/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.books[0].title").value("Clean Code"))
                .andExpect(jsonPath("$.books[0].price.value").value(50.00))
                .andExpect(jsonPath("$.books[0].price.currency").value("EUR"))
                .andExpect(jsonPath("$.books[0].quantity").value(1))
                .andExpect(jsonPath("$.books[0].details_link").value("http://localhost/api/book/" + isbn))
                .andExpect(jsonPath("$.total_price.value").value(50.00))
                .andExpect(jsonPath("$.total_price.currency").value("EUR"));
    }

    @Test
    @DisplayName("POST /api/cart/add should return 404 NOT FOUND when book does not exist")
    void addBookToCart_shouldReturn404_whenBookNotFound() throws Exception {
        // GIVEN
        var isbn = "9780132350884";
        var request = new AddCartItemRequestDTO();
        request.setIsbn(isbn);

        when(addBookToCartUseCase.execute(isbn)).thenReturn(new Outcome.Failure<>("Book not found"));

        // WHEN & THEN
        mockMvc.perform(post("/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Book not found"));
    }

    @Test
    @DisplayName("POST /api/cart/add should return 400 BAD REQUEST when ISBN is invalid")
    void addBookToCart_shouldReturn400_whenIsbnInvalid() throws Exception {
        // GIVEN
        var isbn = "invalid-isbn";
        var request = new AddCartItemRequestDTO();
        request.setIsbn(isbn);

        when(addBookToCartUseCase.execute(isbn)).thenReturn(new Outcome.Failure<>("Invalid ISBN13"));

        // WHEN & THEN
        mockMvc.perform(post("/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Invalid ISBN13"));
    }

    @Test
    @DisplayName("DELETE /api/cart/remove/{isbn} should return 200 OK with updated cart")
    void removeBookFromCart_shouldReturn200_whenSuccessful() throws Exception {
        // GIVEN
        var isbn = "9780201616224";
        var remainingBook = Book.builder()
                .withTitle("The Pragmatic Programmer")
                .withAuthor("Andy Hunt")
                .withPublicationYear(1999)
                .withIsbn(new Isbn13(isbn))
                .withPrice(new Money(new BigDecimal("50.00"), Currency.EUR))
                .build();

        var cart = ShoppingCart.builder()
                .withItems(new ArrayList<>(List.of(
                        CartItem.builder().withBook(remainingBook).withQuantity(1).build()
                )))
                .build();

        when(removeBookFromCartUseCase.execute("9780132350884")).thenReturn(new Outcome.Success<>(cart));

        // WHEN & THEN
        mockMvc.perform(delete("/cart/remove/9780132350884")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.books[0].title").value("The Pragmatic Programmer"))
                .andExpect(jsonPath("$.total_price").value(50.00));
    }

    @Test
    @DisplayName("DELETE /api/cart/remove/{isbn} should return 404 when book not in cart")
    void removeBookFromCart_shouldReturn404_whenBookNotInCart() throws Exception {
        // GIVEN
        when(removeBookFromCartUseCase.execute("9781234567897"))
                .thenReturn(new Outcome.Failure<>("Book not found in cart"));

        // WHEN & THEN
        mockMvc.perform(delete("/cart/remove/9781234567897")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.detail").value("Book not found in cart"));
    }

    @Test
    @DisplayName("DELETE /api/cart/remove/{isbn} should return 400 when ISBN is invalid")
    void removeBookFromCart_shouldReturn400_whenIsbnInvalid() throws Exception {
        // GIVEN
        when(removeBookFromCartUseCase.execute("INVALID_ISBN"))
                .thenReturn(new Outcome.Failure<>("Invalid ISBN13"));

        // WHEN & THEN
        mockMvc.perform(delete("/cart/remove/INVALID_ISBN")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Invalid ISBN13"));
    }
}
