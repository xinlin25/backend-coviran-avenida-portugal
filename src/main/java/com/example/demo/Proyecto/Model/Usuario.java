package com.example.demo.Proyecto.Model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.Proyecto.Enum.Rol;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table (name = "usuarios")
@Getter
@Setter
public class Usuario implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "El nombre es obligatorio.")
    @Size(min = 3, max = 100, message = "El nombre debe de tener entre 3 y 100 caracteres.")
    private String nombreCompleto;

    @Column(nullable = false, length = 15)
    @NotBlank(message = "El número de teléfono es obligatorio.")
    @Size(min = 9, max = 15, message = "El número de teléfono debe de tener entre 9 y 15 caracteres.")
    private String tlf;

    @Column(nullable = false, unique = true, length = 100)
    @Email
    @NotBlank(message = "El correo electrónico es obligatorio.")
    @Size(min = 5, max = 100, message = "El email debe de tener entre 5 y 100 caracteres.")
    private String correo;

    @Column(nullable = false, length = 60)
    @Size(min = 8, max = 60)
    @NotBlank(message = "La contraseña es obligatoria.")
    
    private String password;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "La dirección es obligatoria.")
    @Size(min = 5, max = 100)
    private String direccion;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Rol rol;

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    private List<Pedido> pedidos = new ArrayList<>();

    @OneToMany(mappedBy = "empleado")
    @JsonIgnore
    private List<Pedido> pedidosGestionados = new ArrayList<>();

    @Column(nullable = false)
    private boolean enabled = true;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + rol.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return correo;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}