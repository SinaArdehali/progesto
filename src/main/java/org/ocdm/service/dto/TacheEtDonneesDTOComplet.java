package org.ocdm.service.dto;

import javax.validation.constraints.Size;

/**
 * Created by sardehali on 01/02/18.
 */
public class TacheEtDonneesDTOComplet {

    private Long id;

    private String tacheLogin;

    private float joursVendusTache;

    @Size(max = 50)
    private Long countAttribue;

    @Size(max = 50)
    private Float countAttribueFloat;


    @Size(max = 50)
    private Long countResteAttribuer;

    @Size(max = 50)
    private Float countResteAttribuerFloat;


    public TacheEtDonneesDTOComplet() {
        // Empty constructor needed for Jackson.
    }

    public TacheEtDonneesDTOComplet(TacheEtDonneesDTO tacheEtDonneesDTO, TacheEtDonneesDTO tacheEtDonneesDTO1, TacheEtDonneesDTO tacheEtDonneesDTO2) {
        this.id = tacheEtDonneesDTO.getId();
        this.tacheLogin = tacheEtDonneesDTO.getTacheLogin();
        this.joursVendusTache = tacheEtDonneesDTO.getJoursVendusTache();
        this.countAttribue = tacheEtDonneesDTO1.getCount();
        this.countResteAttribuer = tacheEtDonneesDTO2.getCount();
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTacheLogin() {
        return tacheLogin;
    }

    public void setTacheLogin(String tacheLogin) {
        this.tacheLogin = tacheLogin;
    }

    public float getJoursVendusTache() {
        return joursVendusTache;
    }

    public void setJoursVendusTache(float joursVendusTache) {
        this.joursVendusTache = joursVendusTache;
    }

    public Long getCountAttribue() {
        return countAttribue;
    }

    public void setCountAttribue(Long countAttribue) {
        this.countAttribue = countAttribue;
    }

    public Long getCountResteAttribuer() {
        return countResteAttribuer;
    }

    public void setCountResteAttribuer(Long countResteAttribuer) {
        this.countResteAttribuer = countResteAttribuer;
    }

    public Float getCountAttribueFloat() {
        return countAttribueFloat;
    }

    public void setCountAttribueFloat(Float countAttribueFloat) {
        this.countAttribueFloat = countAttribueFloat;
    }

    public Float getCountResteAttribuerFloat() {
        return countResteAttribuerFloat;
    }

    public void setCountResteAttribuerFloat(Float countResteAttribuerFloat) {
        this.countResteAttribuerFloat = countResteAttribuerFloat;
    }

    @Override
    public String toString() {
        return "TacheEtDonneesDTOComplet{" +
            "id=" + id +
            ", tacheLogin='" + tacheLogin + '\'' +
            ", joursVendusTache=" + joursVendusTache +
            ", countAttribue=" + countAttribue +
            ", countAttribueFloat=" + countAttribueFloat +
            ", countResteAttribuer=" + countResteAttribuer +
            ", countResteAttribuerFloat=" + countResteAttribuerFloat +
            '}';
    }
}
