package br.com.barbearia.udia.procedimento;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class CadastraNovoProcedimentoControllerTest {


    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ProcedimentoRepository procedimentoRepository;

    @BeforeEach
    void setUp() {
        procedimentoRepository.deleteAll();
    }

    @Test
    @DisplayName("deve cadastrar um procedimento valido")
    void test1() throws Exception {

        var request = new NovoProcedimentoRequest("corte","40 minutos", BigDecimal.TEN);

        var payload = mapper.writeValueAsString(request);

        mockMvc.perform(
                post("/v1/procedimento")
                .content(payload)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated());

        var procedimento = procedimentoRepository.findAll();

        assertEquals(1,procedimento.size());

    }

    @Test
    @DisplayName("deve dar erro, pois nome é obrigatorio")
    void test2() throws Exception{

        var request = new NovoProcedimentoRequest("","30 minutos",BigDecimal.TEN);

        var payload = mapper.writeValueAsString(request);

        var resolvedException = mockMvc.perform(
                        post("/v1/procedimento")
                                .content(payload)
                                .contentType(APPLICATION_JSON)
                                .header("Accept-Language","pt-br"))
                                .andExpect(status().isBadRequest())
                                .andReturn().getResolvedException();

        assertNotNull(resolvedException);
        assertEquals(MethodArgumentNotValidException.class,resolvedException.getClass());
        assertEquals(getCampo(resolvedException),"nome");
        assertEquals(getNome(resolvedException),"não deve estar em branco");

    }

    @Test
    @DisplayName("deve dar erro, pois duracao é obrigatorio")
    void test3() throws Exception{

        var request = new NovoProcedimentoRequest("corte","",BigDecimal.TEN);

        var payload = mapper.writeValueAsString(request);

        var resolvedException = mockMvc.perform(
                        post("/v1/procedimento")
                                .content(payload)
                                .contentType(APPLICATION_JSON)
                                .header("Accept-Language","pt-br"))
                .andExpect(status().isBadRequest())
                .andReturn().getResolvedException();

        assertNotNull(resolvedException);
        assertEquals(MethodArgumentNotValidException.class,resolvedException.getClass());
        assertEquals(getCampo(resolvedException),"duracao");
        assertEquals(getNome(resolvedException),"não deve estar em branco");

    }

    @Test
    @DisplayName("deve dar erro, pois preco é obrigatorio")
    void test4() throws Exception{

        var request = new NovoProcedimentoRequest("corte","30 min",null);

        var payload = mapper.writeValueAsString(request);

        var resolvedException = mockMvc.perform(
                        post("/v1/procedimento")
                                .content(payload)
                                .contentType(APPLICATION_JSON)
                                .header("Accept-Language","pt-br"))
                .andExpect(status().isBadRequest())
                .andReturn().getResolvedException();

        assertNotNull(resolvedException);
        assertEquals(MethodArgumentNotValidException.class,resolvedException.getClass());
        assertEquals(getCampo(resolvedException),"preco");
        assertEquals(getNome(resolvedException),"não deve ser nulo");

    }

    @Test
    @DisplayName("deve dar erro, pois preco deve ser positivo")
    void test5() throws Exception{

        var request = new NovoProcedimentoRequest("corte","30 min",new BigDecimal("-2"));

        var payload = mapper.writeValueAsString(request);

        var resolvedException = mockMvc.perform(
                        post("/v1/procedimento")
                                .content(payload)
                                .contentType(APPLICATION_JSON)
                                .header("Accept-Language","pt-br"))
                .andExpect(status().isBadRequest())
                .andReturn().getResolvedException();

        assertNotNull(resolvedException);
        assertEquals(MethodArgumentNotValidException.class,resolvedException.getClass());
        assertEquals(getCampo(resolvedException),"preco");
        assertEquals(getNome(resolvedException),"deve ser maior que 0");

    }

    @Test
    @DisplayName("deve dar erro, pois procedimento já esta cadastrado")
    void test6() throws Exception {

        var procedimento = new Procedimento("corte","30 min",BigDecimal.TEN);
        procedimentoRepository.save(procedimento);

        var request = new NovoProcedimentoRequest("corte","40 minutos", BigDecimal.TEN);

        var payload = mapper.writeValueAsString(request);

        var resolvedException = mockMvc.perform(
                        post("/v1/procedimento")
                                .content(payload)
                                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResolvedException();

        assertNotNull(resolvedException);
        assertEquals("Procedimento já cadastrado no sistema",((ResponseStatusException) resolvedException).getReason());

    }
    private String getCampo (Exception resolvedException){

       return ((MethodArgumentNotValidException) resolvedException)
                .getBindingResult()
                .getFieldError()
                .getField();
    }
    private String getNome (Exception resolvedException){

        return ((MethodArgumentNotValidException) resolvedException)
                .getBindingResult()
                .getFieldError()
                .getDefaultMessage();
    }
}