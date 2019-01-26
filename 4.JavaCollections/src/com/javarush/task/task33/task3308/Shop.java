package com.javarush.task.task33.task3308;

import javax.xml.bind.annotation.*;
import java.util.Arrays;
import java.util.List;


@XmlRootElement(name="shop")
@XmlAccessorType(XmlAccessType.FIELD)
public class Shop {

    public Goods goods;

    public int count;
    public double profit;

    @XmlElement(name="secretData")
    public String[] secretData;

    @XmlType(name = "goods")
    public static class Goods {
        @XmlElement(name="names")
        public List<String> names;

        @Override
        public String toString() {
            return "Goods{" +
                    "names=" + names +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Shop{" +
                "goods=" + goods +
                ", count=" + count +
                ", profit=" + profit +
                ", secretData=" + Arrays.toString(secretData) +
                '}';
    }
}
