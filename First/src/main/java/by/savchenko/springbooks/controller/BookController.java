package by.savchenko.springbooks.controller;

import by.savchenko.springbooks.forms.BookForm;
import by.savchenko.springbooks.model.Book;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BookController {
    private static List<Book> bookList = new ArrayList<>();
    static {
        bookList.add(new Book("Full stack 1","Savchenko Vlad"));
        bookList.add(new Book("Full stack 2","Savchenko Vladick"));
    }

    @Value("${welcom.message}")
    private String message;

    @Value("${error.message}")
    private String errorMessage;


    @RequestMapping(value = {"/","/index"}, method = RequestMethod.GET)
    public ModelAndView index(Model model){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("index");
        model.addAttribute("message",message);
        return modelAndView;
    }
    @RequestMapping(value = {"/allbooks"}, method = RequestMethod.GET)
    public  ModelAndView personList(Model model){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("booklist");
        model.addAttribute("books", bookList);
        return modelAndView;
    }
    @RequestMapping(value = "/addbook", method = RequestMethod.GET)
    public ModelAndView ShowAddListPage(Model model){
        ModelAndView modelAndView = new ModelAndView("addbook");
        BookForm bookForm = new BookForm();
        model.addAttribute("bookform", bookForm);
        return modelAndView;
    }

    @RequestMapping(value = "/update/{book}", method = RequestMethod.GET)
    public ModelAndView ShowUpdateListPage(@PathVariable(value = "book") Book book){
        ModelAndView modelAndView = new ModelAndView("update");
        BookForm bookForm = new BookForm();
        bookForm.setAuthor(book.getAuthor());
        book.setTitle(book.getTitle());
        return modelAndView;
    }
    
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public ModelAndView ActionUpdateProduct(Model model, @ModelAttribute("updateform") BookForm bookform){
        ModelAndView modelAndView = new ModelAndView();
        return modelAndView;
    }

    @RequestMapping(value = "/addbook", method = RequestMethod.POST)
    public ModelAndView ActionAddListPage(Model model, @ModelAttribute("bookform") BookForm bookForm){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("booklist");
        String title = bookForm.getTitle();
        String author = bookForm.getAuthor();

        if(title != null && title.length()>0 && author != null && author.length()>0){
            Book book = new Book(title, author);
            bookList.add(book);
            model.addAttribute("books", bookList);
            return modelAndView;
        }
        model.addAttribute("errorMessage", errorMessage);
        modelAndView.setViewName("addbook");
        return modelAndView;
    }

    @GetMapping("/delete/{name}")
    public String DeleteProduct(@PathVariable(value = "name") String name){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("booklist");
        Book bookdelete = bookList.
                stream()
                .filter(x -> x.getTitle().equals(name)).findFirst().orElse(null);

        bookList.remove(bookdelete);
        return "redirect:/allbooks";
    }
}
