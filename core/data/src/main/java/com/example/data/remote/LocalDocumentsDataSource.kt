package com.example.data.remote

import android.content.Context
import com.example.domain.model.Document
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalDocumentsDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    suspend fun loadDocuments(): List<Document> =
        withContext(Dispatchers.IO) {
            val json = context.assets
                .open("documents.json")
                .bufferedReader(Charsets.UTF_8)
                .use { it.readText() }

            JsonParser.parseString(json)
                .asJsonArray
                .map { element ->
                    val root = element.asJsonObject
                    val model = root.getAsJsonObject("ObjectModel")
                    val info = root.getAsJsonObject("ObjectInfo")

                    Document(
                        id = infoString(model, "ID"),
                        type = infoString(model, "ObjectName"),
                        number = infoString(info, "Number"),
                        date = infoString(info, "Date"),
                        amount = infoString(info, "Amount"),
                        currency = infoString(info, "Currency"),
                        status = infoString(info, "Status"),
                        project = infoString(info, "Project"),
                        comment = infoString(info, "Comment")
                    )
                }
        }

    private fun infoString(
        json: JsonObject,
        key: String
    ): String {
        val value = json.get(key)

        return if (value == null || value.isJsonNull) {
            ""
        } else {
            value.asString
        }
    }
}