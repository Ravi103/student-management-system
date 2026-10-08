public record Student(String name, int age, String dateOfBirth, String mail, long contact, String address, long id,
        String grade) {
    public Student(String name, int age, String dateOfBirth, String mail, long contact, String address) {
        this(name, age, dateOfBirth, mail, contact, address, 0, "");
    }

    public Student(String name, int age, String dateOfBirth, String mail, long contact, String address, String grade) {
        this(name, age, dateOfBirth, mail, contact, address, 0, grade);
    }

    public Student(String name, int age, String dateOfBirth, String mail, long contact, String address, long id) {
        this(name, age, dateOfBirth, mail, contact, address, id, "");
    }

    @Override
    public String toString() {
        return "{\"id\": " + id + ", \"name\": \"" + name + "\", \"age\": " + age
                + ", \"dateOfBirth\": \"" + dateOfBirth + "\","
                + "  \"mail\": \"" + mail + "\","
                + "  \"contact\": " + contact + ","
                + "  \"address\": \"" + address + "\","
                + "  \"grade\": \"" + grade + "\""
                + "}";
    }
}