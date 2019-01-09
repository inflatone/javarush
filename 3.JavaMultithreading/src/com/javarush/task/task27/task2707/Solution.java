package com.javarush.task.task27.task2707;

/*
Определяем порядок захвата монитора
*/
public class Solution {
    public void someMethodWithSynchronizedBlocks(Object obj1, Object obj2) {
        synchronized (obj1) {
            synchronized (obj2) {
                System.out.println(obj1 + " " + obj2);
            }
        }
    }

    public static boolean isLockOrderNormal(final Solution solution, final Object o1, final Object o2) throws Exception {
        synchronized (o1) {
            Thread first = new Thread(
                    () -> solution.someMethodWithSynchronizedBlocks(o1, o2)
            );
            first.start();

            while(first.getState() != Thread.State.BLOCKED) {
                Thread.sleep(1);
            }


            Thread second = new Thread(
                    () -> {
                        synchronized (o2) {

                        }
                    }
            );
            second.start();
            while(second.getState() != Thread.State.TERMINATED && second.getState() != Thread.State.BLOCKED) {
                Thread.sleep(1);
            }
            return second.getState() == Thread.State.TERMINATED;
        }



        //do something here




    }



    public static void main(String[] args) throws Exception {
        final Solution solution = new Solution();
        final Object o1 = new Object();
        final Object o2 = new Object();

        System.out.println(isLockOrderNormal(solution, o1, o2));
    }
}
