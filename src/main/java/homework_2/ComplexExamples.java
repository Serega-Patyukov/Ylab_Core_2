package homework_2;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

public class ComplexExamples {

    static class Person {
        final int id;

        final String name;

        Person(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Person person)) return false;
            return getId() == person.getId() && getName().equals(person.getName());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getId(), getName());
        }
    }

    private static Person[] RAW_DATA = new Person[]{
            new Person(0, "Harry"),
            new Person(0, "Harry"), // дубликат
            new Person(1, "Harry"), // тёзка
            new Person(2, "Harry"),
            new Person(3, "Emily"),
            new Person(4, "Jack"),
            new Person(4, "Jack"),
            new Person(5, "Amelia"),
            new Person(5, "Amelia"),
            new Person(6, "Amelia"),
            new Person(7, "Amelia"),
            new Person(8, "Amelia"),
    };
        /*  Raw data:

        0 - Harry
        0 - Harry
        1 - Harry
        2 - Harry
        3 - Emily
        4 - Jack
        4 - Jack
        5 - Amelia
        5 - Amelia
        6 - Amelia
        7 - Amelia
        8 - Amelia

        **************************************************

        Duplicate filtered, grouped by name, sorted by name and id:

        Amelia:
        1 - Amelia (5)
        2 - Amelia (6)
        3 - Amelia (7)
        4 - Amelia (8)
        Emily:
        1 - Emily (3)
        Harry:
        1 - Harry (0)
        2 - Harry (1)
        3 - Harry (2)
        Jack:
        1 - Jack (4)
     */

    public static void main(String[] args) {
        System.out.println("Raw data:");
        System.out.println();

        for (Person person : RAW_DATA) {
            System.out.println(person.id + " - " + person.name);
        }

        System.out.println();
        System.out.println("**************************************************");
        System.out.println();
        System.out.println("Duplicate filtered, grouped by name, sorted by name and id:");
        System.out.println();

        /*
        Task1
            Убрать дубликаты, отсортировать по идентификатору, сгруппировать по имени

            Что должно получиться
                Key: Amelia
                Value:4
                Key: Emily
                Value:1
                Key: Harry
                Value:3
                Key: Jack
                Value:1
         */

        Map<String, List<Person>> resultMap = Arrays.stream(Optional.of(RAW_DATA).orElseThrow(RuntimeException::new))
                .filter(Objects::nonNull)
                .distinct()
                .sorted(Comparator.comparingInt(Person::getId))
                .collect(Collectors.groupingBy(Person::getName));   // Эта строка заменила километры закомментированного кода.
//                .map(person -> {
//                    List<Person> personList = new ArrayList<>();
//                    personList.add(person);
//                    Map<String, List<Person>> mapTemp = new HashMap<>();
//                    mapTemp.put(person.getName(), personList);
//                    return mapTemp;
//                })
//                .reduce((mapTemp1, mapTemp2) -> {
//                    String keyMapTemp2 = mapTemp2.keySet().stream().findAny().orElseThrow(NullPointerException::new);
//                    if (mapTemp1.containsKey(keyMapTemp2)) {
//                        List<Person> personList = mapTemp1.get(keyMapTemp2);
//                        Person person = mapTemp2.get(keyMapTemp2).get(0);
//                        personList.add(person);
//                    } else {
//                        mapTemp1.put(keyMapTemp2, mapTemp2.get(keyMapTemp2));
//                    }
//                    return mapTemp1;
//                })
//                .orElseThrow(NullPointerException::new);

        List<Map.Entry<String, List<Person>>> keyList = new ArrayList<>(resultMap.entrySet());
        Collections.sort(keyList, Comparator.comparing(Map.Entry::getKey));

        for (int i = 0; i < keyList.size(); i++) {
            Map.Entry<String, List<Person>> entry = keyList.get(i);
            System.out.println(entry.getKey());
            for (int j = 0; j < entry.getValue().size(); j++) {
                Person person = entry.getValue().get(j);
                System.out.println(j + 1 + " - " + person.getName() + " (" + person.getId() + ")");
            }
        }

        /*
        Task2
            [3, 4, 2, 7], 10 -> [3, 7] - вывести пару именно в скобках, которые дают сумму - 10
         */

        System.out.println();
        System.out.println("**************************************************");
        System.out.println();

        int array[] = {3, 4, 2, 7};
        int n = 10;

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length; j++) {
                if (array[i] + array[j] == n && i != j) {
                    System.out.println("[" + array[i] + ", " + array[j] + "]");
                    i = j = array.length;
                }
            }
        }

        /*
        Task3
            Реализовать функцию нечеткого поиска

                    fuzzySearch("car", "ca6$$#_rtwheel"); // true
                    fuzzySearch("cwhl", "cartwheel"); // true
                    fuzzySearch("cwhee", "cartwheel"); // true
                    fuzzySearch("cartwheel", "cartwheel"); // true
                    fuzzySearch("cwheeel", "cartwheel"); // false
                    fuzzySearch("lw", "cartwheel"); // false
         */

        System.out.println();
        System.out.println("**************************************************");
        System.out.println();

        System.out.println(fuzzySearch("car", "ca6$$#_rtwheel")); // true
        System.out.println(fuzzySearch("cwhl", "cartwheel")); // true
        System.out.println(fuzzySearch("cwhee", "cartwheel")); // true
        System.out.println(fuzzySearch("cartwheel", "cartwheel")); // true
        System.out.println(fuzzySearch("cwheeel", "cartwheel")); // false
        System.out.println(fuzzySearch("lw", "cartwheel")); // false
    }

    public static boolean fuzzySearch(@NonNull String substring, @NonNull String str) {

        if (substring.length() == 0) {
            return true;
        }

        int j = 0;
        for (int i = 0; i < str.length(); i++) {
            if (substring.charAt(j) == str.charAt(i)) {
                if (j == substring.length() - 1) {
                    return true;
                } else {
                    j++;
                }
            }
        }
        return false;
    }
}