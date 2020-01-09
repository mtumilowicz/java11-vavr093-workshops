import bill.Invoice
import bill.PaymentType
import io.vavr.control.Either
import io.vavr.control.Option
import out.Display
import person.*
import person.request.PersonRequest
import person.request.ValidPersonRequest
import spock.lang.Specification
import workshops.Answers

import java.time.LocalDate
import java.time.format.DateTimeParseException

class AnswersTests extends Specification {
    def "numberConverter"() {
        expect:
        Answers.numberConverter('one') == 1
        Answers.numberConverter('two') == 2
        Answers.numberConverter('three') == 3
    }

    def "numberConverter guard"() {
        when:
        Answers.numberConverter(null)

        then:
        thrown(IllegalStateException)

        when:
        Answers.numberConverter('four')

        then:
        thrown(IllegalStateException)
    }

    def "thresholds"() {
        expect:
        Answers.thresholds(0) == 'threshold1'
        Answers.thresholds(25) == 'threshold1'
        Answers.thresholds(50) == 'threshold1'
        and:
        Answers.thresholds(51) == 'threshold2'
        Answers.thresholds(100) == 'threshold2'
        Answers.thresholds(150) == 'threshold2'
        and:
        Answers.thresholds(200) == 'threshold3'
        Answers.thresholds(250) == 'threshold3'
        Answers.thresholds(1_000_000) == 'threshold3'
    }

    def "thresholds guard"() {
        when:
        Answers.thresholds(-1)

        then:
        thrown(IllegalArgumentException)
    }

    def "switchOnEnum"() {
        expect:
        Answers.switchOnEnum(Person.builder().type(PersonType.VIP).build()) == 'full stats'
        Answers.switchOnEnum(Person.builder().type(PersonType.REGULAR).build()) == 'just stats'
        Answers.switchOnEnum(Person.builder().type(PersonType.TEMPORARY).build()) == 'fast stats'
    }

    def "switchOnEnum guard"() {
        when:
        Answers.switchOnEnum(Person.builder().build())

        then:
        thrown(IllegalStateException)
    }

    def "rawDateMapper"() {
        expect:
        Answers.rawDateMapper('2014-10-10') == LocalDate.of(2014, 10, 10)
        !Answers.rawDateMapper(null)
    }

    def "rawDateMapper exceptional"() {
        when:
        Answers.rawDateMapper('wrong')

        then:
        thrown(DateTimeParseException)
    }

    def "optionDateMapper"() {
        expect:
        Answers.optionDateMapper('2014-10-10') == Option.some(LocalDate.of(2014, 10, 10))
        Answers.optionDateMapper(null) == Option.none()
    }

    def "optionDateMapper exceptional"() {
        when:
        Answers.optionDateMapper('wrong')

        then:
        thrown(DateTimeParseException)
    }

    def "eitherDecompose - successful patch when salary < 0 - active = false, salary = 0"() {
        given:
        def canBeFixed = PersonRequest.builder()
                .type(PersonType.VIP)
                .balance(3000)
                .country('Poland')
                .city('Warsaw')
                .active(true)
                .salary(-1000)
                .build()
        when:
        def fixed = Answers.eitherDecompose(Either.left(canBeFixed))

        then:
        fixed == Either.right(Person.builder()
                .type(PersonType.VIP)
                .active(false)
                .account(Account.builder()
                        .salary(Salary.of(0))
                        .balance(3000)
                        .build())
                .address(Address.builder()
                        .city('Warsaw')
                        .country('Poland')
                        .build())
                .build())
    }

    def "eitherDecompose - argument cannot be null"() {
        expect:
        Answers.eitherDecompose(null) == Either.left('cannot be null')
    }

    def "eitherDecompose - failure patch - to many errors (empty request)"() {
        given:
        def cannotBeFixed = PersonRequest.builder().build()

        when:
        def notFixed = Answers.eitherDecompose(Either.left(cannotBeFixed))

        then:
        notFixed == Either.left('cannot be fixed, too many errors')
    }

