package com.pluu.support.olleh

import android.content.Context
import android.text.TextUtils
import com.pluu.kotlin.iterator
import com.pluu.support.impl.AbstractWeekApi
import com.pluu.support.impl.NAV_ITEM
import com.pluu.support.impl.NetworkSupportApi
import com.pluu.webtoon.item.Status
import com.pluu.webtoon.item.WebToonInfo
import com.pluu.webtoon.item.WebToonType
import org.json.JSONArray
import org.json.JSONObject

/**
 * 올레 웹툰 Week API
 * Created by pluu on 2017-04-22.
 */
class OllehWeekApi(context: Context) : AbstractWeekApi(context, OllehWeekApi.TITLE) {
    override val url = HOST + "/api/work/getWorkList.kt"
    private val WEEKLY_VALUE = arrayOf("mondayyn", "tuesdayyn", "wednesdayyn", "thursdayyn", "fridayyn", "saturdayyn", "sundayyn", "endyn")

    private lateinit var savedArray: JSONArray
    private var totalSize: Int = 0

    private val adult = "adult"
    private val novel = "novel"

    override val naviItem: NAV_ITEM
        get() = NAV_ITEM.OLLEH

    override fun parseMain(position: Int): List<WebToonInfo> {
        savedArray = try {
            JSONObject(requestApi()).optJSONArray("workList")
        } catch (e : Exception) {
            e.printStackTrace()
            return emptyList()
        }

        totalSize = savedArray.length()

        val list = mutableListOf<WebToonInfo>()

        var temp: String?
        val optY = "Y"
        val optN = "N"

        val checkValue = WEEKLY_VALUE[position]

        savedArray.iterator().forEach { obj ->
            if (!TextUtils.equals(obj.optString(checkValue), optY)) {
                return@forEach
            }
            val item = WebToonInfo(obj.optString("webtoonseq")).apply {
                title = obj.optString("webtoonnm")
                image = obj.optString("thumbpath")

                writer = buildString {
                    append(obj.optString("authornm1"))

                    temp = obj.optString("authornm2")
                    if (temp?.isNotEmpty() ?: false) {
                        append(", ").append(temp)
                    }
                    temp = obj.optString("authornm3")
                    if (temp?.isNotEmpty() ?: false) {
                        append(", ").append(temp)
                    }
                }

                rate = obj.optString("totalstickercnt")
                updateDate = obj.optString("regdt")

                if (optN == obj.optString("endyn", optN)) {
                    if (optY == obj.optString("upyn", optN)) {
                        // 최근 업데이트
                        status = Status.UPDATE
                    } else if (optY == obj.optString("restyn", optN)) {
                        // 휴재
                        status = Status.BREAK
                    }
                }

                isAdult = adult == obj.optString("agefg")

                if (novel == obj.optString("toonfg")) {
                    type = WebToonType.NOVEL
                }
            }
            list.add(item)
        }
        return list
    }

    override val method: String
        get() = NetworkSupportApi.POST

    override val headers: Map<String, String>
        get() = hashMapOf("Referer" to HOST)

    override val params: Map<String, String>
        get() = hashMapOf("mobileyn" to "N",
                "toonfg" to "toon",
                "toonType" to "toon",
                "sort" to "subject")

    companion object {
        val HOST = "http://m.myktoon.com"
        private val TITLE = arrayOf("월", "화", "수", "목", "금", "토", "일", "완결")
    }
}
