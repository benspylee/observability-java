package com.ramlin.observeme.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Item {
    private String itemId;
    private String itemName;
    private int itemQuantity;
    private double itemPrice;
}
