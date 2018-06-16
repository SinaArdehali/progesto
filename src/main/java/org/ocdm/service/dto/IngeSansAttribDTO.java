package org.ocdm.service.dto;

import javax.validation.constraints.Size;

/**
 * Created by sardehali on 26/02/18.
 */
public class IngeSansAttribDTO {

    private Long id;

    private String login;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String userLogin) {
        this.login = login;
    }

    public IngeSansAttribDTO(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "IngeSansAttribDTO{" +
            "id=" + id +
            ", login='" + login + '\'' +
            '}';
    }
}
