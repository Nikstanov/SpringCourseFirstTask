package nikstanov.project.controllers;

import nikstanov.project.DAO.BookDAO;
import nikstanov.project.DAO.PersonDAO;
import nikstanov.project.Services.BooksService;
import nikstanov.project.models.Book;
import nikstanov.project.models.Person;
import nikstanov.project.utills.BookValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {


//    private final BookDAO bookDAO;
//    private final PersonDAO personDAO;
    private final BookValidator bookValidator;
    private final BooksService booksService;

    @Autowired
    public BookController(BookValidator bookValidator, BooksService booksService) {
//        this.bookDAO = bookDAO;
//        this.personDAO = personDAO;
        this.bookValidator = bookValidator;
        this.booksService = booksService;
    }

//    @GetMapping()
//    public String index(Model model) {
////        model.addAttribute("books", bookDAO.index());
//        model.addAttribute("books", booksService.index());
//        return "books/index";
//    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(name="page", required = false) int page,
                        @RequestParam(name="forPage", required = false) int forPage,
                        @RequestParam(name="sort_by_year", required = false) Boolean sort_by_year) {
//        model.addAttribute("people", personDAO.index());
        if(forPage == 0){
            if(sort_by_year != null){
                model.addAttribute("books", booksService.findAll(sort_by_year));
            }
            else{
                model.addAttribute("books", booksService.index());
            }
        }
        else{
            if(sort_by_year != null){
                model.addAttribute("books", booksService.findAll(page-1,forPage,sort_by_year));
            }
            else{
                model.addAttribute("books", booksService.findAll(page - 1, forPage));

            }
        }
        model.addAttribute("forPage", forPage);
        model.addAttribute("count", booksService.count(forPage));
        model.addAttribute("pageNumber", page);
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@ModelAttribute("person") Person person, @PathVariable("id") int id, Model model) {
//        model.addAttribute("book", bookDAO.show(id));
//        model.addAttribute("owner", bookDAO.showOwner(id));
//        model.addAttribute("people", personDAO.index());
        model.addAttribute("book", booksService.show(id));
        model.addAttribute("owner", booksService.showOwner(id));
        model.addAttribute("people", booksService.index());
        return "books/show";
    }

    @PostMapping("/search")
    public String search(Model model, @RequestParam String name){
        if(name != null){
            model.addAttribute("result", booksService.findByName(name));
        }
        else{
            model.addAttribute("result", null);
        }
        return "books/search";
    }

    @GetMapping("/search")
    public String search(Model model){
        model.addAttribute("result", null);
        return "books/search";
    }

    @PatchMapping("/{id}/newOwner")
    public String setNewOwner(@ModelAttribute("person") Person person, @PathVariable("id") int id) {
//        bookDAO.setOwner(id, person.getId());
        booksService.setOwner(id, person.getId());
        return "redirect:/books/" + id;
    }

    @DeleteMapping ("/{id}/deleteOwner")
    public String deleteOwner(@PathVariable("id") int id) {
//        bookDAO.setOwner(id);
        booksService.setOwner(id);
        return "redirect:/books/" + id;
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {

        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors())
            return "books/new";

//        bookDAO.save(book);
        booksService.save(book);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
//        model.addAttribute("book", bookDAO.show(id));
        model.addAttribute("book", booksService.show(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult,
                         @PathVariable("id") int id) {

        bookValidator.validate(book, bindingResult);
        if (bindingResult.hasErrors())
            return "books/edit";

//        bookDAO.update(id, book);
        booksService.update(id, book);
        return "redirect:/books";

    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id) {
//        bookDAO.delete(id);
        booksService.delete(id);
        return "redirect:/books";
    }
}
