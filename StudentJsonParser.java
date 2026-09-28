import java.io.IOException;
import java.util.ArrayList;

final class StudentJsonParser {
    private final String json;
    private int index;

    StudentJsonParser(String json) {
        this.json = json;
    }

    ArrayList<Student> parse() throws IOException {
        ArrayList<Student> students = new ArrayList<>();
        expect('[');
        if (!consume(']')) {
            do {
                students.add(parseStudent());
            } while (consume(','));
            expect(']');
        }
        skipWhitespace();
        if (index != json.length()) {
            throw invalidJson();
        }
        return students;
    }

    private Student parseStudent() throws IOException {
        String name = null;
        String dateOfBirth = null;
        String mail = null;
        String address = null;
        Integer age = null;
        Long contact = null;
        Long id = null;
        String grade = "";

        expect('{');
        if (!consume('}')) {
            do {
                String key = parseString();
                expect(':');
                switch (key) {
                    case "id" -> id = Long.parseLong(parseNumber());
                    case "name" -> name = parseString();
                    case "age" -> age = Integer.parseInt(parseNumber());
                    case "dateOfBirth" -> dateOfBirth = parseString();
                    case "mail" -> mail = parseString();
                    case "contact" -> contact = Long.parseLong(parseNumber());
                    case "address" -> address = parseString();
                    case "grade" -> grade = parseString();
                    default -> throw invalidJson();
                }
            } while (consume(','));
            expect('}');
        }

        if (name == null || age == null || dateOfBirth == null || mail == null || contact == null
                || address == null) {
            throw invalidJson();
        }
        return new Student(name, age, dateOfBirth, mail, contact, address, id == null ? 0 : id, grade);
    }

    private String parseString() throws IOException {
        expect('"');
        StringBuilder value = new StringBuilder();
        while (index < json.length()) {
            char character = json.charAt(index++);
            if (character == '"') {
                return value.toString();
            }
            if (character == '\\') {
                if (index >= json.length()) {
                    throw invalidJson();
                }
                char escape = json.charAt(index++);
                switch (escape) {
                    case '"', '\\', '/' -> value.append(escape);
                    case 'b' -> value.append('\b');
                    case 'f' -> value.append('\f');
                    case 'n' -> value.append('\n');
                    case 'r' -> value.append('\r');
                    case 't' -> value.append('\t');
                    case 'u' -> value.append(parseUnicodeEscape());
                    default -> throw invalidJson();
                }
            } else if (character < 0x20) {
                throw invalidJson();
            } else {
                value.append(character);
            }
        }
        throw invalidJson();
    }

    private char parseUnicodeEscape() throws IOException {
        if (index + 4 > json.length()) {
            throw invalidJson();
        }
        try {
            char value = (char) Integer.parseInt(json.substring(index, index + 4), 16);
            index += 4;
            return value;
        } catch (NumberFormatException e) {
            throw invalidJson();
        }
    }

    private String parseNumber() throws IOException {
        skipWhitespace();
        int start = index;
        if (index < json.length() && json.charAt(index) == '-') {
            index++;
        }
        int digitStart = index;
        while (index < json.length() && Character.isDigit(json.charAt(index))) {
            index++;
        }
        if (digitStart == index) {
            throw invalidJson();
        }
        return json.substring(start, index);
    }

    private void expect(char expected) throws IOException {
        skipWhitespace();
        if (index >= json.length() || json.charAt(index++) != expected) {
            throw invalidJson();
        }
    }

    private boolean consume(char expected) {
        skipWhitespace();
        if (index < json.length() && json.charAt(index) == expected) {
            index++;
            return true;
        }
        return false;
    }

    private void skipWhitespace() {
        while (index < json.length() && Character.isWhitespace(json.charAt(index))) {
            index++;
        }
    }

    private IOException invalidJson() {
        return new IOException("Invalid student JSON array at position " + index);
    }
}