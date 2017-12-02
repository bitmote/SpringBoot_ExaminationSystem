package examination.service;

import examination.dao.ClassDao;
import examination.dao.StudentDao;
import examination.dao.TeacherDao;
import examination.entity.Student;
import examination.entity.Teacher;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {
    @Autowired
    StudentDao studentDao;

    @Autowired
    TeacherDao teacherDao;

    @Autowired
    ClassDao classDao;

    public int addStudent(Student student) {
        String classId = classDao.getIdByName(student.getClassid());
        student.setClassid(classId);
        return studentDao.add(student);
    }

    public Student findStudentById(long id) {
        Student student = studentDao.findById(id);
        if (null == student)
            return null;
        student.setClassid(classDao.getNameById(student.getClassid()));
        return student;
    }

    public int addStudentByExcel(InputStream inputStream) {
        List<Student> students = new ArrayList<Student>();
        try {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                String account = formatter.formatCellValue(row.getCell(0));
                String name = formatter.formatCellValue(row.getCell(1));
                String sex = formatter.formatCellValue(row.getCell(2));
                String className = formatter.formatCellValue(row.getCell(3));
                String classId = classDao.getIdByName(className);
                Student student = new Student(account, name, sex, classId);
                students.add(student);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        return studentDao.addByList(students);
    }

    public void addTeacher(String account, String name) {
        teacherDao.add(account, name);
    }

    public int updateStudent(Student student) {
        student.setClassid(classDao.getIdByName(student.getClassid()));
        return studentDao.update(student);
    }

    public void updateTeacher(Teacher teacher) {
        teacherDao.update(teacher);
    }

    public int addTeacherByExcel(InputStream inputStream) {
        List<Teacher> teachers = new ArrayList<Teacher>();
        try {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                String account = formatter.formatCellValue(row.getCell(0));
                String name = formatter.formatCellValue(row.getCell(1));
                Teacher teacher = new Teacher(account, name);
                teachers.add(teacher);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        }
        return teacherDao.addByList(teachers);
    }


}