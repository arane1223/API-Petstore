package models;

import data.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderModel {
    private long id;
    private long petId;
    private int quantity;
    private String shipDate;
    private OrderStatus status;
    private boolean complete;
}
