package br.com.barbearia.udia.cliente;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
class CadastraNovoClienteControllerTest  {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ClienteRepository clienteRepository;
    @BeforeEach
    void setUp() {
        clienteRepository.deleteAll();
    }

    @Test
    @DisplayName("deve cadastrar um cliente valido")
    void test1() throws Exception {

        var request = new NovoClienteRequest("thiago", "thiago@thiago");

        var payload = mapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/cliente")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        var cliente = clienteRepository.findAll();
        Assertions.assertEquals(1, cliente.size());

    }

    @Test
    @DisplayName("deve dar erro pois email é obrigatorio")
    void test2() throws Exception {

        var request = new NovoClienteRequest("thiago", "");

        var payload = mapper.writeValueAsString(request);

        var resolvedException = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/cliente")
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
        Assertions.assertEquals("email", ((MethodArgumentNotValidException) resolvedException)
                .getBindingResult()
                .getFieldError().
                getField());

    }

    @Test
    @DisplayName("deve dar erro pois nome é obrigatorio")
    void test3() throws Exception {

        var request = new NovoClienteRequest("", "thiago@thiago");

        var payload = mapper.writeValueAsString(request);

        var resolvedException = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/cliente")
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
    @DisplayName("deve dar erro pois nome e email é obrigatorio")
    void test4() throws Exception {

        var request = new NovoClienteRequest("", "");

        var payload = mapper.writeValueAsString(request);

        var resolvedException = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/cliente")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept-Language", "pt-br"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResolvedException();

        Assertions.assertNotNull(resolvedException);
        Assertions.assertEquals(MethodArgumentNotValidException.class, resolvedException.getClass());
    }

    @Test
    @DisplayName("deve dar erro pois email deve ser válido")
    void test5() throws Exception {

        var request = new NovoClienteRequest("thiago", "thiago132");

        var payload = mapper.writeValueAsString(request);

        var resolvedException = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/cliente")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept-Language", "pt-br"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResolvedException();


        Assertions.assertNotNull(resolvedException);
        Assertions.assertEquals(MethodArgumentNotValidException.class, resolvedException.getClass());
        Assertions.assertEquals("deve ser um endereço de e-mail bem formado", ((MethodArgumentNotValidException) resolvedException)
                .getBindingResult()
                .getFieldError().
                getDefaultMessage());
        Assertions.assertEquals("email", ((MethodArgumentNotValidException) resolvedException)
                .getBindingResult()
                .getFieldError().
                getField());

    }

    @Test
    @DisplayName("deve dar erro pois email já esta cadastrado no banco")
    void test6() throws Exception {

        var clienteJaCadastrado = new Cliente("thiago", "thiago@thiago");
        clienteRepository.save(clienteJaCadastrado);

        var request = new NovoClienteRequest("thiago", "thiago@thiago");

        var payload = mapper.writeValueAsString(request);

        var resolvedException = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/cliente")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResolvedException();

        Assertions.assertNotNull(resolvedException);
        Assertions.assertEquals(ResponseStatusException.class, resolvedException.getClass());
        Assertions.assertEquals("Cliente já cadastrado com este email no sistema.", ((ResponseStatusException) resolvedException).getReason());

    }
}