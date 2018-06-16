package org.ocdm.service.impl;

import org.ocdm.config.Constants;
import org.ocdm.domain.AttributionTache;
import org.ocdm.domain.Projet;
import org.ocdm.repository.AttributionTacheRepository;
import org.ocdm.repository.ProjetRepository;
import org.ocdm.repository.UserRepository;
import org.ocdm.service.TacheService;
import org.ocdm.domain.Tache;
import org.ocdm.repository.TacheRepository;
import org.ocdm.service.dto.EmplMDTO;
import org.ocdm.service.dto.EmplMM1M2DTO;
import org.ocdm.service.dto.TacheEtDonneesDTO;
import org.ocdm.service.dto.TacheEtDonneesDTOComplet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.Instant;
import java.time.LocalDate;
import java.util.*;

import static java.lang.Math.toIntExact;

/**
 * Service Implementation for managing Tache.
 */
@Service
@Transactional
public class TacheServiceImpl implements TacheService{

    List<AttributionTache> resultSelectLimit;
    int nombreDeLignesAffectees;
    Set<Long> setIdResultSelectLimit = new HashSet<Long>();
    int messageAlerteCarEssaieDeSupprimerDesAttribDejaAffecteesInt;
    float messageAlerteCarEssaieDeSupprimerDesAttribDejaAffecteesFloat;
    float messageAlerteCarEssaieDeSupprimerDesAttribDejaAffecteesFloatDivisePar4;

    private final Logger log = LoggerFactory.getLogger(TacheServiceImpl.class);
    private final ProjetRepository projetRepository;
    private final TacheRepository tacheRepository;
    private final AttributionTacheRepository                 attributionTacheRepository;
    private final UserRepository userRepository;

    List<AttributionTache> attributionTacheResult = new ArrayList<AttributionTache>();







    public TacheServiceImpl(TacheRepository tacheRepository, ProjetRepository projetRepository, AttributionTacheRepository attributionTacheRepository, UserRepository userRepository) {
        this.tacheRepository = tacheRepository;
        this.projetRepository = projetRepository;
        this.attributionTacheRepository = attributionTacheRepository;
        this.userRepository = userRepository;
    }

    /**
     * Save a tache.
     *
     * @param tache the entity to save
     * @return the persisted entity
     */
    @Override
    public Tache save(Tache tache) {
        log.debug("Request to save Tache : {}", tache);
//        AttributionTache attributionTache = new AttributionTache();
//        attributionTache.setTacheMere(tache);
        System.out.println("°°°°°°°tache.getId() : "+ tache.getProjetMere());


//        dans le bloc ci dessous, jessaie de voir si le projet existe ou pas.
        //        tacheRepository.methodeQuiFait : select count(*) from tache where tache_nomTache = tache.getNomTache and tache_projetMere= tache.getProjetMere
//            if le resultat de cet appel est superieur a 0, donc cette tache existe deja dans la base. dans ce cas, je ne fais qu'un save.
//        'masi il le resulta de cet appel est egal à 0, alors cette tache nexsite pas dans la base. dans ce cas, je fais tout mon bloc ci dessous, cest a dire *4 ....
//        le probleme de cette methode est que le nom de la tache ne pourra plus etre editable , car si on la modifie, x attributions_tache seront a nouveau cree.
        Long idDuProjetMere = tache.getProjetMere().getId();
        float resultDeExisteDeja = tacheRepository.existeDeja(tache.getNomTache(), idDuProjetMere);
        System.out.println("°°°°°°° resultDeExisteDeja : "+ resultDeExisteDeja);
        System.out.println(resultDeExisteDeja < 1);
        if (resultDeExisteDeja < 1){
            System.out.println("ce projet nexiste pas");
            float nbreEnregistrementAttribution = tache.getJoursVendusTache()* Constants.CONVERSIONJOURQUARTJOURNEEFLOAT;
            // Prevoir changement de cette methode -> mettre en place des trigger coté BDD pour automoatiser cette taches et diminuer les allée retour BDD
            for (int i=0; i<nbreEnregistrementAttribution; i++){
                AttributionTache attributionTache = new AttributionTache();
                attributionTache.setTacheMere(tache);
                //appel a la base de donnée pour enregistrmeent
                System.out.println("****************creation de mon attribution tache************");
                attributionTacheRepository.save(attributionTache);
            }

        }
//



//         JE N'AI PLUS BESOIN DE CE BOUT DE CODE CAR A PRESENT LA MAJ DU PROJET EN MEME TEMPS QUE LA MAJ DE LA TACHE'
        Tache savedTache = tacheRepository.save(tache);
        majJoursVendProj(savedTache);
        return savedTache;


 //       return tacheRepository.save(tache);
    }



