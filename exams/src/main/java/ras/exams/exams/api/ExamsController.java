package ras.exams.exams.api;

import ras.exams.exams.service.ExamsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class ExamsController {
    private final ExamsService exams;

    @Autowired
    public ExamsController(ExamsService exams){
        this.exams = exams;
    }

    @GetMapping("teste")
    public String teste(){
        return "piroca";
    }

    // Creates a new exam - examName
    @PostMapping("exam")
    public void createExam(@RequestBody String name){
        System.out.println(name);
    }
}
