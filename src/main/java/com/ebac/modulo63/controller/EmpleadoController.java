package com.ebac.modulo63.controller;

import com.ebac.modulo63.dto.Empleado;
import com.ebac.modulo63.service.EmpleadoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
public class EmpleadoController {

    @Autowired
    EmpleadoService empleadoService;

    @GetMapping("/empleados")
    public List<Empleado> listadoEmpleados(){
        System.out.println("------------------------> LISTADO DE EMPLEADOS <------------------------");
        List<Empleado> listaEmpleados = empleadoService.listaEmpleados();
        listaEmpleados.forEach(System.out::println);
        return listaEmpleados;
    }

    @GetMapping("/empleados/{id}")
    public ResponseEntity<Empleado> empleadoPorId(@PathVariable Long id){
        System.out.println("------------------------> LISTADO DE EMPLEADOS POR ID <------------------------");
        Optional<Empleado> empleadoABuscar = empleadoService.empleadoPorId(id);
        System.out.println(empleadoABuscar);
        return empleadoABuscar.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/empleados")
    public ResponseEntity<Empleado> altaEmpleado(@RequestBody Empleado empleadoNuevo)  {
        System.out.println("------------------------> AGREGA EMPLEADOS <------------------------");
        try {
            empleadoService.altaEmpleado(empleadoNuevo);
            return ResponseEntity.created(new URI("http://localhost/empleados")).build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/empleados/{id}")
    public ResponseEntity<Empleado> actualizarEmpleado(@PathVariable Long id , @RequestBody Empleado empleadoActualizado){
        System.out.println("------------------------> ACTUALIZA EMPLEADOS <------------------------");
        System.out.println(empleadoActualizado);

        Optional<Empleado> empleadoABuscar = empleadoService.empleadoPorId(id);

        if (empleadoABuscar.isPresent()){
            empleadoActualizado.setIdEmpleado(empleadoABuscar.get().getIdEmpleado());
            empleadoService.actualizarEmpleado(empleadoActualizado);
            return ResponseEntity.ok(empleadoActualizado);
        }else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/empleados/{id}")
    public ResponseEntity<Void> eliminarEmpleado(@PathVariable Long id){
        System.out.println("------------------------> ELIMINA EMPLEADOS <------------------------");
        System.out.println(id);
        empleadoService.eliminarEmpleado(id);
        return ResponseEntity.noContent().build();
    }


}
