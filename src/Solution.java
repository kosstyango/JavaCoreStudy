import java.io.*;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import static java.lang.Integer.parseInt;

public class Solution {
    public static void main(String[] args) {
        init();

    }

    private static void init() {//готовим второй поток
        Thread secThread;
        Runnable runnable = new Runnable() {
            @Override //переопределяем run
            public void run() {
                Elements data = getWeb();//запускаем работу с сайтом ЦБРФ
                //Информация о доступных валютах
                System.out.println("Программа работает с " + (data.size() - 1) + " видами валют по курсу ЦБРФ на текущую дату: ");
                for (int i = 1; i < data.size(); i++) {
                    System.out.println(i + ": \t" + data.get(i).children().get(3).text());
                }
                System.out.println("Пожалуйста, укажите номер валюты для обмена");
                int userChoice1 = getCorrectIntCurrancy(data);
                double currancyRate1 = Double.parseDouble(data.get(userChoice1).children().get(4).text().replace(',', '.'));
                int currancyQuantity1 = Integer.parseInt(data.get(userChoice1).children().get(2).text());

                System.out.println("Спасибо. В какую валюту желаете конвертировать?");
                int userChoice2 = getCorrectIntCurrancy(data);
                double currancyRate2 = Double.parseDouble(data.get(userChoice2).children().get(4).text().replace(',', '.'));
                int currancyQuantity2 = Integer.parseInt(data.get(userChoice2).children().get(2).text());

                System.out.println("Спасибо. Какую сумму хотите обменять?");
                int userChoice3 = getCorrectInt();

                double result = (userChoice3*currancyRate1*currancyQuantity2)/(currancyRate2*currancyQuantity1);
                System.out.println("При обмене "+userChoice3+" "+data.get(userChoice1).children().get(3).text()+" на "+data.get(userChoice2).children().get(3).text()+" Вы получите "+result+" "+data.get(userChoice2).children().get(3).text());
            }
        };
        secThread = new Thread(runnable);
        secThread.start();
    }

    private static Elements getWeb() { //метод приёма массива данных с сайта
        Document doc;
        try {
            doc = Jsoup.connect("https://www.cbr.ru/currency_base/daily/").get();
            Element table = doc.getElementsByTag("tbody").get(0); //забираем первую таблицу с сайта
            Elements strings = table.children(); //создаём массив строк из этой таблицы
            return strings; //возвращаем полученный массив

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Не удалось связаться с Центробанком РФ. \nПожалуйста, попробуйте позже. \nПриносим Вам глубочайшие извинения");
        }
        return null;
    }

    private static int getCorrectIntCurrancy(Elements data) {
        System.out.println("введите порядковый номер");
        int currencyNumber = getCorrectInt();
        boolean isCorrectInput=false;
        while (isCorrectInput==false) {
            if ((currencyNumber >= 1) && (currencyNumber < data.size())) //проверка currencyNumber на диапазон 1-34
            {
                System.out.println("Вы ввели код валюты: " + currencyNumber + ", это - " + data.get(currencyNumber).children().get(3).text() + ".");
                isCorrectInput = true;
                return currencyNumber; //корректный выход
            } else //ошибка по диапазону int
            {
                System.out.println("Вы ввели: " + currencyNumber + ". Валюты с таким номером нет. Диапазон номеров валют от 1 до " + (data.size() - 1));
                currencyNumber = getCorrectInt();
            }
        }
        return 0;
    }//некорректный выход

    static int getCorrectInt() {
        Scanner s = new Scanner(System.in);
        boolean isCorrectInput = false;
        int correctInt = 0;
        while (isCorrectInput == false) { //проверка userInput на int
            String userInput = s.nextLine();
            try {
                correctInt = parseInt(userInput);
                return correctInt; //корректный выход
            } catch (Exception e) //ошибка "это не число"
            {
                System.out.println("Вы ввели: \"" + userInput + "\". Это НЕ число. Введите ЧИСЛО, пожалуйста");
            }
        }
        return 0;
    }//некорректный выход
}
