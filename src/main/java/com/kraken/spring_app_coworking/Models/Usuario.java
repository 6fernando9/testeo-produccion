package com.kraken.spring_app_coworking.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.engine.internal.Cascade;

import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min = 4, max = 20)
    private String username;

    private boolean enabled;

    @Column(unique = true)
    @NotBlank
    //@Email(message = "Se debe colocar un correo valido")// no funciona
    private String correo;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;

    @OneToMany(mappedBy = "usuario",cascade = {CascadeType.PERSIST,CascadeType.REMOVE},orphanRemoval = true)
    @JsonIgnore
    @ToString.Exclude
    private List<Notificacion> notificaciones;

//    @OneToMany(mappedBy = "usuario", cascade = CascadeType.REMOVE,orphanRemoval = true)
//    @JsonIgnore
//    @ToString.Exclude
//    private Set<Toke> tokens;

    @PrePersist
    public void asignarEnabledAUsuario(){
        this.enabled=true;
    }
}
