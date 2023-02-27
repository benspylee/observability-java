package com.ramlin.observeme.controller;

/*import jakarta.persistence.Entity;
import jakarta.persistence.Id;*/
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;


@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Item {
    @Id
    private int id;
    private String name;

}