    /**
     *  Get all the taches.
     *
     *  @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<Tache> findAll() {
        log.debug("Request to get all Taches");
        return tacheRepository.findAll();
    }

//    /**
//     *  Get all the taches by project.
//     *
//     *  @return the list of taches
//     */
//    @Override
//    public List<Tache> findAllByProjet(Projet projet) {
//        log.debug("Request to get all Taches by project");
//        List<Tache> allTasksByProjet = tacheRepository.findAllByProjet(projet);
//        return allTasksByProjet;
//    }



    /**
     *  Get one tache by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Tache findOne(Long id) {
        System.out.println("on est ici dans findOne de TacheServiceImpl : " + id);
        log.debug("Request to get Tache : {}", id);
        //System.out.println(" on est dans findOne et tacheserviceImple" + tacheRepository.findOne(id));
        return tacheRepository.findOne(id);
    }

    /**
     *  Delete the  tache by id.
     *
     *  @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Tache : {}", id);
        tacheRepository.delete(id);
    }






    //cree par moiSina
    /**
     * maj a projet.
     *
     * @param tache the entity to save
     * @return the persisted entity
     */
    @Override
    public int majJoursVendProj (Tache tache) {

    int afficher;
        log.debug("Request to save Tache : {}", tache);
//        AttributionTache attributionTache = new AttributionTache();
//        attributionTache.setTacheMere(tache);
        Projet projetMere =tache.getProjetMere();

        //System.out.println("-----------------------------------------dans repository--------------------------------------------");
        //System.out.println(projetMere);


        afficher = projetRepository.majJoursVendProj(projetMere.getId());
        System.out.println(afficher);
        return afficher;
    }
//
////fin cree par moiSina

//    // JE N'AI PLUS BESOIN DE CE BOUT DE CODE CAR LE BLOC CI DESSOUS CORRESPOND A LA LIGNE TOTAL QUE J'AI INSERE ET QUI SE DEPLOYAIT QUAND ON CLIQUAIT SUR "VOIR LES TACHES DU PROJET"
//    /**
//     *  Get the "id" projet.
//     *
//     *  @param id the id of the projet to retrieve sum of.
//     *  @return the entity
//     */
//    @Override
//    @Transactional(readOnly = true)
//    public float trouverSumJoursVendus(Long id) {
//        log.debug("Request to get Tache : {}", id);
//        return tacheRepository.trouverSumJoursVendus(id);
//    }


//    /**
//     * maj a projet.
//     *
//     * @param nbJour the entity to save
//     * @return the persisted entity
//     */
//    @Override
//    public void modifierJoursVendus (int nbJour) {
//
//        log.debug("Request to save Tache : {}", nbJour);
//        return attributionTacheRepository.modifierJoursVendus(nbJour);
//    }





//    °°°°°°°°°°°°°°°°°°°°°°°°°°°°°

//en balancatnl'objet tache en entier'
//    /**
//     * Save a attributionTache.
//     *
//     * @param tacheMere the entity to save
//     * @param nbJour the entity to save
//     * @return the persisted entity
//     */
//    @Override
//    public int modifierJoursVendus(long nbJour, Tache tacheMere) {
//        log.debug("Request to save AttributionTache : {}", tacheMere);
//
//        int nbJourLoop = toIntExact(nbJour*4);
//
//
////        for (int i = 0; i<nbJourLoop; i++){
////
////
////            //AttributionTache resultDuReturn;
////            AttributionTache attributionTacheTemp = new AttributionTache(tacheMere);
////            attributionTacheResult.add(attributionTacheRepository.save(attributionTacheTemp));
////            //resultDuReturn = attributionTacheRepository.save(attributionTacheTemp);
////        }
////        return attributionTacheResult;
//
//        return 1;
//    }
    /**
     //     * Save a attributionTache.
     //     *
     //     * @param tacheMere the entity to save
     //     * @param nbJour the entity to save
     //     * @return the persisted entity
     //     */
    @Override
    public List<AttributionTache> modifierJoursVendus(long nbJour, long idTacheMere) {
        log.debug("Request to save AttributionTache : {}", idTacheMere);

        System.out.println("############ : " + nbJour + idTacheMere);
        int nbJourLoop = toIntExact(nbJour*4);


        for (int i = 0; i<nbJourLoop; i++){
            //AttributionTache resultDuReturn;
            Tache tacheMereObjet = tacheRepository.findOne(idTacheMere);
            AttributionTache attributionTacheTemp = new AttributionTache(tacheMereObjet);
            attributionTacheResult.add(attributionTacheRepository.save(attributionTacheTemp));
            //resultDuReturn = attributionTacheRepository.save(attributionTacheTemp);
        }
        return attributionTacheResult;

        //return 1;
    }



