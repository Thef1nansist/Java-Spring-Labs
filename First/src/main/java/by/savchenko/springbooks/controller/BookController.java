package by.savchenko.springbooks.controller;

import by.savchenko.springbooks.forms.BookForm;
import by.savchenko.springbooks.model.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
@Controller
public class BookController {
    private static List<Book> bookList = new ArrayList<>();
    static {
        bookList.add(new Book("Full stack 1","Savchenko Vlad"));
        bookList.add(new Book("Full stack 2","Savchenko Vladick"));
    }
    private static int ind = 0;

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
    @GetMapping("{update}")
    public ModelAndView UpdateGetProduct(Model model, @ModelAttribute("productUpdate") Book book){
        ModelAndView modelAndView = new ModelAndView("update");
        BookForm bookForm = new BookForm();
        bookForm.setTitle(book.getTitle());
        bookForm.setAuthor(book.getAuthor());
        ind  =bookList.indexOf(book);
        model.addAttribute("bookform", bookForm);
        return  modelAndView;
    }

    @PostMapping("{update}")
    public ModelAndView UpdatePostProduct(Model model, @ModelAttribute("productUpdate") Book book){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("booklist");
        String title = book.getTitle();
        String author = book.getAuthor();
        if(title != null && title.length()>0 && author != null && author.length()>0){
            bookList.get(ind).setTitle(title);
            bookList.get(ind).setAuthor(author);
            model.addAttribute("books", bookList);
            return  modelAndView;
        }
        model.addAttribute("bookList",bookList);
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
