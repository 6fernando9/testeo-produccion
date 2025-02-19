package com.kraken.spring_app_coworking.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kraken.spring_app_coworking.Models.enums.RolEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Setter
@Getter
@Builder
@Table(name = "roles")
public class Rol {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="nombre", unique = true,nullable = false)
    //@Enumerated(EnumType.STRING)
    private String nombre;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "rol")
    private List<Usuario> usuarios;

    @ManyToMany
    @JoinTable(name = "roles_permisos"
            , joinColumns = @JoinColumn(name = "rol_id")
            , inverseJoinColumns = @JoinColumn(name = "permiso_id")
            , uniqueConstraints = {@UniqueConstraint(columnNames = {"rol_id", "permiso_id"})})
    private Set<Permiso> permisos;
}
