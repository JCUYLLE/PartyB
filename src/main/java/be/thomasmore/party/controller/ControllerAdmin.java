package be.thomasmore.party.controller;

import be.thomasmore.party.model.Party;
import be.thomasmore.party.model.Venue;
import be.thomasmore.party.repositories.PartyRepository;
import be.thomasmore.party.repositories.VenueRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class ControllerAdmin {
    private final Logger logger = LoggerFactory.getLogger(ControllerAdmin.class);

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private VenueRepository venueRepository;

    @ModelAttribute("party")
    public Party findParty(@PathVariable(required = false) Integer id) {
        logger.info("findParty " + id);
        if (id == null) return new Party();

        Optional<Party> optionalParty = partyRepository.findById(id);
        //noinspection OptionalIsPresent
        if (optionalParty.isPresent())
            return optionalParty.get();
        return null;
    }
    @GetMapping("/partynew")
    public String partyNew(Model model) {
        logger.info("partyNew ");
        model.addAttribute("party", new Party());
        model.addAttribute("venues", venueRepository.findAll());
        return "admin/partynew";
    }

    @PostMapping("/partynew")
    public String partyNewPost(Model model,
                               @ModelAttribute("party") Party party,
                               @RequestParam int venueId) {
        logger.info("partyNewPost -- new name=" + party.getName() + ", date=" + party.getDate());
        party.setVenue(new Venue(venueId));
        Party newParty = partyRepository.save(party);
        return "redirect:/partydetails/" + newParty.getId();
    }

    @GetMapping("/partyedit/{id}")
    public String partyEdit(Model model,
                            @PathVariable int id) {
        logger.info("partyEdit " + id);
        model.addAttribute("venues", venueRepository.findAll());
        return "admin/partyedit";
    }

    @PostMapping("/partyedit/{id}")
    public String partyEditPost(Model model,
                                @PathVariable int id,
                                @ModelAttribute("party") Party party,
                                @RequestParam int venueId) {
        logger.info("partyEditPost " + id + " -- new name=" + party.getName());
        if (party.getVenue().getId() != venueId) {
            party.setVenue(new Venue(venueId));
        }
        partyRepository.save(party);
        return "redirect:/partydetails/" + id;
    }



}