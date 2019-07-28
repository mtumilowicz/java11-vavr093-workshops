import io.vavr.control.Option;

/**
 * Created by mtumilowicz on 2019-03-14.
 */
class CarRepository {
    static Option<Car> findCarById(int id) {
        return Option.when(id == 1, new Car(1, 1));
    }

    static Option<Engine> findEngineById(int id) {
        return Option.when(id == 1, new Engine(1));
    }
}
