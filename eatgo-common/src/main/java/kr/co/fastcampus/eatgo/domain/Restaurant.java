package kr.co.fastcampus.eatgo.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Restaurant {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private Long categoryId;

    @NotEmpty
    private String name;

    @NotEmpty
    private String address;

    @Transient
    @Builder.Default
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<MenuItem> menuItems = new ArrayList<MenuItem>();

    @Transient
    @Builder.Default
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Review> reviews = new ArrayList<Review>();


    public String getInformation() {
        return name + " in " + address;
    }

    public void updateInformation(String name, String address) {
        this.address = address;
        this.name = name;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = new ArrayList<>(menuItems);
    }


    public void setReviews(List<Review> reviews) {
        this.reviews = new ArrayList<>(reviews);
    }
}