    def "eitherDecompose - successful assemble - all business rules are met"() {
        given:
        def allBusinessRulesAreMet = ValidPersonRequest.builder()
                .type(PersonType.VIP)
                .active(true)
                .salary(Salary.of(0))
                .balance(3000)
                .city('Warsaw')
                .country('Poland')
                .build()

        when:
        def correct = Answers.eitherDecompose(Either.right(allBusinessRulesAreMet))

        then:
        correct == Either.right(Person.builder()
                .type(PersonType.VIP)
                .active(true)
                .account(Account.builder()
                        .salary(Salary.of(0))
                        .balance(3000)
                        .build())
                .address(Address.builder()
                        .city('Warsaw')
                        .country('Poland')
                        .build())
                .build())
    }

    def "eitherDecompose - failure assemble - not all business rules are met: VIP should be active"() {
        given:
        def notAllBusinessRulesAreMet = ValidPersonRequest.builder()
                .type(PersonType.VIP)
                .active(false)
                .salary(Salary.of(0))
                .balance(3000)
                .city('Warsaw')
                .country('Poland')
                .build()

        when:
        def incorrect = Answers.eitherDecompose(Either.right(notAllBusinessRulesAreMet))

        then:
        incorrect == Either.left('not all business rules are matched')
    }

    def "optionDecompose, cannot find in database"() {
        given:
        def display = new Display()
        def notExistsId = 2

        when:
        Answers.optionDecompose(notExistsId, display)

        then:
        display.message == "cannot find person with id = ${notExistsId}"
    }

    def "optionDecompose, found in database"() {
        given:
        def display = new Display()
        def existsId = 1

        when:
        Answers.optionDecompose(existsId, display)

        then:
        display.message == "person: ${existsId} processed"
    }

    def "tryDecompose, successfully parsed and squared"() {
        given:
        def display = new Display()

        when:
        Answers.tryDecompose('3', display)

        then:
        display.message == 'squared number is: 9'
    }

    def "tryDecompose, fail to parse an input string as a number"() {
        given:
        def display = new Display()

        when:
        Answers.tryDecompose('wrong', display)

        then:
        display.message == 'cannot square number: For input string: "wrong"'
    }

    def "ifSyntax"() {
        given:
        def activePerson = Person.builder().active(true).build()
        def inactivePerson = Person.builder().active(false).build()

        expect:
        Answers.ifSyntax(null) == 'cannot be null'
        Answers.ifSyntax(inactivePerson) == 'activated'
        Answers.ifSyntax(activePerson) == 'deactivated'
    }

    def "getTaxRateFor"() {
        given:
        def _2005_10_10 = LocalDate.of(2005, 10, 10)
        def _2010_10_10 = LocalDate.of(2010, 10, 10)
        def _2011_10_10 = LocalDate.of(2011, 10, 10)
        def _2015_10_10 = LocalDate.of(2015, 10, 10)
        def _2017_10_10 = LocalDate.of(2017, 10, 10)
        def _2020_01_01 = LocalDate.of(2020, 1, 1)

        expect:
        Answers.getTaxRateFor(_2005_10_10) == 1
        Answers.getTaxRateFor(_2010_10_10) == 1
        Answers.getTaxRateFor(_2011_10_10) == 2
        Answers.getTaxRateFor(_2015_10_10) == 3
        Answers.getTaxRateFor(_2017_10_10) == 3
        Answers.getTaxRateFor(_2020_01_01) == 3
    }

    def "decomposePerson"() {
        given:
        def _800 = Salary.of(800)
        def _2000 = Salary.of(2000)
        def _950 = Salary.of(950)
        and:
        def p1 = Person.builder()
                .account(Account.builder().salary(_800).balance(20_000).build())
                .address(Address.builder().country('POLAND').build())
                .build()
        def p2 = Person.builder()
                .account(Account.builder().salary(_2000).balance(1000).build())
                .address(Address.builder().country('USA').build())
                .build()
        def p3 = Person.builder().account(Account.builder().salary(_950).balance(15_000).build())
                .address(Address.builder().country('POLAND').build())
                .build()

        expect:
        Answers.personDecompose(p1) == 508
        Answers.personDecompose(p2) == 328
        Answers.personDecompose(p3) == 508
    }

