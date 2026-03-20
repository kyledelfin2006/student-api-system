import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StudentRepository implements Repository<Student>{
   private final List<Student> studentList;

    public StudentRepository(List<Student> studentList) {
        this.studentList = studentList;
    }


    @Override
    public void add(Student s) {
      studentList.add(s);
    }

    @Override
    public void remove(Student s) {
     studentList.remove(s);
    }

    @Override
    public List<Student> getAll() {
        return Collections.unmodifiableList(studentList);
    }

    @Override
    public void clear() {
     studentList.clear();
    }

    public void addAll(List<Student> students){
        studentList.addAll(students);
    }

}
