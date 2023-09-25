package io.skyvoli.springboot;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class Controller {

    private final SpringService springService;

    @GetMapping
    public String greeting() {
       return springService.greeting();
    }
}
