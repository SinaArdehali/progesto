package org.ocdm.service.dto;

import javax.validation.constraints.Size;

/**
 * Created by sardehali on 01/02/18.
 */
public class TacheEtDonneesDTO {

    private Long id;

    private String tacheLogin;

    private float joursVendusTache;

    @Size(max = 50)
    private Long count;


    public TacheEtDonneesDTO() {
        // Empty constructor needed for Jackson.
    }


    public TacheEtDonneesDTO(Long id, String tacheLogin, float joursVendusTache, Long count) {
        this.id = id;
        this.tacheLogin = tacheLogin;
        this.joursVendusTache = joursVendusTache;
        this.count = count;
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

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "TacheEtDonneesDTO{" +
            "id=" + id +
            ", tacheLogin='" + tacheLogin + '\'' +
            ", joursVendusTache=" + joursVendusTache +
            ", count=" + count +
            '}';
    }
}
