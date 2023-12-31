package br.com.barbearia.udia.barbeiro;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class CadastraNovoBarbeiroControllerTest {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private BarbeiroRepository barbeiroRepository;

    @BeforeEach
    void setUp() {
        barbeiroRepository.deleteAll();
    }

    @Test
    @DisplayName("deve cadastrar um barbeiro valido")
    void test1() throws Exception {

        var request = new NovoBarbeiroRequest("thiago", "241.659.160-64");

        var payload = mapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/barbeiro")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        var barbeiro = barbeiroRepository.findAll();
        Assertions.assertEquals(1, barbeiro.size());

    }

    @Test
    @DisplayName("deve dar erro pois CPF é obrigatorio")
    void test2() throws Exception {

        var request = new NovoBarbeiroRequest("thiago", null);

        var payload = mapper.writeValueAsString(request);

        var resolvedException = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/barbeiro")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept-Language", "pt-br"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResolvedException();

        Assertions.assertNotNull(resolvedException);
        Assertions.assertEquals(MethodArgumentNotValidException.class, resolvedException.getClass());
        Assertions.assertEquals("não deve estar em branco", ((MethodArgumentNotValidException) resolvedException)
                .getBindingResult()
                .getFieldError().
                getDefaultMessage());
        Assertions.assertEquals("cpf", ((MethodArgumentNotValidException) resolvedException)
                .getBindingResult()
                .getFieldError().
                getField());

    }

    @Test
    @DisplayName("deve dar erro pois nome é obrigatorio")
    void test3() throws Exception {

        var request = new NovoBarbeiroRequest("", "241.659.160-64");

        var payload = mapper.writeValueAsString(request);

        var resolvedException = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/barbeiro")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept-Language", "pt-br"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResolvedException();

        Assertions.assertNotNull(resolvedException);
        Assertions.assertEquals(MethodArgumentNotValidException.class, resolvedException.getClass());
        Assertions.assertEquals("não deve estar em branco", ((MethodArgumentNotValidException) resolvedException)
                .getBindingResult()
                .getFieldError().
                getDefaultMessage());
        Assertions.assertEquals("nome", ((MethodArgumentNotValidException) resolvedException)
                .getBindingResult()
                .getFieldError().
                getField());

    }


    @Test
    @DisplayName("deve dar erro pois CPF deve ser válido")
    void test5() throws Exception {

        var request = new NovoBarbeiroRequest("thiago", "123");

        var payload = mapper.writeValueAsString(request);

        var resolvedException = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/barbeiro")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept-Language", "pt-br"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResolvedException();


        Assertions.assertNotNull(resolvedException);
        Assertions.assertEquals(MethodArgumentNotValidException.class, resolvedException.getClass());
        Assertions.assertEquals("número do registro de contribuinte individual brasileiro (CPF) inválido", ((MethodArgumentNotValidException) resolvedException)
                .getBindingResult()
                .getFieldError().
                getDefaultMessage());
        Assertions.assertEquals("cpf", ((MethodArgumentNotValidException) resolvedException)
                .getBindingResult()
                .getFieldError().
                getField());

    }

    @Test
    @DisplayName("deve dar erro pois CPF já esta cadastrado no banco")
    void test6() throws Exception {

        var barbeiroJaCadastrado = new Barbeiro("thiago", "241.659.160-64");
        barbeiroRepository.save(barbeiroJaCadastrado);

        var request = new NovoBarbeiroRequest("thiago", "241.659.160-64");

        var payload = mapper.writeValueAsString(request);

        var resolvedException = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/barbeiro")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResolvedException();

        Assertions.assertNotNull(resolvedException);
        Assertions.assertEquals(ResponseStatusException.class, resolvedException.getClass());
        Assertions.assertEquals("Barbeiro já cadastrado com este CPF no sistema!", ((ResponseStatusException) resolvedException).getReason());

    }

}