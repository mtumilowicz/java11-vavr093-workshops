package bill;

import lombok.Value;

@Value(staticConstructor = "of")
public class Invoice {
    PaymentType paymentType;
}
