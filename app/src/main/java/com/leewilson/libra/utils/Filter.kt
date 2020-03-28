package com.leewilson.libra.utils

import com.leewilson.libra.model.Book

/**
 * Filter a list of books by a specific string. Includes the title, subtitle and authors fields.
 * Does not include description field.
 */
fun List<Book>.filterBy(searchTerm: String): List<Book> {
    val lowerCaseString = searchTerm.toLowerCase()
    return this.filter {book ->
        book.title.toLowerCase().contains(Regex(lowerCaseString)) ||
        book.description.toLowerCase().contains(Regex(lowerCaseString)) ||
        book.authors.toLowerCase().contains(Regex(lowerCaseString))
    }
}
