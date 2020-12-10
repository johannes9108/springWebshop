package springWebshop.application.model.domain.order;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="Orderlines")
public class OrderLine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private long productId;

	private int itemQuantity;

	private double sum;
	
	private Double vatSum;

	private double vatPercentage;

	private double discount;
	
	private double sumPayable;
	
	private Currency currency;

	@Override
	public String toString() {
		return "\nOrderLine{" +
				"id=" + id +
				", productId=" + productId +
				", itemQuantity=" + itemQuantity +
				", sum=" + sum +
				", vatSum=" + vatSum +
				", vatPercentage=" + vatPercentage +
				", discount=" + discount +
				", sumPayable=" + sumPayable +
				", currency=" + currency +
				'}';
	}
}
