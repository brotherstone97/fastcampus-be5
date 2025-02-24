package me.day14.assignment06_review.practice01;

import me.day11.assignment06.practice01.Column;
import me.day11.assignment06.practice01.DataBase;
import me.day11.assignment06.practice01.Student;
import me.day11.assignment06.practice01.Students;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class StudentDao {
    private static StudentDao studentDao; // singleton
    private Map<String, Student> map = DataBase.getInstance().get(0).getData(); // get data stored in database.

    public static StudentDao getInstance() { // singleton
        if ( studentDao == null ) {
            studentDao = new StudentDao();
        }
        return studentDao;
    }


    /**
     * @Param: Student key 값
     * @return: select된 Student 반환
     * */
    public Student select(String key) {
        if ( key == null ) return null;

        if ( map.containsKey(key) ) {
            return map.get(key);
        }
        return null;
    }


    public Students select(Predicate<Student> condition) {
        if ( condition == null ) return null;

        Students students = new Students();
        Iterator<String> iterator = map.keySet().iterator();

        while ( iterator.hasNext() ) {
            String num = iterator.next();
            Student student = map.get(num);
            if ( student == null ) continue;;

            if ( condition.test(student) ) {
                students.add(student);
            }

        }

        return students;
    }


    public Students select() {
        Students studentAll = new Students();
        for ( Student student : map.values() ) {
            studentAll.add(student);
        }
        return studentAll;
    }


    /**
     * @Param: Student key 값, Student 객체 1명
     * @return: DB에 정상적으로 insert 된 객체 반환
     * @exception: 현재 DB에 있는 객체들과 중복된 키를 가질 수 없음
     * */
    public Student insert(String key, Student student) {
        if ( key == null ) return null;
        if ( student == null ) return null;

        if ( !map.containsKey(key) ) {
            map.put(key, student);
        }
        return student;
    }

    /**
     * @Param: Students 들의 key 값들, Students 객체 (Student 객체 여러명 담을 수 있음)
     * @return: Students 객체 중 DB에 정상적으로 insert 된 객체 수 반환
     * @exception: 현재 DB에 있는 객체들과 중복된 키를 가질 수 없음
     * */
    public int insert(List<String> keys, Students students) {
        if ( keys.size() != students.size() ) return 0;

        int count = 0;
        int size = keys.size();
        for ( int i = 0; i < size; i++ ) {
            if ( !map.containsKey(keys.get(i)) ) {
                map.put(keys.get(i), students.get(i));
                count++;
            }
        }
        return count;
    }

    public int update(Predicate<Student> condition, Column column, Object value) {
        if ( condition == null ) return 0;
        if ( column == null || column.getColumnName() == null ) return 0;
        if ( value == null ) return 0;

        int count = 0;
        Iterator<String> iterator = map.keySet().iterator();
        while ( iterator.hasNext() ) {
            String num = iterator.next();
            Student student = map.get(num);

            if ( condition.test(student) ) {
                String columName = column.getColumnName();

                // sId 이외의 다른 요소 모두 삭제 가능
                if ( columName.equals("sPw") ) {
                    student.setsPw((String) value);
                } else if ( columName.equals("sName") ) {
                    student.setsName((String) value);
                } else if ( columName.equals("sAge") ) {
                    student.setsAge((Integer) value);
                } else if ( columName.equals("sGender") ) {
                    student.setsGender((String) value);
                } else if ( columName.equals("sMajor") ) {
                    student.setsMajor((String) value);
                } else {
                    continue;
                }
                count++;
            }
        }

        return count;
    }

    public int delete() {
        return delete(s -> true);
    }

    public int delete(Predicate<Student> condition) {
        if ( condition == null ) return 0;

        int count = 0;
        Iterator<String> iterator = map.keySet().iterator();
        while ( iterator.hasNext() ) {
            String num = iterator.next();
            Student student = map.get(num);

            if ( condition.test(student) ) {
                iterator.remove();
                count++;
            }

        }

        return count;
    }

    /**
     * @Param: Student key 값
     * @return: 삭제된 Student 반환
     * */
    public Student delete(String key) {
        if ( key == null ) return null;

        Student student = null;
        if ( map.containsKey(key) ) {
            student = map.remove(key);
        }
        return student;
    }

}