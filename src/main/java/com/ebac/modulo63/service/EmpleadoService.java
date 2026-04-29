package com.ebac.modulo63.service;

import com.ebac.modulo63.dto.Empleado;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoService {
    @Autowired
    EmpleadoRepository empleadoRepository;

    //INSERT
    public Empleado altaEmpleado(Empleado empleadoNuevo) throws Exception {
        //valida
        if (empleadoNuevo.getEdad() > 18){
            return empleadoRepository.save(empleadoNuevo);
        }else {
            throw new Exception("Solo pueden trabajar mayores de 18 años");
        }
    }

    //SELECT
    public List<Empleado> listaEmpleados(){
        return empleadoRepository.findAll();
    }

    //SELECT POR ID
    public Optional<Empleado> empleadoPorId(Long id){
        return empleadoRepository.findById(id);
    }

    //UPDATE
    public void actualizarEmpleado(Empleado empleadoActualizado){
        empleadoRepository.save(empleadoActualizado);
    }

    //DELETE
    public void eliminarEmpleado(Long id){
        empleadoRepository.deleteById(id);
    }

}