    def "isInTest"() {
        expect:
        Answers.isInTest(Invoice.of(PaymentType.APPLE_PAY)) == 'mobile'
        Answers.isInTest(Invoice.of(PaymentType.BLIK)) == 'mobile'
        Answers.isInTest(Invoice.of(PaymentType.CASH)) == 'cash'
        Answers.isInTest(Invoice.of(PaymentType.CREDIT_CARD)) == 'card'
        Answers.isInTest(Invoice.of(PaymentType.GIFT_CARD)) == 'card'
        Answers.isInTest(Invoice.of(PaymentType.PAYPAL)) == 'online'
        Answers.isInTest(Invoice.of()) == 'not paid yet'
    }

    def "allOfTest"() {
        given:
        def vipActive = Person.builder().type(PersonType.VIP).active(true).build()
        def vipNotActive = Person.builder().type(PersonType.VIP).active(false).build()
        def regularActive = Person.builder().type(PersonType.REGULAR).active(true).build()
        def regularNotActive = Person.builder().type(PersonType.REGULAR).active(false).build()
        def temporaryActive = Person.builder().type(PersonType.TEMPORARY).active(true).build()
        def temporaryNotActive = Person.builder().type(PersonType.TEMPORARY).active(false).build()

        expect:
        Answers.allOfTest(vipActive) == 'vip + active'
        Answers.allOfTest(vipNotActive) == 'vip + not active'
        Answers.allOfTest(regularActive) == 'regular + active'
        Answers.allOfTest(regularNotActive) == 'regular + not active'
        Answers.allOfTest(temporaryActive) == 'temporary + active'
        Answers.allOfTest(temporaryNotActive) == 'temporary + not active'
    }

    def "instanceOfTest"() {
        given:
        def noExceptionThrower = {}
        def illegalArgumentExceptionThrower = { throw new IllegalArgumentException() }
        def runtimeExceptionThrower = { throw new RuntimeException() }
        def iOExceptionThrower = { throw new IOException() }
        def classNotFoundExceptionThrower = { throw new ClassNotFoundException() }

        expect:
        Answers.instanceOfTest(noExceptionThrower) == 'no exception'
        Answers.instanceOfTest(illegalArgumentExceptionThrower) == 'IllegalArgumentException'
        Answers.instanceOfTest(runtimeExceptionThrower) == 'RuntimeException'
        Answers.instanceOfTest(iOExceptionThrower) == 'IOException'
        Answers.instanceOfTest(classNotFoundExceptionThrower) == 'handle rest'
    }

    def "noneOfTest"() {
        given:
        def _1 = Salary.of(1)
        def _3000 = Salary.of(3000)
        def _300 = Salary.of(300)
        and:
        def vip = Person.builder().type(PersonType.VIP)
                .account(Account.builder().salary(_1).build())
                .build()
        def bigSalary = Person.builder().type(PersonType.REGULAR)
                .account(Account.builder().salary(_3000).build())
                .build()
        def regular = Person.builder().type(PersonType.REGULAR)
                .account(Account.builder().salary(_300).build())
                .build()

        expect:
        Answers.noneOfTest(vip) == 'handle rest'
        Answers.noneOfTest(bigSalary) == 'handle rest'
        Answers.noneOfTest(regular) == 'handle special'
    }

    def "anyOfTest"() {
        given:
        def _1 = Salary.of(1)
        def _3000 = Salary.of(3000)
        def _300 = Salary.of(300)
        and:
        def vip = Person.builder().type(PersonType.VIP)
                .account(Account.builder().salary(_1).build())
                .build()
        def bigSalary = Person.builder().type(PersonType.REGULAR)
                .account(Account.builder().salary(_3000).build())
                .build()
        def regular = Person.builder().type(PersonType.REGULAR)
                .account(Account.builder().salary(_300).build())
                .build()

        expect:
        Answers.anyOfTest(vip) == 'handle special'
        Answers.anyOfTest(bigSalary) == 'handle special'
        Answers.anyOfTest(regular) == 'handle rest'
    }
}
