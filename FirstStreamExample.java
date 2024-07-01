import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class FirstStreamExample {
    public static void main(String[] args){
        Person p1 = new Person("Paul", 25);
        Person p2 = new Person("John", 30);
        Person p3 = new Person("Jane", 35);
        Person p4 = new Person("Mary", 40);
        Person p5 = new Person("Mark", 45);
        Person p6 = new Person("Sarah", 50);
        Person p7 = new Person("Kate", 55);
        Person p8 = new Person("", 60);
        Person p9 = new Person("Hello", 65);

        List<Person> people = List.of(p1, p2, p3, p4, p5, p6, p7, p8, p9);
        Person[] peopleArray = {p1, p2, p3, p4, p5, p6, p7, p8, p9};

        City city1 = new City("Cairo", p1, p2, p3);
        City city2 = new City("Alex", p4, p5, p6);
        City city3 = new City("Giza", p7, p8, p9);

        List<City> cities = List.of(city1, city2, city3);

        Stream<Person> stream = people.stream();
        stream.map(p -> p.getName().toUpperCase()).filter(name -> !name.isEmpty()).forEach(System.out::println);

        cities.stream().flatMap(city -> city.getResidents().stream()).forEach(p -> System.out.println(p));

        System.out.println(people.stream().count());
        System.out.println(Arrays.stream(peopleArray).count());
    }
}