    //cree par moiSina
    /**
     //     *  get the count of joursattribues from an "id" user.
     //     *
     //     *  @param idInge the id of the inge to retrieve sum of.
     //     *  @return the entity
     //     */
    @Override
    @Transactional(readOnly = false)
    public float selectPuisRetirerJoursVendus(Double nbJour, Long idTacheMere) {

        List<AttributionTache> resultSelectLimit;
        int nombreDeLignesAffectees;
        Set<Long> setIdResultSelectLimit = new HashSet<Long>();
        int messageAlerteCarEssaieDeSupprimerDesAttribDejaAffecteesInt;
        float messageAlerteCarEssaieDeSupprimerDesAttribDejaAffecteesFloat;
        float messageAlerteCarEssaieDeSupprimerDesAttribDejaAffecteesFloatDivisePar4;






        System.out.println("-----------------------------------------dans le TacheServiceImpl-------------------------------------------- nbJour" + nbJour + " " + idTacheMere);
        log.debug("Request to get the sum of joursattribues from an id user. : {}", nbJour, idTacheMere);

        Double nbQuartJourneeFloat = nbJour * Constants.CONVERSIONJOURQUARTJOURNEEFLOAT;
        int nbQuartJourneeInt = nbQuartJourneeFloat.intValue();
        System.out.println("-----------------------------------------dans le TacheServiceImpl-------------------------------------------- nbJour" + nbQuartJourneeInt + " " + idTacheMere);

        resultSelectLimit =  attributionTacheRepository.getAttrTacheLimit(idTacheMere, new PageRequest(0,nbQuartJourneeInt));




        resultSelectLimit.forEach( item->{
            setIdResultSelectLimit.add(item.getId());
        });

        System.out.println("setIdResultSelectLimit :  ********" + setIdResultSelectLimit.size());
        System.out.println(setIdResultSelectLimit);
        messageAlerteCarEssaieDeSupprimerDesAttribDejaAffecteesInt = setIdResultSelectLimit.size();
        messageAlerteCarEssaieDeSupprimerDesAttribDejaAffecteesFloat = (float) messageAlerteCarEssaieDeSupprimerDesAttribDejaAffecteesInt;
        messageAlerteCarEssaieDeSupprimerDesAttribDejaAffecteesFloatDivisePar4 = messageAlerteCarEssaieDeSupprimerDesAttribDejaAffecteesFloat / Constants.CONVERSIONJOURQUARTJOURNEEFLOAT;
        System.out.println("setIdResultSelectLimit :  ******** messageAlerteCarEssaieDeSupprimerDesAttribDejaAffecteesFloat : " + messageAlerteCarEssaieDeSupprimerDesAttribDejaAffecteesFloatDivisePar4);
//        if (nbQuartJourneeInt > setIdResultSelectLimit.size())
//        {
//            this.messageAlerteCarEssaieDeSupprimerDesAttribDejaAffectees = true;
//        }


        Iterator<Long> iterator = setIdResultSelectLimit.iterator();
        while(iterator.hasNext()) {
            Long setElement = iterator.next();
            attributionTacheRepository.delete(setElement);
        }

        System.out.println("ce que je recupere dans le retirn . je sui sdans tacheServiceImpl au moment du retour avec le this " + this.messageAlerteCarEssaieDeSupprimerDesAttribDejaAffecteesFloatDivisePar4);
        System.out.println("ce que je recupere dans le retirn . je sui sdans tacheServiceImpl au moment du retour sans le this " + messageAlerteCarEssaieDeSupprimerDesAttribDejaAffecteesFloatDivisePar4);

        return messageAlerteCarEssaieDeSupprimerDesAttribDejaAffecteesFloatDivisePar4;

        //return attributionTacheRepository.getAttrTacheLimit(idTache, new PageRequest(0,2));
    }













    /**
     *  Get all the taches by project id.
     *
     *  @return the list of taches
     */
    @Override
    public List<Tache> getAllByProjId(Long id) {
        log.debug("Request to get all Taches by project id");
        List<Tache> allTasksByProjetId = tacheRepository.getAllByProjId(id);
//        System.out.println("|-|-|-|-|-|-| voici le resultat de mon userRepository.getJoursAttrByTache(2251l) : " + tacheRepository.getJoursAttrByTache(2251l));
        return allTasksByProjetId;
    }





