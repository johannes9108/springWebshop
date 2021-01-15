package springWebshop.application.model.domain.user;

import javax.persistence.Embeddable;

public enum ERole {
	CUSTOMER,
	ADMIN,
	CUSTOMER_MANAGER,
	ORDER_MANAGER,
	ACCOUNT_MANAGER,
	PRODUCT_MANAGER;
}
