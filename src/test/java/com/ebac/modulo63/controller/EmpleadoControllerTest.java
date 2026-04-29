package com.ebac.modulo63.controller;

import com.ebac.modulo63.dto.Empleado;
import com.ebac.modulo63.service.EmpleadoService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@ExtendWith(MockitoExtension.class)
class EmpleadoControllerTest {

    @Mock
    EmpleadoService empleadoService;

    @InjectMocks
    EmpleadoController empleadoController;

    @Test
    void listadoEmpleados() {
        int total_empleados = 5;
        List<Empleado> empleadoListExpected = crearEmpleados(total_empleados);

        //Mock
        when(empleadoService.listaEmpleados()).thenReturn(empleadoListExpected);

        //Controlador
        List<Empleado> empleadoListaActual = empleadoController.listadoEmpleados();

        //Valida
        assertEquals(total_empleados , empleadoListaActual.size());
        assertEquals(empleadoListExpected , empleadoListaActual);
    }

    @Test
    void listadoEmpleadosCuandoNoExisten(){
        //Mock
        when(empleadoService.listaEmpleados()).thenReturn(List.of());

        //Controlador
        List<Empleado> empleadoListActual = empleadoController.listadoEmpleados();

        //Valida
        assertTrue(empleadoListActual.isEmpty());
        verify(empleadoService , times(1)).listaEmpleados();

    }

    @Test
    void empleadoPorId() {
        long idEmpleado = 1;
        Optional<Empleado> empleadoExpected = Optional.of(crearEmpleados(1).get(0));

        //Mock
        when(empleadoService.empleadoPorId(idEmpleado)).thenReturn(empleadoExpected);

        //Controlador
        ResponseEntity<Empleado> empleadoResponseEntity = empleadoController.empleadoPorId(idEmpleado);
        Empleado empleadoActual = empleadoResponseEntity.getBody();

        //Valida
        assertEquals(200 , empleadoResponseEntity.getStatusCode().value());
        assertNotNull(empleadoActual);
        assertEquals("Empleado 1" , empleadoActual.getNombre());
        assertEquals("Direccion 1" , empleadoActual.getDireccion());
        assertEquals("Tipo empleado 1" , empleadoActual.getTipo_empleado());
        assertEquals(11 , empleadoActual.getEdad());
    }

    @Test
    void empleadoPorIdPorIdCuandoNoExiste(){
        long idEmpleado = 1;

        //Mock
        when(empleadoService.empleadoPorId(idEmpleado)).thenReturn(Optional.empty());

        //Controlador
        ResponseEntity<Empleado> empleadoResponseEntity = empleadoController.empleadoPorId(idEmpleado);
        Empleado empleadoActual = empleadoResponseEntity.getBody();

        //Valida
        assertEquals(404 , empleadoResponseEntity.getStatusCode().value());
        assertTrue(Objects.isNull(empleadoActual));
    }

    @Test
    void altaEmpleado() throws Exception {
        Empleado empleadoExpected = crearEmpleados(1).get(0);

        //Mock
        when(empleadoService.altaEmpleado(empleadoExpected)).thenReturn(empleadoExpected);

        //Controlador
        ResponseEntity<Empleado> empleadoResponseEntity = empleadoController.altaEmpleado(empleadoExpected);
        Empleado empleadoActual = empleadoResponseEntity.getBody();

        //Valida
        assertEquals(201, empleadoResponseEntity.getStatusCode().value());
        assertTrue(Objects.isNull(empleadoActual));
    }

    @Test
    void actualizarEmpleado() {
        int idEmpleado = 5;

        String nombreActualizado = "Angel";
        String direccionActualizada = "Ciudad";
        int edadActualizada = 25;
        String tipoEmpleado = "Empleado";

        Empleado empleadoAnterior = new Empleado();
        empleadoAnterior.setIdEmpleado(5);
        empleadoAnterior.setNombre("Jose");
        empleadoAnterior.setEdad(28);
        empleadoAnterior.setTipo_empleado("Operario");
        empleadoAnterior.setDireccion("CDMX");

        Empleado empleadoActualizado = new Empleado();
        empleadoActualizado.setNombre(nombreActualizado);
        empleadoActualizado.setEdad(edadActualizada);
        empleadoActualizado.setDireccion(direccionActualizada);
        empleadoActualizado.setTipo_empleado(tipoEmpleado);

        //Mock
        when(empleadoService.empleadoPorId((long) idEmpleado)).thenReturn(Optional.of(empleadoAnterior));
        doNothing().when(empleadoService).actualizarEmpleado(empleadoActualizado);

        //Controlador
        ResponseEntity<Empleado> empleadoResponseEntity = empleadoController.actualizarEmpleado((long) idEmpleado , empleadoActualizado);
        Empleado empleadoActual = empleadoResponseEntity.getBody();

        //Valida
        assertEquals(200,empleadoResponseEntity.getStatusCode().value());
        assertNotNull(empleadoActual);
        assertEquals(idEmpleado , empleadoActual.getIdEmpleado());
        assertEquals(nombreActualizado , empleadoActual.getNombre());
        assertEquals(direccionActualizada , empleadoActual.getDireccion());
        assertEquals(tipoEmpleado , empleadoActual.getTipo_empleado());
        assertEquals(edadActualizada , empleadoActual.getEdad());
    }

