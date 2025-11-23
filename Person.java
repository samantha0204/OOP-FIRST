public class Person {

    // Attributes
    private String name;
    private int age;
    private String birthDay;
    private String emailAddress;
    private String address;

    public Person() {
    }

    // Parameterized Constructor
    public Person(String name, int age, String birthDay, String emailAddress, String address) {
        this.name = name;
        this.age = age;
        this.birthDay = birthDay;
        this.emailAddress = emailAddress;
        this.address = address;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getAddress() {
        return address;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}
