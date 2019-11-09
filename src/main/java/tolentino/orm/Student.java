package tolentino.orm;

public class Student {


    class Entity {

        private int id;
        private String firstName;
        private String middleName;
        private String lastName;

        public Entity() {
            this.id = -1;
            this.firstName = "unspecified";
            this.middleName = "unspecified";
            this.lastName = "unspecified";
        }

        public Entity(int id, String firstName, String middleName, String lastName) {
            this.id = id;
            this.firstName = firstName;
            this.middleName = middleName;
            this.lastName = lastName;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getMiddleName() {
            return middleName;
        }

        public void setMiddleName(String middleName) {
            this.middleName = middleName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }
    }
}
