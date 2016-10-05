package model;

import org.springframework.security.crypto.bcrypt.BCrypt;

import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Entity
public class Person {

  public static int BCRYPT_ROUNDS = 12;

  public Person() {

  }

  public Person(String firstName, String lastName, String email, String phoneNumber, Role role,
      String password) {
    super();
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phoneNumber = phoneNumber;
    this.role = role;
    changePassword(null, password);
  }


  public enum Role {
    Admin, Staff, Student
  }

  private Integer id;
  @NotBlank
  private String firstName;
  @NotBlank
  private String lastName;
  @NotBlank
  @Pattern(regexp = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\\b")
  private String email;
  private String phoneNumber;
  @NotNull
  private Role role;

  @NotNull
  private String passwordHash;
  private String oldPass;


  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  public Integer getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhoneNumber() {
    return phoneNumber;
  }

  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }

  public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  private String getPasswordHash() {
    return passwordHash;
  }

  private void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public boolean checkPassword(String pass) {
    return BCrypt.checkpw(pass, passwordHash);
  }

  public void changePassword(String oldPass, String newPass) {
    if (passwordHash == null || BCrypt.checkpw(oldPass, passwordHash)) {
      setPasswordHash(BCrypt.hashpw(newPass, BCrypt.gensalt(BCRYPT_ROUNDS)));
    }
  }

  @Transient
  public String getOldPass() {
    return oldPass;
  }

  public void setOldPass(String oldPass) {
    this.oldPass = oldPass;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((email == null) ? 0 : email.hashCode());
    result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
    result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
    result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
    result = prime * result + ((role == null) ? 0 : role.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    Person other = (Person) obj;
    if (getEmail() == null) {
      if (other.getEmail() != null) {
        return false;
      }
    } else if (!getEmail().equals(other.getEmail())) {
      return false;
    }
    return true;
  }

}
