package com.iait.ctrl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iait.entities.LocalidadEntity;
import com.iait.payloads.requests.LocalidadRequest;
import com.iait.payloads.responses.LocalidadResponse;
import com.iait.services.LocalidadService;

@RestController
@RequestMapping("/localidades")
public class LocalidadCtrl {
    
    private static final Logger LOG = LoggerFactory.getLogger(LocalidadCtrl.class);
    
    @Autowired
    private LocalidadService service;
    
    @GetMapping
    public ResponseEntity<List<LocalidadResponse>> buscarPorProvincia(
            @RequestParam(name = "provincia") Long provinciaId) {
        List<LocalidadEntity> entities = service.findAll(q -> q.provincia.id.eq(provinciaId));
        List<LocalidadResponse> response = 
                entities.stream().map(LocalidadResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping
    public ResponseEntity<LocalidadResponse> alta(
            @RequestBody LocalidadRequest request,
            @RequestHeader(value = "x-delay", required = false, defaultValue = "0") long delay) 
                    throws InterruptedException {
        LOG.info("Header X-DELAY: {}", delay);
        LocalidadEntity entity = service.create(
                request.getProvinciaId(), request.getNombre(), delay);
        LocalidadResponse response = new LocalidadResponse(entity);
        return ResponseEntity.ok(response);
    }
}
