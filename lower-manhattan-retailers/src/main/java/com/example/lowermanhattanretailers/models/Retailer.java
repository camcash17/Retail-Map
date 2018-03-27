package com.example.lowermanhattanretailers.models;

import lombok.*;

import javax.persistence.*;

@Data
@AllArgsConstructor @NoArgsConstructor @Getter @Setter
@Entity @Table(name = "RETAILERS")
public class Retailer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ORG_NAME")
    private String orgName;

    @Column(name = "PRIMARY_NAME")
    private String primaryName;

    @Column(name = "SECONDARY_NAME")
    private String secondaryName;

    @Column(name = "LAT")
    private String lat;

    @Column(name = "LON")
    private String lon;

    public Retailer(String orgName, String primaryName, String secondaryName, String lat, String lon) {
        this.orgName = orgName;
        this.primaryName = primaryName;
        this.secondaryName = secondaryName;
        this.lat = lat;
        this.lon = lon;
    }

}
