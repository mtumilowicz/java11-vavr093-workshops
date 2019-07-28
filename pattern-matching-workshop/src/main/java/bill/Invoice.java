package bill;

import lombok.Value;

/**
 * Created by mtumilowicz on 2019-05-08.
 */
@Value(staticConstructor = "of")
public class Invoice {
    PaymentType paymentType;
}
