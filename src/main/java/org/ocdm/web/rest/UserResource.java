package org.ocdm.web.rest;

import org.ocdm.config.Constants;
import com.codahale.metrics.annotation.Timed;
import org.ocdm.domain.User;
import org.ocdm.repository.UserRepository;
import org.ocdm.security.AuthoritiesConstants;
import org.ocdm.service.MailService;
import org.ocdm.service.UserService;
import org.ocdm.service.dto.EmplMDTO;
import org.ocdm.service.dto.EmplMM1M2DTO;
import org.ocdm.service.dto.UserDTO;
import org.ocdm.service.dto.ParticTacheDTO;
import org.ocdm.web.rest.vm.ManagedUserVM;
import org.ocdm.web.rest.util.HeaderUtil;
import org.ocdm.web.rest.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import io.swagger.annotations.ApiParam;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the User entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 */
@RestController
@RequestMapping("/api")
public class UserResource {

    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    private static final String ENTITY_NAME = "userManagement";

    private final UserRepository userRepository;

    private final MailService mailService;

    private final UserService userService;

    public UserResource(UserRepository userRepository, MailService mailService,
            UserService userService) {

        this.userRepository = userRepository;
        this.mailService = mailService;
        this.userService = userService;
    }

    /**
     * POST  /users  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param managedUserVM the user to create
     * @return the ResponseEntity with status 201 (Created) and with body the new user, or with status 400 (Bad Request) if the login or email is already in use
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/users")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity createUser(@Valid @RequestBody ManagedUserVM managedUserVM) throws URISyntaxException {
        log.debug("REST request to save User : {}", managedUserVM);

        if (managedUserVM.getId() != null) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new user cannot already have an ID"))
                .body(null);
        // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase()).isPresent()) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "userexists", "Login already in use"))
                .body(null);
        } else if (userRepository.findOneByEmail(managedUserVM.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                .headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "emailexists", "Email already in use"))
                .body(null);
        } else {
            User newUser = userService.createUser(managedUserVM);
            mailService.sendCreationEmail(newUser);
            return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
                .headers(HeaderUtil.createAlert( "userManagement.created", newUser.getLogin()))
                .body(newUser);
        }
    }

    /**
     * PUT  /users : Updates an existing User.
     *
     * @param managedUserVM the user to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated user,
     * or with status 400 (Bad Request) if the login or email is already in use,
     * or with status 500 (Internal Server Error) if the user couldn't be updated
     */
    @PutMapping("/users")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody ManagedUserVM managedUserVM) {
        log.debug("REST request to update User : {}", managedUserVM);
        Optional<User> existingUser = userRepository.findOneByEmail(managedUserVM.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(managedUserVM.getId()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "emailexists", "Email already in use")).body(null);
        }
        existingUser = userRepository.findOneByLogin(managedUserVM.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(managedUserVM.getId()))) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "userexists", "Login already in use")).body(null);
        }
        Optional<UserDTO> updatedUser = userService.updateUser(managedUserVM);

        return ResponseUtil.wrapOrNotFound(updatedUser,
            HeaderUtil.createAlert("userManagement.updated", managedUserVM.getLogin()));
    }

    /**
     * GET  /users : get all users.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and with body all users
     */
    @GetMapping("/users")
    @Timed
    public ResponseEntity<List<UserDTO>> getAllUsers(@ApiParam Pageable pageable) {
        final Page<UserDTO> page = userService.getAllManagedUsers(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/users");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * @return a string list of the all of the roles
     */
    @GetMapping("/users/authorities")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public List<String> getAuthorities() {
        return userService.getAuthorities();
    }

    /**
     * GET  /users/:login : get the "login" user.
     *
     * @param login the login of the user to find
     * @return the ResponseEntity with status 200 (OK) and with body the "login" user, or with status 404 (Not Found)
     */
    @GetMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    @Timed
    public ResponseEntity<UserDTO> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        return ResponseUtil.wrapOrNotFound(
            userService.getUserWithAuthoritiesByLogin(login)
                .map(UserDTO::new));
    }

    /**
     * DELETE /users/:login : delete the "login" User.
     *
     * @param login the login of the user to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    @Timed
    @Secured(AuthoritiesConstants.ADMIN)
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        log.debug("REST request to delete User: {}", login);
        userService.deleteUser(login);
        return ResponseEntity.ok().headers(HeaderUtil.createAlert( "userManagement.deleted", login)).build();
    }

   /* *//**
     * POST  /users/tache/:tache : get all users related to the "tache".
     *
     * @param tache the tache to retrieve users from
     * @return the ResponseEntity with status 200 (OK) and with body the user, or with status 404 (Not Found)
     *//*
    @PostMapping("/users/tache/")
    @Timed
    public List<User> getUserByTache(@RequestBody Tache tache) {
        log.debug("REST request to get all User related to the task : {}", tache);
        List<User> userList = userService.findAllByProjet(tache);
        return tacheList;
    }*/


    /**
     * GET  /users/taches/attributionTache/:id : get all users related to the "tache".
     *
     * @param id the id of the tache to retrieve users from
     * @return the ResponseEntity with status 200 (OK) and with body the tache, or with status 404 (Not Found)
     */
    @GetMapping("/users/taches/attributionTache/{id}")
    @Timed
    public List<ParticTacheDTO> getParticByTache(@PathVariable Long id) {
        System.out.println(id);
        log.debug("REST request to get all users related to the tache : {}", id);
        List<ParticTacheDTO> res = userService.getParticByTache(id);
        System.out.println("voici le contenu de res ))) : " + Arrays.toString(res.toArray()));
        return res;
//        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }


    /**
     * GET  /users/taches/attributionTache/personnesNonAffectees/ : get all non affectees users related to the "tache".
     *
     * //@param id the id of the tache to retrieve users from
     * @return the ResponseEntity with status 200 (OK) and with body the tache, or with status 404 (Not Found)
     */
    @GetMapping("/users/taches/attributionTache/personnesNonAffectees/")
    @Timed
    public List<User> queryPersonnesNonAffectees() {
        System.out.println();
        log.debug("REST request to get all non affectees users related to the tache : {}");
        List<User> res = userService.queryPersonnesNonAffectees();
        //System.out.println(Arrays.toString(res.toArray()));
        return res;
//        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }


//cree par moiSina
    /**
     * GET  /users/taches/attributionTache/personnesNonAffectees/ : get all non affectees users related to the "tache".
     *
     * //@param id the id of the tache to retrieve users from
     * @return the ResponseEntity with status 200 (OK) and with body the tache, or with status 404 (Not Found)
     */
    @GetMapping("/users/listeIngenieurs/")
    @Timed
    public List<User> getAllInge() {
        System.out.println();
        log.debug("REST request to get all non affectees users related to the tache : {}");
        List<User> res = userService.getAllInge();
        //System.out.println(Arrays.toString(res.toArray()));
        return res;
//        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(res));
    }


    /**
     * GET  /users/getAllIngeM : get all users and his available days at M.
     *
     * @return the ResponseEntity with status 200 (OK) and with body all users
     */
    @GetMapping("/users/getAllIngeM")
    @Timed
    public List<EmplMM1M2DTO> getAllIngeM() {
        System.out.println();
        log.debug("REST request to get all non affectees users related to the tache : {}");
        List<EmplMM1M2DTO> res = userService.getAllIngeM();
        return res;
    }


    /**
     * GET  /users/getAllIngeNoAttrib : get all users and his available days at M.
     *
     * @return the ResponseEntity with status 200 (OK) and with body all users
     */
    @GetMapping("/users/getAllIngeNoAttrib")
    @Timed
    public List<String> getAllIngeNoAttrib() {
        System.out.println();
        log.debug("REST request to get all non affectees users related to the tache : {}");
        List<String> res = userService.getAllIngeNoAttrib();
        return res;
    }





//    /**
//     * GET  /users/getAllIngeM : get all users and his available days at M.
//     *
//     * @return the ResponseEntity with status 200 (OK) and with body all users
//     */
//    @GetMapping("/users/getAllIngeM1")
//    @Timed
//    public List<EmplMDTO> getAllIngeM1() {
//        System.out.println();
//        log.debug("REST request to get all non affectees users related to the tache : {}");
//        List<EmplMDTO> res = userService.getAllIngeM1();
//        return res;
//    }


//    /**
//     * GET  /users/getAllIngeM : get all users and his available days at M.
//     *
//     * @return the ResponseEntity with status 200 (OK) and with body all users
//     */
//    @GetMapping("/users/getAllIngeM2")
//    @Timed
//    public List<EmplMDTO> getAllIngeM2() {
//        System.out.println();
//        log.debug("REST request to get all non affectees users related to the tache : {}");
//        List<EmplMDTO> res = userService.getAllIngeM2();
//        return res;
//    }


    /**
     * GET  /users/getIdFromLogin/:id : get all users and his available days at M.
     *
     * @return the ResponseEntity with status 200 (OK) and with body all users
     */
    @GetMapping("/users/getIdFromLogin/{login}")
    @Timed
    public Long getIdFromLogin(@PathVariable String login) {
        System.out.println("voici la valeur de login dans UserResource" + login);
        log.debug("REST request to get all non affectees users related to the tache : {}");
        Long res = userService.getIdFromLogin(login);
        System.out.println("voici la valeur de login dans UserResource au niveau du retour " + res);
        return res;
    }


}
