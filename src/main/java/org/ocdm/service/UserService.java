package org.ocdm.service;

import org.ocdm.domain.Authority;
import org.ocdm.domain.User;
import org.ocdm.repository.AuthorityRepository;
import org.ocdm.repository.PersistentTokenRepository;
import org.ocdm.config.Constants;
import org.ocdm.repository.UserRepository;
import org.ocdm.security.AuthoritiesConstants;
import org.ocdm.security.SecurityUtils;
import org.ocdm.service.dto.*;
import org.ocdm.service.util.RandomUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service class for managing users.
 */
@Service
@Transactional
public class UserService {

    Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
    ZoneId defaultZoneId = ZoneId.systemDefault();
    Date date = new Date();
    Instant instant = date.toInstant();











    private static final DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final PersistentTokenRepository persistentTokenRepository;

    private final AuthorityRepository authorityRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, PersistentTokenRepository persistentTokenRepository, AuthorityRepository authorityRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.persistentTokenRepository = persistentTokenRepository;
        this.authorityRepository = authorityRepository;
    }

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        return userRepository.findOneByActivationKey(key)
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                log.debug("Activated user: {}", user);
                return user;
            });
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
       log.debug("Reset user password for reset key {}", key);

       return userRepository.findOneByResetKey(key)
           .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
           .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                return user;
           });
    }

    public Optional<User> requestPasswordReset(String mail) {
        return userRepository.findOneByEmail(mail)
            .filter(User::getActivated)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(Instant.now());
                return user;
            });
    }

    public User createUser(String login, String password, String firstName, String lastName, String email,
        String imageUrl, String langKey) {

        User newUser = new User();
        Authority authority = authorityRepository.findOne(AuthoritiesConstants.USER);
        Set<Authority> authorities = new HashSet<>();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(login);
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setImageUrl(imageUrl);
        newUser.setLangKey(langKey);
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        authorities.add(authority);
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public User createUser(UserDTO userDTO) {
        User user = new User();
        user.setLogin(userDTO.getLogin());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());
        user.setImageUrl(userDTO.getImageUrl());
        if (userDTO.getLangKey() == null) {
            user.setLangKey("fr"); // default language
        } else {
            user.setLangKey(userDTO.getLangKey());
        }
        if (userDTO.getAuthorities() != null) {
            Set<Authority> authorities = new HashSet<>();
            userDTO.getAuthorities().forEach(
                authority -> authorities.add(authorityRepository.findOne(authority))
            );
            user.setAuthorities(authorities);
        }
        String encryptedPassword = passwordEncoder.encode(RandomUtil.generatePassword());
        user.setPassword(encryptedPassword);
        user.setResetKey(RandomUtil.generateResetKey());
        user.setResetDate(Instant.now());
        user.setActivated(true);
        userRepository.save(user);
        log.debug("Created Information for User: {}", user);
        return user;
    }

    /**
     * Update basic information (first name, last name, email, language) for the current user.
     *
     * @param firstName first name of user
     * @param lastName last name of user
     * @param email email id of user
     * @param langKey language key
     * @param imageUrl image URL of user
     */
    public void updateUser(String firstName, String lastName, String email, String langKey, String imageUrl) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setEmail(email);
            user.setLangKey(langKey);
            user.setImageUrl(imageUrl);
            log.debug("Changed Information for User: {}", user);
        });
    }

    /**
     * Update all information for a specific user, and return the modified user.
     *
     * @param userDTO user to update
     * @return updated user
     */
    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        return Optional.of(userRepository
            .findOne(userDTO.getId()))
            .map(user -> {
                user.setLogin(userDTO.getLogin());
                user.setFirstName(userDTO.getFirstName());
                user.setLastName(userDTO.getLastName());
                user.setEmail(userDTO.getEmail());
                user.setImageUrl(userDTO.getImageUrl());
                user.setActivated(userDTO.isActivated());
                user.setLangKey(userDTO.getLangKey());
                Set<Authority> managedAuthorities = user.getAuthorities();
                managedAuthorities.clear();
                userDTO.getAuthorities().stream()
                    .map(authorityRepository::findOne)
                    .forEach(managedAuthorities::add);
                log.debug("Changed Information for User: {}", user);
                return user;
            })
            .map(UserDTO::new);
    }

    public void deleteUser(String login) {
        userRepository.findOneByLogin(login).ifPresent(user -> {
            userRepository.delete(user);
            log.debug("Deleted User: {}", user);
        });
    }

    public void changePassword(String password) {
        userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).ifPresent(user -> {
            String encryptedPassword = passwordEncoder.encode(password);
            user.setPassword(encryptedPassword);
            log.debug("Changed password for User: {}", user);
        });
    }

    @Transactional(readOnly = true)
    public Page<UserDTO> getAllManagedUsers(Pageable pageable) {
        return userRepository.findAllByLoginNot(pageable, Constants.ANONYMOUS_USER).map(UserDTO::new);
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthoritiesByLogin(String login) {
        return userRepository.findOneWithAuthoritiesByLogin(login);
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities(Long id) {
        return userRepository.findOneWithAuthoritiesById(id);
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities() {
        return userRepository.findOneWithAuthoritiesByLogin(SecurityUtils.getCurrentUserLogin()).orElse(null);
    }

    /**
     * Persistent Token are used for providing automatic authentication, they should be automatically deleted after
     * 30 days.
     * <p>
     * This is scheduled to get fired everyday, at midnight.
     */
    @Scheduled(cron = "0 0 0 * * ?")
    public void removeOldPersistentTokens() {
        LocalDate now = LocalDate.now();
        persistentTokenRepository.findByTokenDateBefore(now.minusMonths(1)).forEach(token -> {
            log.debug("Deleting token {}", token.getSeries());
            User user = token.getUser();
            user.getPersistentTokens().remove(token);
            persistentTokenRepository.delete(token);
        });
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(Instant.now().minus(3, ChronoUnit.DAYS));
        for (User user : users) {
            log.debug("Deleting not activated user {}", user.getLogin());
            userRepository.delete(user);
        }
    }

    /**
     * @return a list of all the authorities
     */
    public List<String> getAuthorities() {
        return authorityRepository.findAll().stream().map(Authority::getName).collect(Collectors.toList());
    }




    @Transactional(readOnly = true)
    public List<ParticTacheDTO> getParticByTache(Long id) {


        List<ParticTacheDTO> listeRecupereParticipTacheDTO= new ArrayList<ParticTacheDTO>();


        listeRecupereParticipTacheDTO =  userRepository.getParticByTache(id);
        System.out.println(" voici le contenu de listeRecupereParticipTacheDTO.toString() : " + listeRecupereParticipTacheDTO.toString());

        //System.out.println(EmplMDTO.toString());
//        System.out.println("impression de ma liste ListeRecupereM @@@@@@@@@@@@@@@@@@@@@@:");
//        listeRecupereM.forEach(System.out::println);
        //listeRecupereM.forEach(System.out::println);
//        ci dessous, je met le contenu de ma List listeRecupereM dans ma HashMap listeRecupereMapM;


//        ci dessous ca fonctionne mais je l'ai commenté car javais limrpession que ca contredisait ce que je connnaissais'
        for (ParticTacheDTO i : listeRecupereParticipTacheDTO) i.setCountFloat((i.getCount())/Constants.CONVERSIONJOURQUARTJOURNEEFLOAT);





        return listeRecupereParticipTacheDTO;
    }

    @Transactional(readOnly = true)
    public List<User> queryPersonnesNonAffectees() {
        return userRepository.queryPersonnesNonAffectees();
    }


    @Transactional(readOnly = true)
    public List<User> getAllInge() {
        return userRepository.getAllInge();
    }


    @Transactional(readOnly = true)
    public List<String> getAllIngeNoAttrib() {

        //    Long listeRecupereIngePasDansAttribTache;
        List<String> listeRecupereIngePasDansAttribTache= new ArrayList<String>();

        System.out.println(" avant d'entrer: ");
        listeRecupereIngePasDansAttribTache = userRepository.getAllIngeNoAttrib();
        System.out.println(" [[[[[[[[[[[[[ this.listeRecupereIngePasDansAttribTache : " + listeRecupereIngePasDansAttribTache);
        return listeRecupereIngePasDansAttribTache;
    }

    @Transactional(readOnly = true)
    public List<EmplMM1M2DTO> getAllIngeM() {


        List<EmplMDTO> listeRecupereM= new ArrayList<EmplMDTO>();
        List<EmplMDTO> listeRecupereMplus1= new ArrayList<EmplMDTO>();
        List<EmplMDTO> listeRecupereMplus2= new ArrayList<EmplMDTO>();
        List<EmplMM1M2DTO> listeRecupereMMplus1Mplus2= new ArrayList<EmplMM1M2DTO>();

        List myList = new ArrayList();

        Map<Long, EmplMDTO> listeRecupereMapM = new HashMap<Long, EmplMDTO>();
        Map<Long, EmplMDTO> listeRecupereMapMplus1 = new HashMap<Long, EmplMDTO>();
        Map<Long, EmplMDTO> listeRecupereMapMplus2 = new HashMap<Long, EmplMDTO>();
        Map<Long, EmplMM1M2DTO> listeRecupereMapMMplus1Mplus2= new HashMap<Long, EmplMM1M2DTO>();
        //EmplMDTO EmplMDTO;


//        CONCERNANT LE MOIS PROCHAIN , AUTREMENT DIT LE MOIS M

//        voici la difference entre Date et Calendar:
//        The difference between Date and Calendar is that Date class operates with specific instant in time and
//            Calendar operates with difference between two dates. The Calendar class gives you possibility for converting
//        between a specific instant in time and a set of calendar fields such as HOUR, YEAR, MONTH, DAY_OF_MONTH. You can also
//        manipulate with the calendar fields, for example getting the date of your grandmother birthday :).

//        ci dessous , je recupere la date d'aujourdhui'
        Date currentDate = new Date();

//The java.util.Calendar.getInstance() method gets a calendar using the specified time zone and specified locale.
// autrement dit, ci dessous , je me cree un calendrier
        Calendar c = Calendar.getInstance();
//        dans ce calendrier, je vais y instaurer la datae d'aujourdhui
        c.setTime(currentDate);
//        j'ai donc mainteant un calendrier qui est regle sur la date d'aujorudhui et sur les horaire et les specificites locales;

        int nombreDeJourDuMoisActuel = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
        int jourDuMoisDeLaDateDaujourdhui = calendar.get(Calendar.DATE);
        //System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~" + dateFormat.format(currentDate));
        int nombreDeJourRestantUntilFinDuMoisMInt = nombreDeJourDuMoisActuel - jourDuMoisDeLaDateDaujourdhui;
        Long nombreDeJourRestantUntilFinDuMoisM = Long.valueOf(nombreDeJourRestantUntilFinDuMoisMInt);
float nombreDeJourRestantUntilFinDuMoisMDouble = nombreDeJourRestantUntilFinDuMoisM.floatValue();

    //        ci dessous, je recupere la date du 1er jour du mois M
        c.set(Calendar.DAY_OF_MONTH, 1);
        Date premierJourMoisM = c.getTime();
        //System.out.println("voici mon syso de premier jour du mois M &&&&&&&&&&&&&&&&&&&&&&>>> " + dateFormat.format(premierJourMoisM));
        //System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~" + dateFormat.format(currentDate));

        //        ci dessous , je transforme mon premierJourMoisM en LocalDateTime
        Instant instant = premierJourMoisM.toInstant();
        LocalDate premierJourMoisMlocalDateTime = instant.atZone(defaultZoneId).toLocalDate();
        //System.out.println("localDateTime &&&&&&&&&&&&&&&&&&&&&&>>> : " + premierJourMoisMlocalDateTime);
        //       fin  ci dessous , je transforme mon premierJourMoisM en LocalDateTime
    // fin       ci dessous, je recupere la date du 1er jour du mois M


    //        ci dessous, je recupere la date du dernier jour du mois M
        c.set(Calendar.DATE, c.getActualMaximum(Calendar.DATE));
        Date dernierJourMoisM = c.getTime();
        //System.out.println("voici mon syso de dernier jour du mois M------------------->>>> " + dateFormat.format(dernierJourMoisM));

        //        ci dessous , je transforme mon dernierJourMoisM en LocalDateTime
        Instant instant2 = dernierJourMoisM.toInstant();
        LocalDate dernierJourMoisMlocalDateTime = instant2.atZone(defaultZoneId).toLocalDate();
        //System.out.println("localDateTime &&&&&&&&&&&&&&&&&&&&&&>>> : " + dernierJourMoisMlocalDateTime);
        //   fin     ci dessous , je transforme mon dernierJourMoisM en LocalDateTime
    // fin       ci dessous, je recupere la date du dernier jour du mois M


//   FIN     CONCERNANT LE MOIS PROCHAIN , AUTREMENT DIT LE MOIS M




//        CONCERNANT LE MOIS PROCHAIN , AUTREMENT DIT LE MOIS PLUS 1
//        ci dessous, je recupere le nombre de jour du mois Prochain
        Date currentDate1 = new Date();
//        System.out.println("voici mon syso de currentDate ------------------->>>> " + dateFormat.format(currentDate));
        Calendar c1 = Calendar.getInstance();
        c1.setTime(currentDate1);
//        System.out.println("voici mon syso de c ------------------->>>> " + c);
        c1.add(Calendar.MONTH, 1);
        int nombreDeJourDuMoisPlus1Int = c1.getActualMaximum(Calendar.DAY_OF_MONTH);
//        System.out.println(" voici le nombre de jours max du mois prohcain ------------------->>>> : "+ c.getActualMaximum(Calendar.DAY_OF_MONTH));
//        ci dessous , on convertit le Long en Int.
        Long nombreDeJourDuMoisPlus1 = Long.valueOf(nombreDeJourDuMoisPlus1Int);
        float nombreDeJourDuMoisPlus1Double = nombreDeJourDuMoisPlus1.floatValue();
        Date currentDatePlus1 = c1.getTime();
//        System.out.println("voici mon syso de currentDatePlusOne ------------------->>>> : " + dateFormat.format(currentDatePlusOne));
// fin ci dessous, je recupere le nombre de jour du mois Prochain



    //        ci dessous, je recupere la date du 1er jour du mois M+1
        c1.set(Calendar.DAY_OF_MONTH, 1);
        Date premierJourMoisplus1 = c1.getTime();
        //System.out.println("voici mon syso de premier jour du mois M+1------------------->>>> " + dateFormat.format(premierJourMoisplus1));
        //        ci dessous , je transforme mon premierJourMoisMplus1 en LocalDateTime
        Instant instantMplus1 = premierJourMoisplus1.toInstant();
        LocalDate premierJourMoisMplus1localDateTime = instantMplus1.atZone(defaultZoneId).toLocalDate();
        //System.out.println("localDateTime Mplus1&&&&&&&&&&&&&&&&&&&&&&>>> : " + premierJourMoisMplus1localDateTime);
        //       fin  ci dessous , je transforme mon premierJourMoisM en LocalDateTime
    // fin       ci dessous, je recupere la date du 1er jour du mois M+1


    //        ci dessous, je recupere la date du dernier jour du mois M+1
        c1.set(Calendar.DATE, c1.getActualMaximum(Calendar.DATE));
        Date dernierJourMoisplus1 = c1.getTime();
        //System.out.println("voici mon syso de dernier jour du mois M+1------------------->>>> " + dateFormat.format(dernierJourMoisplus1));

        //        ci dessous , je transforme mon dernierJourMoisM en LocalDateTime
        Instant instant2Mplus1 = dernierJourMoisplus1.toInstant();
        LocalDate dernierJourMoisMplus1localDateTime = instant2Mplus1.atZone(defaultZoneId).toLocalDate();
        //System.out.println("localDateTime &&&&&&&&&&&&&&&&&&&&&&>>> : " + dernierJourMoisMplus1localDateTime);
        //   fin     ci dessous , je transforme mon dernierJourMoisMplus1 en LocalDateTime
    // fin       ci dessous, je recupere la date du dernier jour du mois M+1
//   FIN     CONCERNANT LE MOIS PROCHAIN , AUTREMENT DIT LE MOIS PLUS 1






        //        CONCERNANT LE MOIS PROCHAIN , AUTREMENT DIT LE MOIS PLUS 2
//        ci dessous, je recupere le nombre de jour du mois Mplus2
        Date currentDate2 = new Date();
//        System.out.println("voici mon syso de currentDate ------------------->>>> " + dateFormat.format(currentDate));
        Calendar c2 = Calendar.getInstance();
        c2.setTime(currentDate2);
//        System.out.println("voici mon syso de c ------------------->>>> " + c);
        c2.add(Calendar.MONTH, 2);
        int nombreDeJourDuMoisPlus2Int = c2.getActualMaximum(Calendar.DAY_OF_MONTH);
//        System.out.println(" voici le nombre de jours max du mois plus 2 ------------------->>>> : "+ c.getActualMaximum(Calendar.DAY_OF_MONTH));
        //        ci dessous , on convertit le Long en Int.
        Long nombreDeJourDuMoisPlus2 = Long.valueOf(nombreDeJourDuMoisPlus2Int);
        float nombreDeJourDuMoisPlus2Double = nombreDeJourDuMoisPlus2.floatValue();
        Date currentDatePlus2 = c2.getTime();
//        System.out.println("voici mon syso de currentDatePlusOne ------------------->>>> : " + dateFormat.format(currentDatePlusOne));
// fin ci dessous, je recupere le nombre de jour du mois Prochain



        //        ci dessous, je recupere la date du 1er jour du mois M+2
        c2.set(Calendar.DAY_OF_MONTH, 2);
        Date premierJourMoisplus2 = c2.getTime();
        //System.out.println("voici mon syso de premier jour du mois M+2------------------->>>> " + dateFormat.format(premierJourMoisplus2));
        //        ci dessous , je transforme mon premierJourMoisMplus1 en LocalDateTime
        Instant instantMplus2 = premierJourMoisplus2.toInstant();
        LocalDate premierJourMoisMplus2localDateTime = instantMplus2.atZone(defaultZoneId).toLocalDate();
        //System.out.println("localDateTime Mplus2&&&&&&&&&&&&&&&&&&&&&&>>> : " + premierJourMoisMplus2localDateTime);
        //       fin  ci dessous , je transforme mon premierJourMoisMplus2 en LocalDateTime
        // fin       ci dessous, je recupere la date du 1er jour du mois M+2


        //        ci dessous, je recupere la date du dernier jour du mois M+2
        c2.set(Calendar.DATE, c2.getActualMaximum(Calendar.DATE));
        Date dernierJourMoisplus2 = c2.getTime();
        //System.out.println("voici mon syso de dernier jour du mois M+2------------------->>>> " + dateFormat.format(dernierJourMoisplus2));

        //        ci dessous , je transforme mon dernierJourMoisplus2 en LocalDateTime
        Instant instant2Mplus2 = dernierJourMoisplus2.toInstant();
        LocalDate dernierJourMoisMplus2localDateTime = instant2Mplus2.atZone(defaultZoneId).toLocalDate();
        //System.out.println("localDateTime &&&&&&&&&&&&&&&&&&&&&&>>> : " + dernierJourMoisMplus2localDateTime);
        //   fin     ci dessous , je transforme mon dernierJourMoisplus2 en LocalDateTime
        // fin       ci dessous, je recupere la date du dernier jour du mois M+2
//   FIN     CONCERNANT LE MOIS PROCHAIN , AUTREMENT DIT LE MOIS PLUS 2





//CONCERNANT M
//        voici la version propre de ce' que je devrai envoyer'
        System.out.println("impression des donnees de ma liste ListeRecupereM @@@@@@@@@@@@@@@@@@@@@@:" + nombreDeJourRestantUntilFinDuMoisM + " , " + premierJourMoisMlocalDateTime + " , " + dernierJourMoisMlocalDateTime);
        listeRecupereM =  userRepository.getAllIngeM(nombreDeJourRestantUntilFinDuMoisMDouble, premierJourMoisMlocalDateTime, dernierJourMoisMlocalDateTime);

        //System.out.println(EmplMDTO.toString());
        System.out.println("impression de ma liste ListeRecupereM @@@@@@@@@@@@@@@@@@@@@@:");
        listeRecupereM.forEach(System.out::println);
        //listeRecupereM.forEach(System.out::println);
//        ci dessous, je met le contenu de ma List listeRecupereM dans ma HashMap listeRecupereMapM;


//        ci dessous ca fonctionne mais je l'ai commenté car javais limrpession que ca contredisait ce que je connnaissais'
        for (EmplMDTO i : listeRecupereM) listeRecupereMapM.put(i.getId(),i);


        //ci dessous j'affichei le contenu de ma Hashmap:
        System.out.println("impression de ma hashmap listeRecupereMapM °°°°°°°°°°°°°°°°°°°°:");
        for (Map.Entry<Long, EmplMDTO> entry : listeRecupereMapM.entrySet()) {
            //System.out.println(entry.getValue().getId());
            System.out.println(entry.getKey() + ":" + entry.getValue().toString());
            //listeRecupereMapM.forEach((key, value) -> System.out.println(key + ":" + value));
            //listeRecupereMapMMplus1Mplus2.put(entry.getKey(), entry.getValue().getId());
            //entry.getValue().getId();
            //System.out.println(entry.getKey()+" : "+entry.getValue());
        }






//CONCERNANT M PLUS 1
        listeRecupereMplus1 =  userRepository.getAllIngeM1(nombreDeJourDuMoisPlus1Double, premierJourMoisMplus1localDateTime, dernierJourMoisMplus1localDateTime);
        System.out.println("impression de ma liste ListeRecupereMplus1 ^^^^^^^^^^^^^^^:");
        listeRecupereMplus1.forEach(System.out::println);
        //        ci dessous, je met le contenu de ma List listeRecupereM dans ma HashMap listeRecupereMapM;
//        for (EmplMDTO i : listeRecupereMplus1) {
//            listeRecupereMapMplus1.put(i.getId(),i);
//        }

//        ci dessous ca fonctionne mais je l'ai commenté car javais limrpession que ca contredisait ce que je connnaissais'
        for (EmplMDTO i : listeRecupereMplus1) listeRecupereMapMplus1.put(i.getId(),i);

        //ci dessous j'affichei le contenu de ma Hashmap:
        System.out.println("impression de ma hashmap listeRecupereMapMplus1 °°°°°°°°°°°°°°°°°°°°:");
        for (Map.Entry<Long, EmplMDTO> entry : listeRecupereMapMplus1.entrySet()) {
            //System.out.println(entry.getValue().getId());
            System.out.println(entry.getKey() + ":" + entry.getValue().toString());
            //listeRecupereMapM.forEach((key, value) -> System.out.println(key + ":" + value));
            //listeRecupereMapMMplus1Mplus2.put(entry.getKey(), entry.getValue().getId());
            //entry.getValue().getId();
            //System.out.println(entry.getKey()+" : "+entry.getValue());
        }





        //CONCERNANT M PLUS 2
        listeRecupereMplus2 =  userRepository.getAllIngeM2(nombreDeJourDuMoisPlus2Double, premierJourMoisMplus2localDateTime, dernierJourMoisMplus2localDateTime);
        System.out.println("impression de ma liste ListeRecupereMplus2 ^^^^^^^^^^^^^^^:");
        listeRecupereMplus2.forEach(System.out::println);
        //        ci dessous, je met le contenu de ma List listeRecupereM dans ma HashMap listeRecupereMapM;
//        for (EmplMDTO i : listeRecupereMplus2) {
//            listeRecupereMapMplus2.put(i.getId(),i);
//        }

//        ci dessous ca fonctionne mais je l'ai commenté car javais limrpession que ca contredisait ce que je connnaissais'
        for (EmplMDTO i : listeRecupereMplus2) listeRecupereMapMplus2.put(i.getId(),i);

        //ci dessous j'affichei le contenu de ma Hashmap:
        System.out.println("impression de ma hashmap listeRecupereMapMplus2 °°°°°°°°°°°°°°°°°°°°:");
        for (Map.Entry<Long, EmplMDTO> entry : listeRecupereMapMplus2.entrySet()) {
            //System.out.println(entry.getValue().getId());
            System.out.println(entry.getKey() + ":" + entry.getValue().toString());
            //listeRecupereMapM.forEach((key, value) -> System.out.println(key + ":" + value));
            //listeRecupereMapMMplus1Mplus2.put(entry.getKey(), entry.getValue().getId());
            //entry.getValue().getId();
            //System.out.println(entry.getKey()+" : "+entry.getValue());
        }






//
        Set<Long> keys = listeRecupereMapM.keySet();
        System.out.println("impression de mon Set" + keys.toString());
      //  keys.addAll(listeRecupereMapMplus1.keySet());
   //     System.out.println("impression de mon keys.addAll" + keys.toString());
        for (Long cle: keys)
        {
            System.out.println("salut");
            System.out.println(cle);
            //System.out.println("ççççççççççççççççççççççççççç"+ listeRecupereMapMplus1.get(cle));
            EmplMDTO EmplMDTO = listeRecupereMapM.get(cle);
            System.out.println(listeRecupereMapM.get(cle).toString());
            System.out.println(EmplMDTO.toString());


            EmplMDTO EmplMDTO1;
            if (listeRecupereMapMplus1.get(cle) != null)
            {
                EmplMDTO1 = listeRecupereMapMplus1.get(cle);
            }
            else{
                EmplMDTO1 = new EmplMDTO();
                EmplMDTO1.setId(cle);
                EmplMDTO1.setUserLogin(listeRecupereMapM.get(cle).getUserLogin());
                EmplMDTO1.setCount(nombreDeJourDuMoisPlus1Double);
            }

            System.out.println(" EmplMDTO1.toString() : " + EmplMDTO1.toString());



            EmplMDTO EmplMDTO2;
            if (listeRecupereMapMplus2.get(cle) != null)
            {
                EmplMDTO2 = listeRecupereMapMplus2.get(cle);
            }
            else{
                EmplMDTO2 = new EmplMDTO();
                EmplMDTO2.setId(cle);
                EmplMDTO2.setUserLogin(listeRecupereMapM.get(cle).getUserLogin());
                EmplMDTO2.setCount(nombreDeJourDuMoisPlus2Double);
            }

            System.out.println(" EmplMDTO2.toString() : " + EmplMDTO2.toString());



//            EmplMDTO EmplMDTO2 = listeRecupereMapMplus2.get(cle);
            listeRecupereMapMMplus1Mplus2.put(cle, new EmplMM1M2DTO(EmplMDTO, EmplMDTO1, EmplMDTO2));

        }

        System.out.println("impression de ma hashmap listeRecupereMapMMplus1Mplus2 °°°°°°°°°°°°°°°°°°°°:");
        for (Map.Entry<Long, EmplMM1M2DTO> entry : listeRecupereMapMMplus1Mplus2.entrySet()) {
            //System.out.println(entry.getValue().getId());
            //listeRecupereMapMMplus1Mplus2.put(entry.getKey(), entry.getValue().getId());
            //entry.getValue().getId();
            System.out.println(entry.getKey()+" : "+entry.getValue());
        }

//        for(EmplMDTO EmplMDTO: ListeRecupereM)
//        {
//            ListeRecupereMMplus1Mplus2.add(EmplMDTO);
//        }

        //listeRecupereMMplus1Mplus2 = listeRecupereM.stream().map(EmplMDTO::getId).collect(Collectors.toList());
        //Collections.copy(ListeRecupereMMplus1Mplus2, ListeRecupereM);
//        ListeRecupereMMplus1Mplus2 = ListeRecupereM.stream()
//            .map(EmplMDTO::getId)
//            .collect(Collectors.toCollection(ArrayList::new));
        //System.out.println("impression de ma liste ListeRecupereMMplus1Mplus2 ###################:");



        //listeRecupereMplus2 =  userRepository.getAllIngeM2(nombreDeJourDuMoisPlus2, premierJourMoisMplus2localDateTime, dernierJourMoisMplus2localDateTime);
        //System.out.println("impression de ma liste ListeRecupereMplus2 @@@@@@@@@@@@@@@@@@@@@@:");
        //ListeRecupereMplus2.forEach(System.out::println);
//        for (EmplMDTO i : listeRecupereMplus2) {
//            listeRecupereMapMplus2.put(i.getId(),i);
//        }

//        ci dessus , je convertis mon Hashmap en List
        List<EmplMM1M2DTO> hashmapTransformeEnList = new ArrayList<EmplMM1M2DTO>(listeRecupereMapMMplus1Mplus2.values());


        System.out.println("Arrays.toString(hashmapTransformeEnList.toArray())  ++++: " + Arrays.toString(hashmapTransformeEnList.toArray()));

        return hashmapTransformeEnList;


        //return userRepository.getAllIngeM(nombreDeJourRestantUntilFinDuMois);
       // System.out.println("a la sortie de la requete @@@@@@@@@@@@@@@@@@ : " + userRepository.getAllIngeM1(nombreDeJourRestantUntilFinDuMois, premierJourMois, dernierJourMois));
       // return userRepository.getAllIngeM1(nombreDeJourRestantUntilFinDuMois, premierJourMois, dernierJourMois);



//        recupererTousLes ingenieurs qui ne sont pas dans AttributionTache





    }



    @Transactional(readOnly = true)
    public Long getIdFromLogin(String login) {




        Long idIngeSansAffec;


        System.out.println(" valeur de login dans mon useservice" + login);
        idIngeSansAffec = userRepository.getIdFromLogin(login);
        System.out.println(" [[[[[[[[[[[[[ this.idIngeSansAffec : " + idIngeSansAffec);
        return idIngeSansAffec;
    }

//    @Transactional(readOnly = true)
//    public List<EmplMDTO> getAllIngeM1() {
//        return userRepository.getAllIngeM1();
//    }

//    @Transactional(readOnly = true)
//    public List<EmplMDTO> getAllIngeM2() {
//        return userRepository.getAllIngeM2();
//    }


}
