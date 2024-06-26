package com.pbthnxl.dto;

import com.pbthnxl.validator.UniqueEmail;
import com.pbthnxl.validator.UniqueStudentCode;
import com.pbthnxl.validator.UniqueUsername;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public class StudentUserForm {

    @NotEmpty(message = "{user.firstName.emptyErr}")
    @Size(max = 20, message = "{user.firstName.maxErr}")
    private String firstName;

    @NotEmpty(message = "{user.lastName.emptyErr}")
    @Size(max = 45, message = "{user.lastName.maxErr}")
    private String lastName;

    @UniqueEmail(message = "{user.UniqueEmail.message}")
    @NotEmpty(message = "{user.email.nullErr}")
    @Email(message = "{user.email.invalid}")
    @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="{user.email.invalid}")
    private String email;

    @NotEmpty(message = "{user.phoneNumber.emptyErr}")
    @Size(max = 15, message = "{user.phoneNumber.maxErr}")
    private String phoneNumber;

    @NotEmpty(message = "{user.username.emptyErr}")
    @Size(max = 45, message = "{user.username.maxErr}")
    @UniqueUsername(message = "{user.UniqueUsername.message}")
    private String username;

    @NotEmpty(message = "{user.password.emptyErr}")
    @Size(min = 6, message = "{user.password.minErr}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$", message = "{user.password.regexErr}")
    private String password;

    @NotEmpty(message = "{user.confirmPassword.emptyErr}")
    private String confirmPassword;

    private String role;

    private MultipartFile avatar;

    @UniqueStudentCode(message = "{student.UniqueStudentCode.message}")
    @NotEmpty(message = "Mã số sinh viên không được để trống")
    @Size(max = 15, message = "Mã số sinh viên không được quá 15 ký tự")
    private String studentCode;

    @NotNull(message = "Lớp không được để trống")
    private Integer classId;

    @NotNull(message = "Khoa không được để trống")
    private Integer facultyId;

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the confirmPassword
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * @param confirmPassword the confirmPassword to set
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    /**
     * @return the avatar
     */
    public MultipartFile getAvatar() {
        return avatar;
    }

    /**
     * @param avatar the avatar to set
     */
    public void setAvatar(MultipartFile avatar) {
        this.avatar = avatar;
    }

    /**
     * @return the studentCode
     */
    public String getStudentCode() {
        return studentCode;
    }

    /**
     * @param studentCode the studentCode to set
     */
    public void setStudentCode(String studentCode) {
        this.studentCode = studentCode;
    }

    /**
     * @return the classId
     */
    public Integer getClassId() {
        return classId;
    }

    /**
     * @param classId the classId to set
     */
    public void setClassId(Integer classId) {
        this.classId = classId;
    }

    /**
     * @return the facultyId
     */
    public Integer getFacultyId() {
        return facultyId;
    }

    /**
     * @param facultyId the facultyId to set
     */
    public void setFacultyId(Integer facultyId) {
        this.facultyId = facultyId;
    }

    /**
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }
}
