package com.example.demo.Proyecto.Service;

import com.example.demo.Proyecto.Model.Usuario;
import com.example.demo.Proyecto.Repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {
    //Probar con .\mvnw.cmd test
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    @Test
    void guardarUsuario_codificaPassword_yGuardaUsuario() {

        //Creamos un usuario de prueba
        Usuario usuario = new Usuario();
        usuario.setPassword("1234");

        //Definimos comportamiento simulado
        when(passwordEncoder.encode("1234")).thenReturn("passwordCodificada");
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        //Ejecutamos el método real
        Usuario resultado = usuarioService.guardarUsuario(usuario);

        //Verificaciones

        //Se codificó la contraseña
        assertEquals("passwordCodificada", resultado.getPassword());

        //Se llamó al repositorio
        verify(usuarioRepository, times(1)).save(usuario);

        //Se llamó al encoder
        verify(passwordEncoder, times(1)).encode("1234");
    }
}