package com.example.data.local

import com.example.domain.model.Document
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser

object DocumentJsonParser {

    fun parse(json: String): List<Document> {
        return JsonParser.parseString(json)
            .asJsonArray
            .map { element ->
                val root = element.asJsonObject
                val model = root.getAsJsonObject("ObjectModel")
                val info = root.getAsJsonObject("ObjectInfo")

                Document(
                    id = model.stringValue("ID"),
                    type = model.stringValue("ObjectName"),
                    number = info.stringValue("Number"),
                    date = info.stringValue("Date"),
                    amount = info.stringValue("Amount"),
                    currency = info.stringValue("Currency"),
                    status = info.stringValue("Status"),
                    project = info.stringValue("Project"),
                    comment = info.stringValue("Comment")
                )
            }
    }

    private fun JsonObject.stringValue(key: String): String {
        val value: JsonElement? = get(key)

        return if (value == null || value.isJsonNull) {
            ""
        } else {
            value.asString
        }
    }
}