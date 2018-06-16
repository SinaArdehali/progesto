package org.ocdm.service.dto;

import javax.validation.constraints.Size;

/**
 * Created by sardehali on 01/02/18.
 */
public class ParticTacheDTO {

    private Long id;

    private String userLogin;

    @Size(max = 50)
    private Long count;


    //    volontairement, je n'ai pas mis countFloat dans le constructeur
    private Float countFloat;


    public ParticTacheDTO() {
        // Empty constructor needed for Jackson.
    }

//    public ParticTacheDTO(User user) {
//        this(user.getId(), user.getUser(), user.getFirstName(), user.getLastName(),
//            user.getEmail(), user.getActivated(), user.getImageUrl(), user.getLangKey(),
//            user.getCreatedBy(), user.getCreatedDate(), user.getLastModifiedBy(), user.getLastModifiedDate(),
//            user.getAuthorities().stream().map(Authority::getName)
//                .collect(Collectors.toSet()));
//    }


    public ParticTacheDTO(Long id, String userLogin, Long count) {
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

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }


    public Float getCountFloat() {
        return countFloat;
    }

    public void setCountFloat(Float countFloat) {
        this.countFloat = countFloat;
    }


    @Override
    public String toString() {
        return "ParticTacheDTO{" +
            "id=" + id +
            ", userLogin='" + userLogin + '\'' +
            ", count=" + count +
            '}';
    }
}