    /**
     *  Get all the taches by project id.
     *
     *  @return the list of taches
     */
    @Override
    public List<Tache> getTachesCurrentUser(String login) {
        log.debug("Request to get all Taches by current user id");
        List<Tache> tachesCurrentUser = attributionTacheRepository.getTachesCurrentUser(login);
        System.out.println("|-|-|-|-|-|-| voici le resultat de mon attributionTacheRepository.getTachesCurrentUser(login) : " + attributionTacheRepository.getTachesCurrentUser(login));
        return tachesCurrentUser;
    }








    /**
     *  Get all the taches by project id.
     *
     *  @return the list of taches
     */
    @Override
    public List<TacheEtDonneesDTOComplet> getAllByProjIdBIS(Long idProjet) {


        List<TacheEtDonneesDTO> listeJoursAttribuesParTache= new ArrayList<TacheEtDonneesDTO>();
        List<TacheEtDonneesDTO> listeJoursNONAttribuesParTache= new ArrayList<TacheEtDonneesDTO>();
        List<TacheEtDonneesDTOComplet> listeJoursAttribuesNONAttribuesParTache= new ArrayList<TacheEtDonneesDTOComplet>();


        Map<Long, TacheEtDonneesDTO> listeJoursAttribuesParTacheMap = new HashMap<Long, TacheEtDonneesDTO>();
        Map<Long, TacheEtDonneesDTO> listeJoursNONAttribuesParTacheMap = new HashMap<Long, TacheEtDonneesDTO>();
        Map<Long, TacheEtDonneesDTOComplet> listeJoursAttribuesNONAttribuesParTacheMap= new HashMap<Long, TacheEtDonneesDTOComplet>();





        log.debug("Request to get all Taches by project id");
        List<TacheEtDonneesDTO> allTasksByProjetId = tacheRepository.getAllByProjIdBIS(idProjet);
        System.out.println("|-|-|-|-|-|-| voici le resultat de mon userRepository.getJoursAttrByTache(@@@@) nouvelle dans tacheServiceImpl: " + tacheRepository.getAllByProjIdBIS(idProjet));

        Map<Long, TacheEtDonneesDTO> allTasksByProjetIdMap = new HashMap<Long, TacheEtDonneesDTO>();
        //transformer la liste en map de type TacheEtDonneesDTO
        for (TacheEtDonneesDTO i : allTasksByProjetId) allTasksByProjetIdMap.put(i.getId(),i);

        //impresssion de ma map allTasksByProjetIdMap
        System.out.println("impression de ma hashmap allTasksByProjetIdMap °°°°°°°°°°°°°°°°°°°°[|:");
        for (Map.Entry<Long, TacheEtDonneesDTO> entry : allTasksByProjetIdMap.entrySet()) {
            System.out.println(entry.getKey() + ":" + entry.getValue().toString());
        }



        //je vais maintenat generer ùma liste dattribues:
        //CONCERNANT JOURS ATTRIBUES
//        voici la version propre de ce' que je devrai envoyer'
        listeJoursAttribuesParTache =  tacheRepository.getJoursAttrByTache(idProjet);

        //System.out.println(EmplMDTO.toString());
        System.out.println("impression de ma liste listeJoursAttribuesParTache @@@@@@@@@@@@@@@@@@@@@@ nouvelle methode :");
        listeJoursAttribuesParTache.forEach(System.out::println);



//        ci dessous ca fonctionne mais je l'ai commenté car javais limrpession que ca contredisait ce que je connnaissais'
        for (TacheEtDonneesDTO i : listeJoursAttribuesParTache) listeJoursAttribuesParTacheMap.put(i.getId(),i);


        //ci dessous j'affichei le contenu de ma Hashmap:
        System.out.println("impression de ma hashmap listeJoursAttribuesParTacheMap °°°°°°°°°°°°°°°°°°°°:");
        for (Map.Entry<Long, TacheEtDonneesDTO> entry : listeJoursAttribuesParTacheMap.entrySet()) {
            //System.out.println(entry.getValue().getId());
            System.out.println(entry.getKey() + ":" + entry.getValue().toString());
        }



        //je vais maintenat generer ùma liste de NON attribues:
        //CONCERNANT JOURS NON ATTRIBUES
        listeJoursNONAttribuesParTache =  tacheRepository.getJoursNonAttrByTache(idProjet);
        System.out.println("impression de ma liste listeJoursNONAttribuesParTache toString ^^^^^^||^^^^^^^:" + listeJoursNONAttribuesParTache.toString());
        System.out.println("impression de ma liste listeJoursNONAttribuesParTache ^^^^^^^^^^^^^^^:");
        listeJoursNONAttribuesParTache.forEach(System.out::println);

//        ci dessous ca fonctionne mais je l'ai commenté car javais limrpession que ca contredisait ce que je connnaissais'
        for (TacheEtDonneesDTO i : listeJoursNONAttribuesParTache) listeJoursNONAttribuesParTacheMap.put(i.getId(),i);

        //ci dessous j'affichei le contenu de ma Hashmap:
        System.out.println("impression de ma hashmap listeJoursNONAttribuesParTacheMap °°°°°°°°°°°°°°°°°°°°:");
        for (Map.Entry<Long, TacheEtDonneesDTO> entry : listeJoursNONAttribuesParTacheMap.entrySet()) {

            System.out.println(entry.getKey() + ":" + entry.getValue().toString());

        }


        //pour les elemensts attribues:
        Set<Long> keys = allTasksByProjetIdMap.keySet();
        System.out.println("impression de mon Set" + keys.toString());

        for (Long cle: keys)
        {
            System.out.println(cle);


            TacheEtDonneesDTO tacheEtDonneesDTO= allTasksByProjetIdMap.get(cle);


            TacheEtDonneesDTO tacheEtDonneesDTO1 = new TacheEtDonneesDTO();

            if(listeJoursAttribuesParTacheMap.get(cle) != null )
            {
                tacheEtDonneesDTO1 = listeJoursAttribuesParTacheMap.get(cle);
            }
            else{
                //on ne fait rien et donc on envoie un objet vide.
            }
System.out.println("contenu de tachesEtDonnéeesDTO1" + tacheEtDonneesDTO1.toString());

            TacheEtDonneesDTO tacheEtDonneesDTO2 = new TacheEtDonneesDTO();

            if(listeJoursNONAttribuesParTacheMap.get(cle) != null )
            {
                tacheEtDonneesDTO2 = listeJoursNONAttribuesParTacheMap.get(cle);
            }
            else{
                //on ne fait rien et donc on envoie un objet vide.
            }

            System.out.println("contenu de tacheEtDonneesDTO2" + tacheEtDonneesDTO2.toString());


            listeJoursAttribuesNONAttribuesParTacheMap.put(cle, new TacheEtDonneesDTOComplet(tacheEtDonneesDTO, tacheEtDonneesDTO1, tacheEtDonneesDTO2));
            System.out.println("contenu de listeJoursAttribuesNONAttribuesParTacheMap" + listeJoursAttribuesNONAttribuesParTacheMap.toString());
        }


//        le bloc ci dessous a ete fait pour convertir mes quarts de jours en jours
        for (Map.Entry<Long, TacheEtDonneesDTOComplet> entry : listeJoursAttribuesNONAttribuesParTacheMap.entrySet()) {

            if (entry.getValue().getCountAttribue()== null){
                entry.getValue().setCountAttribue(0l);
                entry.getValue().setCountAttribueFloat(0f);
            }

            if (entry.getValue().getCountResteAttribuer()== null){
                entry.getValue().setCountResteAttribuer(0l);
                entry.getValue().setCountResteAttribuerFloat(0f);
            }


            Long valeursActuellesAttribue = entry.getValue().getCountAttribue();
            Float valeursActuellesAttribueFloat = valeursActuellesAttribue.floatValue();
            Float valeursActuellesAttribueFloatDivisePar4 = valeursActuellesAttribueFloat/Constants.CONVERSIONJOURQUARTJOURNEEFLOAT;
            entry.getValue().setCountAttribueFloat(valeursActuellesAttribueFloatDivisePar4);



            Long valeursActuellesResteAttribuer = entry.getValue().getCountResteAttribuer();
            Float valeursActuellesResteAttribuerFloat = valeursActuellesResteAttribuer.floatValue();
            Float valeursActuellesResteAttribuerFloatDivisePar4 = valeursActuellesResteAttribuerFloat/Constants.CONVERSIONJOURQUARTJOURNEEFLOAT;
            entry.getValue().setCountResteAttribuerFloat(valeursActuellesResteAttribuerFloatDivisePar4);

            System.out.println("{{{{{{{{{{{{{{{ : " + entry.getKey()+" : "+entry.getValue());
        }


        //ci dessus , je convertis mon Hashmap en List
        List<TacheEtDonneesDTOComplet> hashmapTransformeEnList = new ArrayList<TacheEtDonneesDTOComplet>(listeJoursAttribuesNONAttribuesParTacheMap.values());
        System.out.println("contenu de hashmapTransformeEnList" + hashmapTransformeEnList.toString());

        return hashmapTransformeEnList;

    }





}
