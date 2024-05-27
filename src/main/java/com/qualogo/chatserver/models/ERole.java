package com.qualogo.chatserver.models;

/**
 * Enumeration representing the different roles available in the chat server.
 * <p>
 * This enum defines three roles:
 * <ul>
 *   <li>ROLE_USER - A standard user with basic permissions.</li>
 *   <li>ROLE_MODERATOR - A user with permissions to moderate content.</li>
 *   <li>ROLE_ADMIN - A user with administrative permissions.</li>
 * </ul>
 */
public enum ERole {
  ROLE_USER,
  ROLE_MODERATOR,
  ROLE_ADMIN
}