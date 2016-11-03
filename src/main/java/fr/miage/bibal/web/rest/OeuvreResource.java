package fr.miage.bibal.web.rest;

import com.codahale.metrics.annotation.Timed;
import fr.miage.bibal.domain.Oeuvre;

import fr.miage.bibal.repository.OeuvreRepository;
import fr.miage.bibal.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Oeuvre.
 */
@RestController
@RequestMapping("/api")
public class OeuvreResource {

    private final Logger log = LoggerFactory.getLogger(OeuvreResource.class);

    @Inject
    private OeuvreRepository oeuvreRepository;

    /**
     * POST  /oeuvres : Create a new oeuvre.
     *
     * @param oeuvre the oeuvre to create
     * @return the ResponseEntity with status 201 (Created) and with body the new oeuvre, or with status 400 (Bad Request) if the oeuvre has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/oeuvres",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Oeuvre> createOeuvre(@Valid @RequestBody Oeuvre oeuvre) throws URISyntaxException {
        log.debug("REST request to save Oeuvre : {}", oeuvre);
        if (oeuvre.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("oeuvre", "idexists", "A new oeuvre cannot already have an ID")).body(null);
        }
        Oeuvre result = oeuvreRepository.save(oeuvre);
        return ResponseEntity.created(new URI("/api/oeuvres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("oeuvre", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /oeuvres : Updates an existing oeuvre.
     *
     * @param oeuvre the oeuvre to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated oeuvre,
     * or with status 400 (Bad Request) if the oeuvre is not valid,
     * or with status 500 (Internal Server Error) if the oeuvre couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/oeuvres",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Oeuvre> updateOeuvre(@Valid @RequestBody Oeuvre oeuvre) throws URISyntaxException {
        log.debug("REST request to update Oeuvre : {}", oeuvre);
        if (oeuvre.getId() == null) {
            return createOeuvre(oeuvre);
        }
        Oeuvre result = oeuvreRepository.save(oeuvre);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("oeuvre", oeuvre.getId().toString()))
            .body(result);
    }

    /**
     * GET  /oeuvres : get all the oeuvres.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of oeuvres in body
     */
    @RequestMapping(value = "/oeuvres",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Oeuvre> getAllOeuvres(@RequestParam(required = false) Boolean withExemplaire) {
        if(withExemplaire != null){
            log.debug("REST request to get all Oeuvres with exemplaire");
            return oeuvreRepository.findWithExemplaire();
        }
        log.debug("REST request to get all Oeuvres");
        List<Oeuvre> oeuvres = oeuvreRepository.findAll();
        return oeuvres;
    }

    /**
     * GET  /oeuvres/:id : get the "id" oeuvre.
     *
     * @param id the id of the oeuvre to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the oeuvre, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/oeuvres/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Oeuvre> getOeuvre(@PathVariable Long id) {
        log.debug("REST request to get Oeuvre : {}", id);
        Oeuvre oeuvre = oeuvreRepository.findOne(id);
        return Optional.ofNullable(oeuvre)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /oeuvres/:id : delete the "id" oeuvre.
     *
     * @param id the id of the oeuvre to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/oeuvres/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteOeuvre(@PathVariable Long id) {
        log.debug("REST request to delete Oeuvre : {}", id);
        oeuvreRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("oeuvre", id.toString())).build();
    }

}
