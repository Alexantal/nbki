package ru.nbki;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Setter
@Getter
public class NumberDemonstrator {
    private int minCounterValue;
    private int maxCounterValue;

    private String msgForMultipleThree;
    private String msgForMultipleFive;

    public void startCount() {
        for (int i = minCounterValue; i < maxCounterValue + 1; i++) {
            double remainderByThree = i%3;
            double remainderByFive = i%5;

            if (remainderByThree == 0 && remainderByFive == 0) {
                System.out.println(msgForMultipleThree + msgForMultipleFive);
                continue;
            }
            if (remainderByThree == 0) {
                System.out.println(msgForMultipleThree);
                continue;
            }
            if (remainderByFive == 0) {
                System.out.println(msgForMultipleFive);
                continue;
            }
            System.out.println(i);
        }
    }

    public static void main(String[] args) {
        NumberDemonstrator demonstrator = new NumberDemonstrator(1, 100, "Fizz", "Buzz");
        demonstrator.startCount();
    }
}
