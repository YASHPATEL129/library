package com.library.entity;

import com.library.enums.IsActiveType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "userPlan")
public class UserPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String userName;

    private Integer plan_id;

    private  Instant StartingDate;

    private Instant EndingDate;

    private Long my_order_id;

    @Enumerated(EnumType.STRING)
    private IsActiveType isActive;

}
