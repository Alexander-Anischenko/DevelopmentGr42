package de.ait.homeWork.HomeWork_06;

import java.util.List;
import java.util.stream.Collectors;

public class TVProgramApp {
    private static List<TVProgram> result;
    public static void main(String[] args) {
        List<TVProgram> tvProgramList = TVProgramTestData.getTVProgramList();
        result = getAllProgramsRateHigherThen(tvProgramList, 8);
        for (TVProgram tvProgram : result) {
            System.out.println(tvProgram);
        }


    }

    //Найдите все передачи, рейтинг которых выше заданного порога (например, > 8.0).
    //Подсказка: Используйте метод filter для фильтрации по условию rating > 8.0.
    public static List<TVProgram> getAllProgramsRateHigherThen(List<TVProgram> tvProgramList, double raiting) {

        return tvProgramList.stream()
                .filter(tvProgram -> tvProgram.getRating() > raiting)
                .collect(Collectors.toList());

    }

//    2. Преобразование данных с помощью map
//    Преобразуйте объекты TVProgram в удобные для вывода строки. Например, сформируйте строку в формате:
//            "Канал: [channel] | Передача: [programName] | Рейтинг: [rating]"
//    Подсказка: Используйте String.format (или конкатенацию) и метод map.

//    public static String convertTVProgramObjectsToString(List<TVProgram> tvProgramList) {
//        List<String> collectResult = tvProgramList.stream()
//                .map(tvProgram -> String.format("Канал%s | %s  % .1f", tvProgram.getChannel(), tvProgram.getProgramName(),  tvProgram.getRating()))
//                .collect(Collectors.toList());
//        for (String s : collectResult ) {
//            System.out.println(s);
//        }
//    }
}
