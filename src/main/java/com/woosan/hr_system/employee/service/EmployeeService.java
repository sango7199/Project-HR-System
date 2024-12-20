package com.woosan.hr_system.employee.service;

import com.woosan.hr_system.employee.model.Employee;
import com.woosan.hr_system.search.PageRequest;
import com.woosan.hr_system.search.PageResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface EmployeeService {
    // == 조회 ==
    Employee getEmployeeById(String employeeId);
    String getEmployeeNameById(String employeeId);
    Employee getEmployeeDetails(String employeeId);
    PageResult<Employee> searchEmployees(PageRequest pageRequest, String department, String status);
    List<Employee> getAllEmployee();
    List<Employee> getPreResignationEmployees();
    List<Employee> getResignedEmployees();
    List<Employee> getPreDeletionEmployees();
    List<Employee> getEmployeesByDepartment(String departmentId);
    List<Employee> getEmployeesByDepartmentAndPosition(String department, String position);
    List<String> convertEmployeesToIdList(List<Employee> employeeList);

    // == 등록 ==
    Map<String, Object> insertEmployee(Employee employee, MultipartFile picture);
    String join(Employee employee);

    // == 수정 ==
    String updateEmployee(Employee employee, MultipartFile picture);
    String updateStatus(String employeeId, String status);
    String promoteEmployee(String employeeId);

    // == 삭제 ==
    String deleteEmployee(String employeeId);
}
