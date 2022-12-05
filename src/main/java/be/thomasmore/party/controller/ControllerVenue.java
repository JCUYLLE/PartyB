package be.thomasmore.party.controller;


import be.thomasmore.party.model.Venue;
import be.thomasmore.party.repositories.VenueRepository;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller

public class ControllerVenue {
    private final String[] venuenames = {"Carré", "Zillion", "Cherrymoon", "Boccaccio", "Carat"};
    private final Logger logger = (Logger) LoggerFactory.getLogger(ControllerVenue.class);
    private final Venue[] venues = {
            new Venue("Carré", "Website Carré", 100, true, true, false, false, "Willebroek", 5),
            new Venue("Zillion", "Website Zillion", 250, true, true, false, false, "Antwerpen", 1),
            new Venue("Cherrymoon", "Website Cherrymoon", 150, true, true, false, false, "Knokke", 4),
            new Venue("Boccaccio", "Website Boccaccio", 125, false, false, true, true, "Ergens", 10),
            new Venue("Carat", "Website Carat", 225, false, true, false, true, "Waarschijnlijk", 15)
    };

    @Autowired
    private VenueRepository venueRepository;



    @GetMapping("/venuelist")
    public String venuelist(Model model) {
        boolean showFilter = false;
        Iterable<Venue> venues = venueRepository.findAll();
        model.addAttribute("venues", venues);
        model.addAttribute("showFilters", showFilter);
        return "venuelist";
    }

    @GetMapping({"/venuedetails", "/venuedetails/", "/venuedetails/{venuename}"})
    public String venuedetails(Model model, @PathVariable(required = false) String venuename) {
        model.addAttribute("venuename", venuename);
        return "venuedetails";
    }

    @GetMapping({"/venuedetailsbyindex", "/venuedetailsbyindex/", "/venuedetailsbyindex/{venueindex}"})
    public String venuedetailsbyindex(Model model, @PathVariable(required = false) String venueindex) {
        Venue venue = null;
        if (venueindex != null && Integer.parseInt(venueindex) % 1 == 0 && Integer.parseInt(venueindex) >= 0 && Integer.parseInt(venueindex) < venuenames.length) {
            //get venue data here
            venue = venues[Integer.parseInt(venueindex)];
        }

        int prevIndex = Integer.parseInt(venueindex) - 1;
        if (prevIndex < 0) {
            prevIndex = venuenames.length - 1;
        }

        int nextIndex = Integer.parseInt(venueindex) + 1;
        if (nextIndex > venuenames.length - 1) {
            nextIndex = 0;
        }

        model.addAttribute("venue", venue);
        model.addAttribute("prevIndex", prevIndex);
        model.addAttribute("nextIndex", nextIndex);
        return "venuedetailsbyindex";
    }

    @GetMapping({"/venuelist/filter"})
    public String venueListWithFilter(Model model, @RequestParam(required = false) Integer minCapacity){
        boolean  showFilter = true;
        logger.info(String.format("venueListWithFilter -- min%d", minCapacity));
        Iterable<Venue> venues = venueRepository.findAll();
        model.addAttribute("venues",venues);
        model.addAttribute("showFilters", showFilter);
        return"venuelist";
    }



    @GetMapping({"/venuedetailsbyid","/venuedetailsbyid/{id}"})
    public String venueDetailsById(Model model, @PathVariable(required = false) Integer id) {

        Optional oVenue = null;
        Venue venue = null;
        int venueCount = 0;
        boolean idNull = false;

        venueCount = (int) venueRepository.count();

        if(id == null || id != (int) id){
            id = 0;
        }
        if(id <= 0 || id > venueCount){
            idNull = true;
        }

        oVenue = venueRepository.findById(id);
        if (oVenue.isPresent()) {
            venue = (Venue) oVenue.get();
        }

        int prevId = id-1 ;
        if(prevId<1){
            prevId = venueCount;
        }

        int nextId = id+1;
        if(nextId>venueCount){
            nextId = 1;
        }

        model.addAttribute("nextId", nextId);
        model.addAttribute("prevId",prevId);
        model.addAttribute("venue", venue);
        model.addAttribute("idNull",idNull);
        return "venuedetailsbyid";
    }

    @GetMapping("venuelist/outdoor/{outdoor}")
    public String venuelistOutdoor (Model model, @PathVariable(required = false) String outdoor)
    {Iterable<Venue> venues = venueRepository.findAll();
        if(outdoor.equals("yes")) {
            venues = venueRepository.findByOutdoor(true);
        }
        else if(outdoor.equals("no")) {
            venues = venueRepository.findByOutdoor(false);
        }

        model.addAttribute("venues", venues);
        return "venuelist";
    }
}
