package com.harivansh.gitinfo

object InputValidator {
    fun isValid(userName: String): Any {
        return userName.isNotEmpty()
    }
}