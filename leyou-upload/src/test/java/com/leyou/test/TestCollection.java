package com.leyou.test;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * @author zhoumo
 * @datetime 2018/7/24 17:50
 * @desc
 */
public class TestCollection {


    @Test
    public void test1(){
        ArrayList<Object> list = new ArrayList<>();
        list.add(1);
        Object o = list.remove(0);
        System.out.println("o = " + o);
        System.out.println(list.size());

    }
    @Test
    public void test2(){
        Student s = new Student("jingtian",12);
        Student s3 = new Student("jingtian",12);
        Student s1 = new Student("jingtian",13);
        Student s2 = new Student("reba",18);
//        TreeSet<Student> students = new TreeSet<>(new Comparator<Student>() {
//            @Override
//            public int compare(Student o1, Student o2) {
//                int num1 = o1.getAge() - o2.getAge();
//                int  num2 = num1==0?(o1.getName().compareTo(o2.getName())):num1;
//                return num2;
//            }
//        });
        TreeSet<Student> students = new TreeSet<>((o1,o2)->{
                int num1 = o1.getAge() - o2.getAge();
                int  num2 = num1==0?(o1.getName().compareTo(o2.getName())):num1;
                return num2;
        });
        students.add(s);
        students.add(s1);
        students.add(s2);
        students.add(s3);
        for (Student student : students) {
            System.out.println("student = " + student);
        }

    }
}

class Student{
    private String name;
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Student(String name, Integer age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }


}
