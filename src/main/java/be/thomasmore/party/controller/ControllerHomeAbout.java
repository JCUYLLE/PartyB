package be.thomasmore.party.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ControllerHomeAbout {
    private final int mySpecialNumber = 35;

    @GetMapping(value = {"/", "/home", "/home/"})
    public String home (Model model){
        model.addAttribute("mySpecialNumber",mySpecialNumber);
        return "home";
    }

    @GetMapping("/about")
    public String about (){
        return "about";
    }




}
