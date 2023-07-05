package nikstanov.project.controllers;

import nikstanov.project.DAO.PersonDAO;
import nikstanov.project.Services.PeopleService;
import nikstanov.project.models.Book;
import nikstanov.project.models.Person;
import nikstanov.project.utills.PersonValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/people")
public class PeopleController {

//    private final PersonDAO personDAO;
    private final PersonValidator personValidator;
    private final PeopleService peopleService;

    @Autowired
    public PeopleController(PersonValidator personValidator, PeopleService peopleService) {
//        this.personDAO = personDAO;
        this.personValidator = personValidator;
        this.peopleService = peopleService;
    }

    @GetMapping()
    public String index(Model model, @RequestParam int page, @RequestParam int forPage) {
//        model.addAttribute("people", personDAO.index());
        model.addAttribute("forPage", forPage);
        model.addAttribute("count", peopleService.count(forPage));
        model.addAttribute("pageNumber", page);
        model.addAttribute("people", peopleService.findAll(page - 1, forPage));
        return "people/index";
    }

//    @GetMapping()
//    public String index(Model model) {
////        model.addAttribute("people", personDAO.index());
//        model.addAttribute("people", peopleService.findAll());
//        return "people/index";
//    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
//        model.addAttribute("person", personDAO.show(id));
//        model.addAttribute("books", personDAO.getPersonBooks(id));
        model.addAttribute("person", peopleService.findOne(id));
        model.addAttribute("books", peopleService.getPersonBooks(id));
        ((List<Book>) model.getAttribute("books")).forEach((x) -> {x.haveToReturned();});
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {

        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors())
            return "people/new";

//        personDAO.save(person);
        peopleService.save(person);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
//        model.addAttribute("person", personDAO.show(id));
        model.addAttribute("person", peopleService.findOne(id));
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person, BindingResult bindingResult,
                         @PathVariable("id") int id) {

        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors())
            return "people/edit";

//        personDAO.update(id, person);
        peopleService.update(id, person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
//        personDAO.delete(id);
        peopleService.delete(id);
        return "redirect:/people";
    }
}
