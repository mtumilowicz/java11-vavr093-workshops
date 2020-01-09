package workshops;

import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.match.annotation.Patterns;
import io.vavr.match.annotation.Unapply;
import person.*;

import java.time.LocalDate;

@Patterns
class DecompositionWorkshop {

    @Unapply
    // create Tuple2 of account, balance from an input person
    static Tuple2<Account, Address> PersonByCreditAssessSubjects(Person person) {
        return null; // Tuple.of(..., ...)
    }

    @Unapply
    // create Tuple3 of balance, salary, country from an input subject
    static Tuple3<Integer, Salary, String> CreditAssessSubjects(CreditAssessSubjects subjects) {
        return null; // Tuple.of(..., ..., ...)
    }

    @Unapply
    // create Tuple3 of year, month, day from an input date
    static Tuple3<Integer, Integer, Integer> LocalDate(LocalDate date) {
        return null; // Tuple.of(..., ..., ...)
    }
}
