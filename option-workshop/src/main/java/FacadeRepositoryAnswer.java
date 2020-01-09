import io.vavr.control.Option;

class FacadeRepositoryAnswer {
    static Option<String> findById(int id) {
        return CacheRepository.findById(id).orElse(() -> DatabaseRepository.findById(id));
    }
}
