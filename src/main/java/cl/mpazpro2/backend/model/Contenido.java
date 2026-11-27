
package cl.mpazpro2.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "contenidos")
public class Contenido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "La asignatura es obligatoria")
    @Column(nullable = false, length = 100)
    private String asignatura;

    @Column(nullable = false, length = 100)
    private String unidad;

    @NotBlank(message = "El objetivo es obligatorio")
    @Column(nullable = false, length = 300)
    private String objetivo;

    @NotBlank(message = "El tipo es obligatorio")
    @Column(nullable = false, length = 80)
    private String tipo;

    @NotBlank(message = "El titulo es obligatorio")
    @Column(nullable = false, length = 180)
    private String titulo;

    @NotBlank(message = "El contenido es obligatorio")
    @Column(nullable = false, length = 2000)
    private String contenido;

    @Column(length = 1000)
    private String pregunta;

    @Column(length = 1000)
    private String imagenUri;

    @Enumerated(EnumType.STRING)

    @Column(nullable = false, length = 20)
    private EstadoContenido estado = EstadoContenido.PENDIENTE;

    public Contenido() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public String getPregunta() {
        return pregunta;
    }

    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }


    public String getImagenUri() {
        return imagenUri;
    }

    public void setImagenUri(String imagenUri) {
        this.imagenUri = imagenUri;
    }

    public EstadoContenido getEstado() {
        return estado;
    }

    public void setEstado(EstadoContenido estado) {
        this.estado = estado;
    }


}
