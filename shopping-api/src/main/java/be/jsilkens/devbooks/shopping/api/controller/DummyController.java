package be.jsilkens.devbooks.shopping.api.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyController implements DummyApi {

    @Override
    public ResponseEntity<DummyObject> getDummy() {
        return ResponseEntity.ok(
                DummyObject.builder()
                        .id("1")
                        .name("Dummy Item")
                        .build()
        );
    }
}
