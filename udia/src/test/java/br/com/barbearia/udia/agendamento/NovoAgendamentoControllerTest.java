package br.com.barbearia.udia.agendamento;

import br.com.barbearia.udia.barbeiro.Barbeiro;
import br.com.barbearia.udia.barbeiro.BarbeiroRepository;
import br.com.barbearia.udia.cliente.Cliente;
import br.com.barbearia.udia.cliente.ClienteRepository;
import br.com.barbearia.udia.procedimento.Procedimento;
import br.com.barbearia.udia.procedimento.ProcedimentoRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
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

import java.math.BigDecimal;
import java.util.List;

import static java.time.LocalDateTime.now;
import static java.time.LocalDateTime.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class NovoAgendamentoControllerTest {

    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AgendamentoRepository agendamentoRepository;
    @Autowired
    private BarbeiroRepository barbeiroRepository;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ProcedimentoRepository procedimentoRepository;


    @BeforeEach
    void setUp() {
        this.agendamentoRepository.deleteAll();
        this.clienteRepository.deleteAll();
        this.barbeiroRepository.deleteAll();
    }

    @Test
    @DisplayName("deve cadastrar um agendamento valido")
    void test1() throws Exception {

        var barbeiro = new Barbeiro("joão", "123.123.123");
        barbeiroRepository.save(barbeiro);

        var cliente = new Cliente("thiago", "thiago@gmail.com");
        clienteRepository.save(cliente);

        var procedimento = new Procedimento("corte", "30 min", BigDecimal.TEN);
        procedimentoRepository.save(procedimento);

        var request = new NovoAgendamentoRequest(3L, 3L, of(2023, 7, 15, 14, 0), List.of(1L));

        var payload = mapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/agendamento")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        var agendamento = agendamentoRepository.findAll();
        assertEquals(1, agendamento.size());

    }

    @Test
    @DisplayName("deve dar erro, pois id barbeiro é nulo")
    void test2() throws Exception {

        var request = new NovoAgendamentoRequest(null, 1L, of(2023, 7, 15, 14, 0), List.of(1L));

        var payload = mapper.writeValueAsString(request);

        var resolvedException = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/agendamento")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept-Language", "pt-br"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResolvedException();

        assertNotNull(resolvedException);
        assertEquals("barbeiroId", getCampo(resolvedException));
        assertEquals("não deve ser nulo", getMenssagem(resolvedException));

    }

    @Test
    @DisplayName("deve dar erro, pois id cliente é nulo")
    void test3() throws Exception {

        var request = new NovoAgendamentoRequest(1L, null, of(2023, 7, 15, 14, 0), List.of(1L));

        var payload = mapper.writeValueAsString(request);

        var resolvedException = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/agendamento")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept-Language", "pt-br"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResolvedException();

        assertNotNull(resolvedException);
        assertEquals("clienteId", getCampo(resolvedException));
        assertEquals("não deve ser nulo", getMenssagem(resolvedException));

    }
    @Test
    @DisplayName("deve dar erro, pois data é nula")
    void test4() throws Exception {

        var request = new NovoAgendamentoRequest(1L, 1L, null,List.of(1L));

        var payload = mapper.writeValueAsString(request);

        var resolvedException = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/agendamento")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept-Language", "pt-br"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResolvedException();

        assertNotNull(resolvedException);
        assertEquals("horario", getCampo(resolvedException));
        assertEquals("não deve ser nulo", getMenssagem(resolvedException));

    }
    @Test
    @DisplayName("deve dar erro, pois data esta no passado")
    void test5() throws Exception {

        var request = new NovoAgendamentoRequest(1L, 1L, now().minusDays(1),List.of(1L));

        var payload = mapper.writeValueAsString(request);

        var resolvedException = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/agendamento")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept-Language", "pt-br"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResolvedException();

        assertNotNull(resolvedException);
        assertEquals("horario", getCampo(resolvedException));
        assertEquals("deve ser uma data no presente ou no futuro", getMenssagem(resolvedException));
    }

    @Test
    @DisplayName("deve dar erro, lista de procedimentosIds nao deve estar vazios")
    void test6() throws Exception {

        var request = new NovoAgendamentoRequest(1L, 1L,of(2055, 7, 15, 14, 0),List.of());

        var payload = mapper.writeValueAsString(request);

        var resolvedException = mockMvc.perform(MockMvcRequestBuilders
                        .post("/v1/agendamento")
                        .content(payload)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Accept-Language", "pt-br"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andReturn().getResolvedException();

        assertNotNull(resolvedException);
        assertEquals("procedimentosIds", getCampo(resolvedException));
        assertEquals("não deve estar vazio", getMenssagem(resolvedException));
    }

    private String getCampo(Exception exception) {
        return ((MethodArgumentNotValidException) exception)
                .getBindingResult()
                .getFieldError()
                .getField();
    }

    private String getMenssagem(Exception exception) {
        return ((MethodArgumentNotValidException) exception)
                .getBindingResult()
                .getFieldError()
                .getDefaultMessage();
    }
}