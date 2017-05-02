package com.pluu.webtoon.item

/**
 * 상세데이터 타입
 * Created by pluu on 2017-05-02.
 */
data class DetailView(val type: VIEW_TYPE, val value: String? = null, val chatValue: ChatView? = null) {
    var height: Float = 0.toFloat()

    companion object {

        fun createImage(value: String): DetailView {
            return generate(VIEW_TYPE.IMAGE, value)
        }

        fun createChatNotice(value: String): DetailView {
            return DetailView(VIEW_TYPE.CHAT_NOTICE, chatValue = ChatView(null, null, value))
        }

        fun createChatNoticeImage(img: String): DetailView {
            return DetailView(VIEW_TYPE.CHAT_NOTICE_IMAGE, chatValue = ChatView(img, null, null))
        }

        fun createChatLeft(img: String, name: String, text: String): DetailView {
            return DetailView(VIEW_TYPE.CHAT_LEFT, chatValue = ChatView(img, name, text))
        }

        fun createChatRight(img: String, name: String, text: String): DetailView {
            return DetailView(VIEW_TYPE.CHAT_RIGHT, chatValue = ChatView(img, name, text))
        }

        fun createChatEmpty(): DetailView {
            return DetailView(VIEW_TYPE.CHAT_EMPTY)
        }

        fun generate(type: VIEW_TYPE, value: String?): DetailView {
            return DetailView(type, value)
        }
    }
}
