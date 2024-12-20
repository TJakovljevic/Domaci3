package com.example.Domaci_3.controllers;

import com.example.Domaci_3.model.ErrorMessage;
import com.example.Domaci_3.services.ErrorService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/error")
public class ErrorController {

    private final ErrorService errorService;

    public ErrorController(ErrorService errorService){
        this.errorService = errorService;
    }
    //u rutu ubacujem korisnikov email, nadje ga preko findUserByEmail -> proveri da li je admin ako jeste onda return all,
    // ako ne onda samo userove
    @GetMapping(value = "/{email}")
    public Page<ErrorMessage> all(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer size, @PathVariable String email) {
        return this.errorService.paginate(page, size, email);
    }
}
