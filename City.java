import java.util.List;

public class City {
    private String name;
    private List<Person> residents;

    public City() {

    }
    public City(String name, Person... residents) {
        this.name = name;
        this.residents = List.of(residents);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Person> getResidents() {
        return residents;
    }

    public void setResidents(List<Person> residents) {
        this.residents = residents;
    }

}
