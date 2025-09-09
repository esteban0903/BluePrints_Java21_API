package edu.eci.arsw.blueprints.controllers;

public record ApiResponseDTO<T>(int code, String message, T data) {

}
