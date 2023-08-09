package shop.ecoswapshop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class Address {

    private final String city;

    private final String street;

    private final String zipcode;

    protected Address() {
        this.city = null;
        this.street = null;
        this.zipcode = null;
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
