class Repository {
    User findById(int id) {
        if (id == 1) {
            return new User(1);
        }

        throw new UserCannotBeFoundException();
    }
}

class UserCannotBeFoundException extends RuntimeException {

}
