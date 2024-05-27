package com.qualogo.chatserver.models;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Represents a user entity in the chat server application.
 */
@Entity
@Table(name = "users", 
    uniqueConstraints = { 
      @UniqueConstraint(columnNames = "username"),
      @UniqueConstraint(columnNames = "email") 
    })
public class User {
  
  /**
   * The unique identifier for the user.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * The username of the user.
   * Must not be blank and have a maximum size of 20 characters.
   */
  @NotBlank
  @Size(max = 20)
  private String username;

  /**
   * The email of the user.
   * Must not be blank, have a maximum size of 50 characters, and be a valid email format.
   */
  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  /**
   * The password of the user.
   * Must not be blank and have a maximum size of 120 characters.
   */
  @NotBlank
  @Size(max = 120)
  private String password;

  /**
   * The roles associated with the user.
   * This is a many-to-many relationship with the Role entity.
   */
  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(  name = "user_roles", 
        joinColumns = @JoinColumn(name = "user_id"), 
        inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  /**
   * Default constructor for the User class.
   */
  public User() {
  }

  /**
   * Constructs a new User with the specified username, email, and password.
   * 
   * @param username the username of the user
   * @param email the email of the user
   * @param password the password of the user
   */
  public User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
  }

  /**
   * Gets the unique identifier of the user.
   * 
   * @return the id of the user
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets the unique identifier of the user.
   * 
   * @param id the new id of the user
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Gets the username of the user.
   * 
   * @return the username of the user
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets the username of the user.
   * 
   * @param username the new username of the user
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Gets the email of the user.
   * 
   * @return the email of the user
   */
  public String getEmail() {
    return email;
  }

  /**
   * Sets the email of the user.
   * 
   * @param email the new email of the user
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Gets the password of the user.
   * 
   * @return the password of the user
   */
  public String getPassword() {
    return password;
  }

  /**
   * Sets the password of the user.
   * 
   * @param password the new password of the user
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Gets the roles associated with the user.
   * 
   * @return the roles of the user
   */
  public Set<Role> getRoles() {
    return roles;
  }

  /**
   * Sets the roles associated with the user.
   * 
   * @param roles the new roles of the user
   */
  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }
}