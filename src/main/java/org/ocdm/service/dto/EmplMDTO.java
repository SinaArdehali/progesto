package org.ocdm.service.dto;

import javax.validation.constraints.Size;

/**
 * Created by sardehali on 01/02/18.
 */
//EmplMDTO : signifie  un objet qui contient l'identite d'un Employes avec le nombre de jour dispo dans le Mois en cours
public class EmplMDTO {

    private Long id;

    private String userLogin;

    @Size(max = 50)
    private Float count;


    public EmplMDTO() {
        // Empty constructor needed for Jackson.
    }

    public EmplMDTO(Long id, String userLogin, Float count) {
        this.id = id;
        this.userLogin = userLogin;
        this.count = count;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    public Float getCount() {
        return count;
    }

    public void setCount(Float count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "EmplMDTO{" +
            "id=" + id +
            ", userLogin='" + userLogin + '\'' +
            ", count=" + count +
            '}';
    }
}
