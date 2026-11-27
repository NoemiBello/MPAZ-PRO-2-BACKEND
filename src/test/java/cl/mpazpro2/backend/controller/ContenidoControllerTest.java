package cl.mpazpro2.backend.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:testcontrollerdb;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class ContenidoControllerTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    void flujoCrudCompleto_postGetPutDelete_enApiContenidos() throws Exception {
        String crearJson = """
                {
                  "asignatura": "Lenguaje",
                  "unidad": "Unidad 1",
                  "objetivo": "Comprender textos narrativos",
                  "tipo": "Lectura",
                  "titulo": "Mi primera lectura",
                  "contenido": "Habia una vez un gato que vivia en el campo.",
                  "pregunta": "Donde vivia el gato?",
                  "imagenUri": null,
                  "estado": "PENDIENTE"
                }
                """;

        // 1. POST /api/contenidos -> 201 Created
        MvcResult postResult = mockMvc.perform(post("/api/contenidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(crearJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.titulo").value("Mi primera lectura"))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"))
                .andReturn();

        String body = postResult.getResponse().getContentAsString();
        Matcher matcher = Pattern.compile("\"id\"\\s*:\\s*(\\d+)").matcher(body);
        assertThat(matcher.find()).isTrue();
        long createdId = Long.parseLong(matcher.group(1));

        // 2. GET /api/contenidos -> 200 OK
        mockMvc.perform(get("/api/contenidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].titulo").value("Mi primera lectura"));

        // 3. GET /api/contenidos/{id} -> 200 OK
        mockMvc.perform(get("/api/contenidos/{id}", createdId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.asignatura").value("Lenguaje"))
                .andExpect(jsonPath("$.pregunta").value("Donde vivia el gato?"));

        // 4. PUT /api/contenidos/{id} -> 200 OK (actualiza titulo y estado)
        String actualizarJson = """
                {
                  "asignatura": "Lenguaje",
                  "unidad": "Unidad 1",
                  "objetivo": "Comprender textos narrativos",
                  "tipo": "Lectura",
                  "titulo": "Mi primera lectura aprobada",
                  "contenido": "Habia una vez un gato muy curioso en el campo.",
                  "pregunta": "Como era el gato?",
                  "imagenUri": "content://img/gato.jpg",
                  "estado": "APROBADO"
                }
                """;

        mockMvc.perform(put("/api/contenidos/{id}", createdId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(actualizarJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Mi primera lectura aprobada"))
                .andExpect(jsonPath("$.estado").value("APROBADO"))
                .andExpect(jsonPath("$.imagenUri").value("content://img/gato.jpg"));

        // 5. DELETE /api/contenidos/{id} -> 204 No Content
        mockMvc.perform(delete("/api/contenidos/{id}", createdId))
                .andExpect(status().isNoContent());

        // 6. GET despues de eliminar -> 404 Not Found
        mockMvc.perform(get("/api/contenidos/{id}", createdId))
                .andExpect(status().isNotFound());
    }

    @Test
    void postSinCamposObligatorios_retornaBadRequest400() throws Exception {
        String invalidoJson = """
                {
                  "asignatura": "",
                  "unidad": "Unidad 1",
                  "objetivo": "",
                  "tipo": "",
                  "titulo": "",
                  "contenido": ""
                }
                """;

        mockMvc.perform(post("/api/contenidos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidoJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getConIdInexistente_retornaNotFound404() throws Exception {
        mockMvc.perform(get("/api/contenidos/{id}", 99999L))
                .andExpect(status().isNotFound());
    }
}
