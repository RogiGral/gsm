package com.gymsystem.gms.constraints;



public class Authority {
    public static final String[] USER_AUTHORITIES = { "user:read" };
    public static final String[] COACH_AUTHORITIES = { "user:read", "user:update","workout:crud" };
    public static final String[] ADMIN_AUTHORITIES = { "user:read", "user:create", "user:update","workout:crud" };
    public static final String[] SUPER_ADMIN_AUTHORITIES = { "user:read", "user:create", "user:update", "user:delete","workout:crud" };
}
