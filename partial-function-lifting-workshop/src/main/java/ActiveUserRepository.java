import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

import java.util.Collection;
import java.util.HashMap;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
class ActiveUserRepository {
    HashMap<Integer, ActiveUser> storage = new HashMap<>();

    void add(ActiveUser user) {
        storage.put(user.getId(), user);
    }

    boolean containsAll(Collection<Integer> ids) {
        return storage.keySet().containsAll(ids);
    }
    
    int count() {
        return storage.size();
    }
}
