package de.rwth.idsg.brsm.web.rest;

import com.codahale.metrics.annotation.Timed;
import de.rwth.idsg.brsm.domain.Bike;
import de.rwth.idsg.brsm.domain.BikeStation;
import de.rwth.idsg.brsm.domain.User;
import de.rwth.idsg.brsm.repository.BikeRepository;
import de.rwth.idsg.brsm.repository.BikeStationRepository;
import de.rwth.idsg.brsm.security.AuthoritiesConstants;
import de.rwth.idsg.brsm.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * REST controller for managing BikeStation.
 */
@RestController
@RequestMapping("/app")
public class BikeStationResource {

    private final Logger log = LoggerFactory.getLogger(BikeStationResource.class);

    @Autowired
    private BikeStationRepository bikestationRepository;

    @Autowired
    private BikeRepository bikeRepository;

    @Autowired
    private UserService userService;

    /**
     * POST  /rest/bikestations -> Create a new bikestation.
     */
    @RequestMapping(value = "/rest/bikestations",
            method = RequestMethod.POST,
            produces = "application/json")
    @Timed
    public void create(@RequestBody BikeStation bikestation) {
        User currentUser = userService.getUserWithAuthorities();
        bikestation.setUser(currentUser);
        log.debug("REST request: to save BikeStation : {}", bikestation);
        bikestationRepository.save(bikestation);
    }

    /**
     * POST  /rest/bikestations/{id}/addBike -> Create add new bike to bikestation.
     */
    @RequestMapping(value = "/rest/bikestations/{id}/addBike",
            method = RequestMethod.POST,
            produces = "application/json")
    @Timed
    public void addBike(@PathVariable ("id") long bikeStationId, @RequestBody Bike bike) {
        log.debug("REST request to add Bike : {}", bike);
        BikeStation bikeStation = bikestationRepository.findOne(bikeStationId);
        bikeStation.addBike(bike);
        //bikeRepository.save(bike);
        bikestationRepository.save(bikeStation);
    }



    /**
     * GET  /rest/bikestations -> get all the bikestations.
     */
    @Transactional(readOnly=true)
    @RequestMapping(value = "/rest/bikestations",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public List<BikeStation> getAll() {
        log.debug("REST request to get all BikeStations");
        User currentUser = userService.getUserWithAuthorities();
        List<BikeStation> bikeStations = bikestationRepository.findByUser(currentUser);

        return bikeStations;
    }

    /**
     * GET  /rest/bikestations/:id -> get the "id" bikestation.
     */
    @RequestMapping(value = "/rest/bikestations/{id}",
            method = RequestMethod.GET,
            produces = "application/json")
    @Timed
    public BikeStation get(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to get BikeStation : {}", id);
        BikeStation bikestation = bikestationRepository.findOne(id);
        if (bikestation == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
        return bikestation;
    }

    /**
     * DELETE  /rest/bikestations/:id -> delete the "id" bikestation.
     */
    @RequestMapping(value = "/rest/bikestations/{id}",
            method = RequestMethod.DELETE,
            produces = "application/json")
    @Timed
    public void delete(@PathVariable Long id, HttpServletResponse response) {
        log.debug("REST request to delete BikeStation : {}", id);
        bikestationRepository.delete(id);
    }
}
