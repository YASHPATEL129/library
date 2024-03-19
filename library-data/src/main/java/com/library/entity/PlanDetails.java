package com.library.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "plan_details")
public class PlanDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String planName;

    private Long amount;

    private int months;

    private String planId;

    private int position;

    private String planTypes;
}
