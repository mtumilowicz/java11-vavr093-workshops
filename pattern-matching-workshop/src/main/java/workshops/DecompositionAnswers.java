package workshops;

import io.vavr.Tuple;
import io.vavr.Tuple2;
import io.vavr.Tuple3;
import io.vavr.match.annotation.Patterns;
import io.vavr.match.annotation.Unapply;
import person.*;

import java.time.LocalDate;

@Patterns
class DecompositionAnswers {
    @Unapply
    static Tuple2<Account, Address> PersonByCreditAssessSubjects(Person person) {
        return Tuple.of(person.getAccount(), person.getAddress());
    }

    @Unapply
    static Tuple3<Integer, Salary, String> CreditAssessSubjects(CreditAssessSubjects subjects) {
        return Tuple.of(subjects.getBalance(), subjects.getSalary(), subjects.getCountry());
    }

    @Unapply
    static Tuple3<Integer, Integer, Integer> LocalDate(LocalDate date) {
        return Tuple.of(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
    }
}
