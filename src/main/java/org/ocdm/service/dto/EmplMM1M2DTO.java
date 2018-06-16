package org.ocdm.service.dto;

import javax.validation.constraints.Size;

/**
 * Created by sardehali on 12/02/18.
 */
//EmplMM1M2DTO : signifie  un objet qui contient l'identite d'un Employes avec le nombre de jour dispo dans le Mois en cours, le mois M+1 et le mois M+2
public class EmplMM1M2DTO {

    private Long id;

    private String userLogin;

    @Size(max = 50)
    private Float countM;

    @Size(max = 50)
    private Float countMplus1;

    @Size(max = 50)
    private Float countMplus2;

    public EmplMM1M2DTO() {

    }

    public EmplMM1M2DTO(EmplMDTO EmplMDTO, EmplMDTO EmplMDTOPlus1, EmplMDTO EmplMDTOPlus2) {
        this.id = EmplMDTO.getId();
        this.userLogin = EmplMDTO.getUserLogin();
        this.countM = EmplMDTO.getCount();
        this.countMplus1 = EmplMDTOPlus1.getCount();
        this.countMplus2 = EmplMDTOPlus2.getCount();
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

    public Float getCountM() {
        return countM;
    }

    public void setCountM(Float count) {
        this.countM = count;
    }


    public Float getCountMplus1() {
        return countMplus1;
    }

    public void setCountMplus1(Float countMplus1) {
        this.countMplus1 = countMplus1;
    }

    public Float getCountMplus2() {
        return countMplus2;
    }

    public void setCountMplus2(Float countMplus2) {
        this.countMplus2 = countMplus2;
    }

    @Override
    public String toString() {
        return "EmplMM1M2DTO{" +
            "id=" + id +
            ", userLogin='" + userLogin + '\'' +
            ", countM=" + countM +
            ", countMplus1=" + countMplus1 +
            ", countMplus2=" + countMplus2 +
            '}';
    }
}
