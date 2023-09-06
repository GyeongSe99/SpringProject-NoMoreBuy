package com.zerobase.nomorebuy.member.type;

public enum Role {
  MEMBER("MEMBER"),
  ADMIN("ADMIN");

  public String role;

  private Role(String role) {
    this.role = role;
  }

  public String getRole() {
    return role;
  }
}
