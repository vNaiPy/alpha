package com.naipy.alpha.modules.user.models;


import java.io.Serializable;

public record AuthenticationResponse (String token) implements Serializable {}
