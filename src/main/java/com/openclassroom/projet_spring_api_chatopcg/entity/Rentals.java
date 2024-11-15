package com.openclassroom.projet_spring_api_chatopcg.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.logging.log4j.message.Message;

import java.util.List;

@Entity
@Table(name= "rentals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rentals {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  @Column(name ="name")
  private String name;

  @Column(name ="surface")
  private double surface;

  @Column(name ="price")
  private double price;

  @Column(name ="picture")
  private String picture;

  @Column(name ="description")
  private String description;

  @ManyToOne
  @JoinColumn(name = "owner_id", nullable = false)
  private User user;

}
