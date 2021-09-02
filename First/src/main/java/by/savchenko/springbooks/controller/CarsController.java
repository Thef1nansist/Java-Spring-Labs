package by.savchenko.springbooks.controller;

import by.savchenko.springbooks.forms.CarsForm;
import by.savchenko.springbooks.model.Car;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Controller
public class CarsController {
    private static List<Car> carList = new ArrayList<>();
    static {
        carList.add(new Car("tesla","white"));
        carList.add(new Car("audi","red"));
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
        log.info("YES index was colled");
        return modelAndView;
    }
    @RequestMapping(value = {"/allbooks"}, method = RequestMethod.GET)
    public  ModelAndView personList(Model model){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("booklist");
        model.addAttribute("books", carList);
        return modelAndView;
    }
    @RequestMapping(value = "/addbook", method = RequestMethod.GET)
    public ModelAndView ShowAddListPage(Model model){
        ModelAndView modelAndView = new ModelAndView("addbook");
        CarsForm carsForm = new CarsForm();
        model.addAttribute("bookform", carsForm);
        return modelAndView;
    }
    @RequestMapping(value = "/addbook", method = RequestMethod.POST)
    public ModelAndView ActionAddListPage(Model model, @ModelAttribute("bookform") CarsForm carsForm){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("booklist");
        String title = carsForm.getMarka();
        String author = carsForm.getColor();

        if(title != null && title.length()>0 && author != null && author.length()>0){
            Car car = new Car(title, author);
            carList.add(car);
            model.addAttribute("books", carList);
            return modelAndView;
        }
        model.addAttribute("errorMessage", errorMessage);
        modelAndView.setViewName("addbook");
        return modelAndView;
    }
    @GetMapping("{update}")
    public ModelAndView UpdateGetProduct(Model model, @ModelAttribute("productUpdate") Car car){
        ModelAndView modelAndView = new ModelAndView("update");
        CarsForm carsForm = new CarsForm();
        carsForm.setMarka(car.getMarka());
        carsForm.setColor(car.getColor());
        ind  = carList.indexOf(car);
        model.addAttribute("bookform", carsForm);
        return  modelAndView;
    }

    @PostMapping("{update}")
    public ModelAndView UpdatePostProduct(Model model, @ModelAttribute("productUpdate") Car car){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("booklist");
        String title = car.getMarka();
        String author = car.getColor();
        if(title != null && title.length()>0 && author != null && author.length()>0){
            carList.get(ind).setMarka(title);
            carList.get(ind).setColor(author);
            model.addAttribute("books", carList);
            return  modelAndView;
        }
        model.addAttribute("booksList", carList);
        return modelAndView;
    }

    @GetMapping("/delete/{name}")
    public String DeleteProduct(@PathVariable(value = "name") String name){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("booklist");
        Car cardelete = carList.
                stream()
                .filter(x -> x.getMarka().equals(name)).findFirst().orElse(null);

        carList.remove(cardelete);
        return "redirect:/allbooks";
    }
}
