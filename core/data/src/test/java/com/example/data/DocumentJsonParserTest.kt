package com.example.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import com.example.data.local.DocumentJsonParser

class DocumentJsonParserTest {

    @Test
    fun `parse reads document fields`() {
        val json = """
            [
              {
                "ObjectModel": {
                  "ID": "doc-1",
                  "ObjectName": "Соглашение NDA"
                },
                "ObjectInfo": {
                  "Number": "001",
                  "Date": "2026-10-20",
                  "Amount": "1500",
                  "Currency": "RUB",
                  "Status": "На согласовании",
                  "Project": "Проект А",
                  "Comment": "Проверить договор"
                }
              }
            ]
        """.trimIndent()

        val documents = DocumentJsonParser.parse(json)

        assertEquals(1, documents.size)

        val document = documents.first()
        assertEquals("doc-1", document.id)
        assertEquals("Соглашение NDA", document.type)
        assertEquals("001", document.number)
        assertEquals("2026-10-20", document.date)
        assertEquals("1500", document.amount)
        assertEquals("RUB", document.currency)
        assertEquals("На согласовании", document.status)
        assertEquals("Проект А", document.project)
        assertEquals("Проверить договор", document.comment)
    }

    @Test
    fun `parse uses empty strings for missing optional values`() {
        val json = """
            [
              {
                "ObjectModel": {
                  "ID": "doc-2",
                  "ObjectName": "Заявка"
                },
                "ObjectInfo": {
                  "Number": "002",
                  "Date": "2026-10-21",
                  "Amount": "0",
                  "Currency": "RUB",
                  "Status": "Новая",
                  "Project": null,
                  "Comment": null
                }
              }
            ]
        """.trimIndent()

        val document = DocumentJsonParser.parse(json).single()

        assertEquals("", document.project)
        assertEquals("", document.comment)
    }

    @Test
    fun `parse returns empty list for empty array`() {
        val documents = DocumentJsonParser.parse("[]")

        assertTrue(documents.isEmpty())
    }
}
