package com.iait.ctrl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iait.entities.LocalidadEntity;
import com.iait.payloads.requests.LocalidadRequest;
import com.iait.payloads.responses.LocalidadResponse;
import com.iait.services.LocalidadService;

@RestController
@RequestMapping("/localidades")
public class LocalidadCtrl {
    
    @Autowired
    private LocalidadService service;
    
    @GetMapping(path = "/{id}")
    public ResponseEntity<LocalidadResponse> buscar(@PathVariable(name = "id") Long id) {
        LocalidadEntity entity = service.buscarPorId(id).orElseThrow(RuntimeException::new);
        LocalidadResponse response = new LocalidadResponse(entity);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<List<LocalidadResponse>> buscar() {
        List<LocalidadEntity> entities = service.buscar();
        List<LocalidadResponse> response = 
                entities.stream().map(LocalidadResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping
    public ResponseEntity<LocalidadResponse> alta(@RequestBody LocalidadRequest request) {
        LocalidadEntity entity = service.alta(request.getProvinciaId(), request.getNombre());
        LocalidadResponse response = new LocalidadResponse(entity);
        return ResponseEntity.ok(response);
    }
}