    @Test
    void actualizarEmpleadoCuandoNoExiste(){
        long idEmpleado = 5;
        String nombreActualizado = "Angel";
        String direccionActualizada = "Ciudad";
        int edadActualizada = 25;
        String tipoEmpleado = "Empleado";

        Empleado empleadoActualizado = new Empleado();
        empleadoActualizado.setNombre(nombreActualizado);
        empleadoActualizado.setEdad(edadActualizada);
        empleadoActualizado.setDireccion(direccionActualizada);
        empleadoActualizado.setTipo_empleado(tipoEmpleado);

        //Mock
        when(empleadoService.empleadoPorId(idEmpleado)).thenReturn(Optional.empty());

        //Controlador
        ResponseEntity<Empleado> empleadoResponseEntity = empleadoController.actualizarEmpleado(idEmpleado , empleadoActualizado);
        Empleado empleadoActual = empleadoResponseEntity.getBody();

        //Valida
        assertEquals(404 , empleadoResponseEntity.getStatusCode().value());
        assertNull(empleadoActual);
        verify(empleadoService , never()).actualizarEmpleado(empleadoActualizado);
    }


    @Test
    void eliminarEmpleado() {
        long idEmpleado = 1;

        //Mock
        doNothing().when(empleadoService).eliminarEmpleado(idEmpleado);

        //Controlador
        ResponseEntity<Void> responseEntity = empleadoController.eliminarEmpleado(idEmpleado);

        //Valida
        assertEquals(204 , responseEntity.getStatusCode().value());
        //verify(empleadoService,times(1)).eliminarEmpleado(idEmpleado);
        verify(empleadoService,atLeastOnce()).eliminarEmpleado(idEmpleado);
    }

    @Test
    void CrearEmpleadoMenorEdad() throws Exception {
        Empleado empleado = new Empleado();
        empleado.setIdEmpleado(1);
        empleado.setNombre("Nombre");
        empleado.setDireccion("CDMX");
        empleado.setTipo_empleado("Almacenista");
        empleado.setEdad(15);

        //Mock
        doThrow(Exception.class).when(empleadoService).altaEmpleado(empleado);

        //Controlador
        ResponseEntity<Empleado> responseEntity = empleadoController.altaEmpleado(empleado);
        Empleado empleadoActual = responseEntity.getBody();

        //Valida
        assertEquals(400 , responseEntity.getStatusCode().value());
        assertNull(empleadoActual);

    }

    @Test
    void CrearEmpleadoConDoAnswer() throws Exception {
        Empleado empleadoExpected = crearEmpleados(1).get(0);

        //Mock
        doAnswer(parametro -> {
            return parametro;
        }).when(empleadoService).altaEmpleado(empleadoExpected);

        //Controlador
        ResponseEntity<Empleado> empleadoResponseEntity = empleadoController.altaEmpleado(empleadoExpected);
        Empleado empleadoActual = empleadoResponseEntity.getBody();

        //Valida
        assertEquals(400 , empleadoResponseEntity.getStatusCode().value());
        assertTrue(Objects.isNull(empleadoActual));

    }

    //
    private static List<Empleado> crearEmpleados(int numeroEmpelados){
        return IntStream.range(1 , numeroEmpelados + 1)
                .mapToObj( i -> {
                    Empleado empleado = new Empleado();
                    empleado.setIdEmpleado(i);
                    empleado.setNombre("Empleado " + i);
                    empleado.setDireccion("Direccion " + i);
                    empleado.setTipo_empleado("Tipo empleado " + i);
                    empleado.setEdad(10 + i);
                    return  empleado;
                }).collect(Collectors.toList());
    }
}