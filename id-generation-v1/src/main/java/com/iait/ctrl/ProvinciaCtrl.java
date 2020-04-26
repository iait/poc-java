package com.iait.ctrl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iait.entities.ProvinciaEntity;
import com.iait.payloads.requests.ProvinciaRequest;
import com.iait.payloads.responses.ProvinciaResponse;
import com.iait.services.ProvinciaService;

@RestController
@RequestMapping("/provincias")
public class ProvinciaCtrl {
    
    private static final Logger LOG = LoggerFactory.getLogger(ProvinciaCtrl.class);
    
    @Autowired
    private ProvinciaService service;
    
    @GetMapping(path = "/{id}")
    public ResponseEntity<ProvinciaResponse> buscar(@PathVariable(name = "id") Long id) {
        ProvinciaEntity entity = service.findById(id).orElseThrow(RuntimeException::new);
        ProvinciaResponse response = new ProvinciaResponse(entity);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    public ResponseEntity<List<ProvinciaResponse>> buscar() {
        List<ProvinciaEntity> entities = service.findAll();
        List<ProvinciaResponse> response = 
                entities.stream().map(ProvinciaResponse::new).collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping
    public ResponseEntity<ProvinciaResponse> alta(
            @RequestBody ProvinciaRequest request,
            @RequestHeader(value = "x-delay", required = false, defaultValue = "0") long delay) 
                    throws InterruptedException {
        LOG.info("Header X-DELAY: {}", delay);
        ProvinciaEntity entity = service.create(request.getNombre(), delay);
        ProvinciaResponse response = new ProvinciaResponse(entity);
        return ResponseEntity.ok(response);
    }
}
